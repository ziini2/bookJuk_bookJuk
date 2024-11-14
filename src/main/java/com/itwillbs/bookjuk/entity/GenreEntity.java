package com.itwillbs.bookjuk.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itwillbs.bookjuk.entity.bookInfo.BookInfoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	// 여러 개의 도서가 하나의 장르에 속함 => OneToMany 관계 설정
    @OneToMany(mappedBy = "genre")
	@JsonIgnore // 순환 참조 방지를 위해 직렬화에서 제외
	private Set<BookInfoEntity> books;

	// 장르이름
	@Column(nullable = false)
	private String genreName;


}
