package com.itwillbs.bookjuk.dto;

import com.itwillbs.bookjuk.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContentDTO {

    private Long memberNum;
    private int bringBook;
    private int userPoint;
    private UserEntity userNum;
}
