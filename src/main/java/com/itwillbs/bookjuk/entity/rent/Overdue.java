package com.itwillbs.bookjuk.entity.rent;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "overdue")
public class Overdue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "over_num", nullable = false)
    private Long id;

    @Column(name = "over_price", nullable = false)
    private Integer overPrice;

    @Column(name = "over_start", nullable = false)
    private Instant overStart;

    @Column(name = "over_end", nullable = false)
    private Instant overEnd;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    private Instant updateDate;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rent_num", nullable = false) // RentEntity를 외래 키로 참조
    private RentEntity rent;
}