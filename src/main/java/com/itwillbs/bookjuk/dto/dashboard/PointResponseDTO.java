package com.itwillbs.bookjuk.dto.dashboard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PointResponseDTO {

    private List<CategoryData> gender;
    private List<CategoryData> age;
    private List<CategoryData> genre;
    private List<CategoryData> store;
}
