package edu.jcourse.jpa.repository;

import edu.jcourse.jpa.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {
}