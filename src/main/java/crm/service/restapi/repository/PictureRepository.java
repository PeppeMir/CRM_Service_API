package crm.service.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import crm.service.restapi.model.Picture;

@Repository("pictureRepository")
public interface PictureRepository extends JpaRepository<Picture, Long> {
	
}
