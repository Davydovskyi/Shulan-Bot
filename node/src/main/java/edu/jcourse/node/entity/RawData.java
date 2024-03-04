package edu.jcourse.node.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
@EqualsAndHashCode(exclude = {"event"})
@ToString(exclude = {"event"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "raw_data")

//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class) - for spring and hibernate less than 6
//@Convert(attributeName = "jsonb", converter = JsonBinaryType.class) - for spring 6
public class RawData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Type(type = "jsonb") - for spring and hibernate less than 6
//    @JdbcTypeCode(SqlTypes.JSON) - for spring 6

    @Type(JsonBinaryType.class)
    @Column(name = "event", nullable = false, columnDefinition = "jsonb")
    private Update event;
}