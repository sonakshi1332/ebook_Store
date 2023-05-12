package com.sonakshi.ebookMaker.services;

import com.sonakshi.ebookMaker.entities.Book;
import com.sonakshi.ebookMaker.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ISBNGeneratorService isbnGeneratorService;

    @Autowired
    private BookTemplateService bookTemplateService;

    public Book save(Book book) {
        book.setIsbn(isbnGeneratorService.generateISBN());
        bookTemplateService.applyTemplate(book, book.getTemplate());
        bookTemplateService.generateTitlePage(book);
        return bookRepository.save(book);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book update(Long id, Book updatedBook) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setDescription(updatedBook.getDescription());
            book.setTemplate(updatedBook.getTemplate());
            book.setEbookFormat(updatedBook.getEbookFormat());
            bookTemplateService.applyTemplate(book, book.getTemplate());
            bookTemplateService.generateTitlePage(book);
            return bookRepository.save(book);
        }
        return null;
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}