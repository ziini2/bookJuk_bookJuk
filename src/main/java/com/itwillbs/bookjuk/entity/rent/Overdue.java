package com.itwillbs.bookjuk.entity.rent;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "overdue")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Overdue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "over_num", nullable = false)
    private Long id;

    @Column(name = "over_price", nullable = false)
    private Integer overPrice;

    @Column(name = "over_start", nullable = false)
    private LocalDate overStart;

    @Column(name = "over_end", nullable = false)
    private LocalDate overEnd;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rent_num", nullable = false) // RentEntity를 외래 키로 참조
    private RentEntity rent;
}