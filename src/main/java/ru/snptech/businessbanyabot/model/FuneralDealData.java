package ru.snptech.businessbanyabot.model;

public record FuneralDealData(
        String id,
        String source,
        String source2,
        String dealType,
        String comment,
        String address,
        String city,
        String customerName,
        String deceasedSurname,
        String region
) { }
