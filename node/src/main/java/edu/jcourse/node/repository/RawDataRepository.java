package edu.jcourse.node.repository;

import edu.jcourse.node.entity.RawData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawDataRepository extends JpaRepository<RawData, Long> {
}