package crm.service.restapi.service.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {

    String store(MultipartFile multipartFile) throws IOException;

    void delete(String url) throws IOException;
}
