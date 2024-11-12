package com.itwillbs.bookjuk.entity.rent;

import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "rent")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rent_num", nullable = false)
	private Long rentNum;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_num", nullable = false)
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "store_code", nullable = false)
	private StoreEntity storeCode;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "books_id", nullable = false)
	private BooksEntity book;

	@Column(name = "rent_price", nullable = false)
	private Integer rentPrice;

	@Column(name = "return_date")
	private Instant returnDate;

	@ColumnDefault("0")
	@Column(name = "rent_status", nullable = false)
	private Byte rentStatus;

	@ColumnDefault("CURRENT_TIMESTAMP")
	@Column(name = "create_date", nullable = false)
	private Instant createDate;

	@ColumnDefault("CURRENT_TIMESTAMP")
	@UpdateTimestamp
	@Column(name = "update_date", nullable = false)
	private Instant updateDate;

	@OneToOne(mappedBy = "rent")
	private Overdue overdue;

	@OneToOne(mappedBy = "rent")
	private PointDealEntity pointDeal;

	@Column(name = "rent_start", nullable = false)
	private LocalDate rentStart;

	@Column(name = "rent_end", nullable = false)
	private LocalDate rentEnd;

}
