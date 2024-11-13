package com.itwillbs.bookjuk.entity.rent;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.itwillbs.bookjuk.entity.StoreEntity;
import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.entity.books.BooksEntity;
import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Table(name = "rent")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rent_num", nullable = false)
	private Long rentNum;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_num", nullable = false)
	@JsonBackReference
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "store_code", nullable = false)
	@JsonBackReference
	private StoreEntity storeCode;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "books_id", nullable = false)
	@JsonBackReference
	private BooksEntity book;

	@Column(name = "rent_price", nullable = false)
	private Integer rentPrice;

	@Column(name = "return_date")
	private LocalDate returnDate; // LocalDate로 변경

	@Column(name = "rent_status", nullable = false)
	private Byte rentStatus = 0; // 기본값 설정

	@Column(name = "create_date", nullable = false, updatable = false)
	private Instant createDate;

	@UpdateTimestamp
	@Column(name = "update_date", nullable = false)
	private Instant updateDate;

	@OneToOne(mappedBy = "rent")
	@JsonManagedReference
	private Overdue overdue;

	@OneToOne(mappedBy = "rent")
	@JsonManagedReference
	private PointDealEntity pointDeal;

	@Column(name = "rent_start", nullable = false)
	private LocalDate rentStart;

	@Column(name = "rent_end", nullable = false)
	private LocalDate rentEnd;

	@PrePersist
	public void prePersist() {
		this.createDate = Instant.now();
		this.updateDate = Instant.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updateDate = Instant.now();
	}
}
