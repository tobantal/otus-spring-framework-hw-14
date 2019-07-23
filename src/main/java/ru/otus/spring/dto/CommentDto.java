package ru.otus.spring.dto;

import lombok.Data;
import ru.otus.spring.domain.Book;

@Data
public class CommentDto {
	
	private final String text;
	private final Book book;

}
