package crm.service.restapi.service;

import crm.service.restapi.model.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PictureService {

    Picture store(MultipartFile file) throws IOException;

    void delete(Picture picture) throws IOException;
}
