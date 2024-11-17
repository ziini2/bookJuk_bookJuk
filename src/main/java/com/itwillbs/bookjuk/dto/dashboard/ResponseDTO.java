package com.itwillbs.bookjuk.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResponseDTO {

    List<CategoryData> gender;
    List<CategoryData> age;
    List<CategoryData> genre;
    List<CategoryData> store;
}
