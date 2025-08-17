package ru.snptech.businessbanyabot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.snptech.businessbanyabot.model.payment.PaymentContent;
import ru.snptech.businessbanyabot.model.payment.PaymentStatus;
import ru.snptech.businessbanyabot.model.payment.PaymentType;
import ru.snptech.businessbanyabot.service.payment.PaymentContentConverter;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "external_id")
    private String externalId;

    @Column(columnDefinition = "text")
    @Convert(converter = PaymentContentConverter.class)
    private PaymentContent content;

    public <T extends PaymentContent> T getContentAs(Class<T> type) {
        if (type.isInstance(content)) {
            return type.cast(content);
        }

        throw new IllegalArgumentException("Content is not of type " + type.getSimpleName());
    }

    @Enumerated(value = EnumType.STRING)
    private PaymentType type;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;

    private Integer amount;

    private String currency;

    @ManyToOne
    @JoinColumn(
        name = "chat_id",
        referencedColumnName = "chat_id"
    )
    private TelegramUser user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = false)
    private Instant updatedAt;

    @Column(name = "expired_at")
    private Instant expiredAt;
}
