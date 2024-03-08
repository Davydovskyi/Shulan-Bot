package edu.jcourse.jpa.repository;

import edu.jcourse.jpa.entity.AppDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppDocumentRepository extends JpaRepository<AppDocument, Long> {
}