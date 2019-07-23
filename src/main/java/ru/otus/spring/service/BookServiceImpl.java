package ru.otus.spring.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
	private final GenreService genreService;
	private final AuthorService authorService;

	@Override
	@Transactional(readOnly = true)
	public long size() {
		return bookRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Book findBookByName(String name) {
		return bookRepository.findByName(name).get();
	}

	@Override
	public void deleteBookById(Long id) {
		Book book = bookRepository.findById(id).orElseThrow(NoSuchElementException::new);
			
		bookRepository.deleteById(id);
		
		Author author = book.getAuthor();
		if(findBooksByAuthor(author.getName()).isEmpty()) {
			authorService.deleteById(author.getId());
		}
		
		Genre genre = book.getGenre();
		if(findBooksByGenre(genre.getName()).isEmpty()) {
			genreService.deleteById(genre.getId());
		}	
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> findAllBooks() {
		return bookRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> findBooksByAuthor(String author) {
		return bookRepository.findAllByAuthorName(author);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Book> findBooksByGenre(String genre) {
		return bookRepository.findAllByGenreName(genre);
	}

	@Override
	public Book save(Book book) {
		Long id = book.getId();
		
		String name = book.getName();
		String authorName = book.getAuthor().getName();
		String genreName = book.getGenre().getName();
		Author author = authorService.createIfItIsNecessaryAndGet(authorName);
		Genre genre = genreService.createIfItIsNecessaryAndGet(genreName);
				
		if (id != null) {
			findBookById(id);
			book.setAuthor(author);
			book.setGenre(genre);
			return bookRepository.save(book);
		}
		
		try {
			findBookByName(name);
			String error = String.format("The book with the name <%s> already exists", name);
			throw new PermissionDeniedDataAccessException(error, null);
		} catch (NoSuchElementException e) {
			book = new Book(null, name, author, genre);
			return bookRepository.save(book);
		}
	}

	@Override
	public Book findBookById(Long id) {
		return bookRepository.findById(id).orElseThrow(NoSuchElementException::new);
	}

}
