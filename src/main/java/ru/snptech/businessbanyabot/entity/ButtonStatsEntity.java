package ru.snptech.businessbanyabot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "button_stats")
@NoArgsConstructor
@AllArgsConstructor
public class ButtonStatsEntity {

    @Id
    @Column(name = "button_name", nullable = false)
    private String buttonName;

    @Column(name = "count")
    private Long count = 0L;
}