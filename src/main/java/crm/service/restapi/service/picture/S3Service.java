package crm.service.restapi.service.picture;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import crm.service.restapi.exception.GenericErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class S3Service {

    /*@Value("${amazonws.s3.endpointUrl}")
    private String endpointUrl;*/

    @Value("${amazonws.s3.endpointRegion}")
    private String endpointRegion;

    @Value("${amazonws.s3.bucketName}")
    private String bucketName;

    @Value("${amazonws.s3.accessKey}")
    private String accessKey;

    @Value("${amazonws.s3.secretKey}")
    private String secretKey;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AmazonS3 s3client;

    @PostConstruct
    private void initializeS3() {

        final AWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(endpointRegion))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public String store(final MultipartFile multipartFile) throws IOException {

        final String customFilename = washAndgenerateCustomFilename(multipartFile.getOriginalFilename());

        final File file;
        try {
            file = convertFile(multipartFile);
        } catch (final IOException e) {
            logger.error("Error while converting multipart file", e);
            throw new IOException("An error occurred while uploading the file");
        }

        final PutObjectRequest putRequest = new PutObjectRequest(bucketName, customFilename, file)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        try {
            s3client.putObject(putRequest);
        } catch (final AmazonServiceException e) {
            logger.error("Error while uploading the file to S3. Filename: '" + customFilename + "'", e);
            throw new IOException("An error occurred while uploading the file");
        }

        file.delete();

        // this call to getUrl can be avoided since the URL of the image is built as
        // "https://[bucket].s3.[region].amazonaws.com/[customFilename]"
        // however, perform a call makes this service class independant from this logic
        return s3client.getUrl(bucketName, customFilename).toExternalForm();
    }

    public void delete(final String url) throws IOException {

        //final String filename = Paths.get(url).getFileName().toString();
        final String filename = url.substring(url.lastIndexOf("/") + 1);

        final DeleteObjectRequest deleteRequest = new DeleteObjectRequest(bucketName, filename);

        try {
            s3client.deleteObject(deleteRequest);
        } catch (final AmazonServiceException e) {
            logger.error("Error while deleting the file from S3. URL: '" + url + "'", e);
            throw new IOException("An error occurred while deleting the file");
        }
    }

    private File convertFile(final MultipartFile file) throws IOException {

        final File convertedFile = new File(file.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }

        return convertedFile;
    }

    private String washAndgenerateCustomFilename(final String filename) {

        return new StringBuilder(UUID.randomUUID().toString())
                .append("_")
                .append(filename.replace(" ", "_"))
                .toString();
    }
}
