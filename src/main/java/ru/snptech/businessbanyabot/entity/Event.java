package ru.snptech.businessbanyabot.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import ru.snptech.businessbanyabot.integration.bank.dto.common.WeekDay;
import ru.snptech.businessbanyabot.integration.bitrix.dto.common.FileDto;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "external_id")
    private String externalId;

    private String title;

    private String speaker;

    private Instant carryDate;

    @Column(name = "week_day")
    @Enumerated(value = EnumType.STRING)
    private WeekDay weekDay;

    private LocalTime time;

    @Type(JsonType.class)
    @Column(name = "full_description", columnDefinition = "jsonb")
    private FileDto fullDescription;

    @Type(JsonType.class)
    @Column(name = "preview_description", columnDefinition = "jsonb")
    private FileDto previewDescription;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private FileDto photo;

    @Column(name = "registration_link")
    private String registrationLink;

    @Column(name = "table_link")
    private String tableLink;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

}
