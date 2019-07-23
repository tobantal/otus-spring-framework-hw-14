package ru.otus.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.otus.spring.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	List<Comment> findAllByBookId(Long bookId);
	
}
