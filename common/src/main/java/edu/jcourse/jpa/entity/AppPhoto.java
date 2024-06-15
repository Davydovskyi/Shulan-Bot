package edu.jcourse.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "app_photo")
public class AppPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "telegram_file_id", unique = true, nullable = false)
    private String telegramFileId;
    @OneToOne
    @JoinColumn(name = "binary_content_id", nullable = false)
    private BinaryContent binaryContent;
    @Column(name = "file_size", nullable = false)
    private Integer fileSize;
}