package crm.service.restapi.service;

import crm.service.restapi.model.Picture;
import crm.service.restapi.repository.PictureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pictureService")
@Transactional
public class PictureServiceImpl implements PictureService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PictureRepository pictureRepository;

    @Override
    public void delete(final Picture picture) {

        logger.info("Deleting picture {}", picture);

        pictureRepository.delete(picture);
    }
}
