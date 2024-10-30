package com.itwillbs.bookjuk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "genre")
public class GenreEntity {
	
	// 장르ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long genreId;
		
	// 장르이름
	@Column(nullable = false)
	private String genreName;

}
