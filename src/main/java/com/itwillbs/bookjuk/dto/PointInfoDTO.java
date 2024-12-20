package com.itwillbs.bookjuk.dto;

import com.itwillbs.bookjuk.domain.pay.PointPayStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointInfoDTO {
    private String reqDate;
    private int pointPrice;
    private String pointPayName;
    private String pointPayStatus;

}
