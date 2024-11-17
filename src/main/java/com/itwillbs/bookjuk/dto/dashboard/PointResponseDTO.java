package com.itwillbs.bookjuk.dto.dashboard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PointResponseDTO {

    private List<CategoryPointData> gender;
    private List<CategoryPointData> age;
    private List<CategoryPointData> genre;
    private List<CategoryPointData> store;
}
