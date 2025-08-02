package ru.snptech.businessbanyabot.model.search;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchMetadata {
    private String searchString;
    private Integer residentSliderCursor;
}
