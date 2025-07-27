package ru.snptech.businessbanyabot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hpsf.Currency;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "qr_code")
public class QrCode {

    private String id;

    private String link;

    private Integer paymentAmount;

    private Currency currency;

    private String content;

    private Instant createdAt;

}