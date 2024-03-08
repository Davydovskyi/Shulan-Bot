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
@Table(name = "app_document")
public class AppDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "telegram_file_id", unique = true, nullable = false)
    private String telegramFileId;
    @Column(name = "doc_name", nullable = false)
    private String docName;
    @OneToOne
    @JoinColumn(name = "binary_content_id", nullable = false)
    private BinaryContent binaryContent;
    @Column(name = "mime_type", nullable = false)
    private String mimeType;
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
}