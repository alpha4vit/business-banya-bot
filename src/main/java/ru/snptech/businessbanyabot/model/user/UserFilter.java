package ru.snptech.businessbanyabot.model.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFilter {
    private String username;
    private String status;
    private Integer minAge;
    private Integer maxAge;
}
