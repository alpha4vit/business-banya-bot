package ru.snptech.ritualbitrixbot.model;

public record ShopDealData(
    String id,
    String source,
    String source2,
    String dealType,
    String comment,
    String clientName,
    String clientPhone,
    String region
) { }
