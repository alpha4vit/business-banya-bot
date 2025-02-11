package ru.snptech.ritualbitrixbot.integration.bitrix.dto;

import java.util.Map;

public record BitrixCrmDealUpdate(
    Long id,
    Map<String, String> fields
) { }
