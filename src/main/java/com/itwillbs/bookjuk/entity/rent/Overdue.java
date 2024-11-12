package com.itwillbs.bookjuk.entity.rent;

import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "overdue")
public class Overdue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "over_num", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rent_num", nullable = false)
    private RentEntity rent;

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

    @OneToOne(mappedBy = "overdue")
    private PointDealEntity pointDeal;

}