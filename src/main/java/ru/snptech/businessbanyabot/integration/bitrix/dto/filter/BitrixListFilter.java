package ru.snptech.businessbanyabot.integration.bitrix.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BitrixListFilter {
    private Map<String, String> filter;

    private List<String> select;

    private Integer start;
}
