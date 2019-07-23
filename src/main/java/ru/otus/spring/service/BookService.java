package ru.otus.spring.service;

import java.util.List;

import ru.otus.spring.domain.Book;

public interface BookService {
	
	long size();
	
	Book findBookByName(String name);
	
	Book save(Book book);
		
	Book findBookById(Long id);
	
	void deleteBookById(Long id);
	
	List<Book> findAllBooks();
	
	List<Book> findBooksByAuthor(String author);
	
	List<Book> findBooksByGenre(String genre);

}
