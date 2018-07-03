package crm.service.restapi.service.picture;

import crm.service.restapi.model.Picture;
import crm.service.restapi.repository.PictureRepository;
import crm.service.restapi.service.s3.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service("pictureService")
@Transactional
public class PictureServiceImpl implements PictureService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final List<MediaType> SUPPORTED_MEDIA_TYPES
            = Arrays.asList(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG);

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private S3Service s3Service;

    @Override
    public Picture store(final MultipartFile file) throws IOException {

        final MediaType mediaType = MediaType.valueOf(file.getContentType());

        if (!SUPPORTED_MEDIA_TYPES.contains(mediaType)) {
            logger.error("Unsupported media type '{}' specified for the upload", mediaType);
            throw new IOException("Unsupported media type '" + mediaType + "'. Supported media types: " + SUPPORTED_MEDIA_TYPES.toString());
        }

        final String url = s3Service.store(file);

        final Picture picture = new Picture();
        picture.setFilename(file.getOriginalFilename());
        picture.setMediaType(file.getContentType());
        picture.setUrl(url);

        return picture;
    }

    @Override
    public void delete(final Picture picture) throws IOException {

        logger.info("Deleting picture {}", picture);

        s3Service.delete(picture.getUrl());

        pictureRepository.delete(picture);
    }
}
