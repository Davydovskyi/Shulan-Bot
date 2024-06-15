package edu.jcourse.jpa.repository;

import edu.jcourse.jpa.entity.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPhotoRepository extends JpaRepository<AppPhoto, Long> {
}