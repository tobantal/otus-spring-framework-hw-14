package ru.otus.spring.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

@Controller
@RequiredArgsConstructor
public class BookController {
	
	private final BookService bookService;
	private final CommentService commentService;
	
	@GetMapping("/books")
	public ModelAndView findAllBooks(ModelMap model) {
		model.addAttribute("books", bookService.findAllBooks());
		model.addAttribute("booksSizeInfo", String.format("books are %d", bookService.size()));
		return new ModelAndView("books-info", model, HttpStatus.FOUND);
	}
	
	@DeleteMapping("/books/{id}")
	public String deleteBook(@PathVariable("id") Long id) {
		commentService.removeAllCommentsByBook(new Book(id, null, null, null));
		bookService.deleteBookById(id);
		return "redirect:/books";
	}
	
	@GetMapping("/book-form")
	public ModelAndView emptyBookForm(ModelMap model) {
		model.addAttribute("book", new Book());	
		return new ModelAndView("book-form", model, HttpStatus.OK);
	}
	
	@PutMapping("/book-form/{id}")
	public ModelAndView bookFormForUpdate(@PathVariable(value = "id", required = false) Long id, ModelMap model) {
		model.addAttribute("book", bookService.findBookById(id));	
		return new ModelAndView("book-form", model, HttpStatus.OK);
	}
	
	@PostMapping("/books")
	public String saveBook(Book book) {
		bookService.save(book);
		return "redirect:/books";
	}
	
	@PostMapping("/comment")
	public String addComment(CommentDto commentDto) {
		Comment comment = new Comment(null, commentDto.getText(), LocalDateTime.now(), commentDto.getBook());
		commentService.addComment(comment);
		return "redirect:/books/" + comment.getBook().getId() + "/comments";
	}
	
	@GetMapping("/books/{id}/comments")
	public ModelAndView comments(@PathVariable("id") Long id, ModelMap model) throws SQLException {
		Book book = bookService.findBookById(id);
		List<Comment> comments = commentService.getCommentsForBook(book);
		CommentDto commentDto = new CommentDto(null, book);
		model.addAttribute("commentDto", commentDto);
		model.addAttribute("comments", comments);
		return new ModelAndView("comments-info", model, HttpStatus.FOUND);
	}
}
