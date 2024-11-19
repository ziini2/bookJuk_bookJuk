package com.itwillbs.bookjuk.entity.rent;

import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "rent")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rent_num", nullable = false)
	private Long rentNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_num", nullable = false)
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_code", nullable = false)
	private StoreEntity storeCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "books_id", nullable = false)
	private BooksEntity book;

	@Column(name = "rent_price", nullable = false)
	private Integer rentPrice;

	@Column(name = "return_date")
	private LocalDate returnDate;

	@Column(name = "rent_status", nullable = false)
	private Boolean rentStatus = false;

	@Column(name = "create_date", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createDate;

	@UpdateTimestamp
	@Column(name = "update_date", nullable = false)
	private LocalDateTime updateDate;

	@Column(name = "rent_start", nullable = false)
	private LocalDate rentStart;

	@Column(name = "rent_end", nullable = false)
	private LocalDate rentEnd;




}
