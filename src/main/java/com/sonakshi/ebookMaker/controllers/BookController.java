package com.sonakshi.ebookMaker.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sonakshi.ebookMaker.entities.Book;
import com.sonakshi.ebookMaker.services.BookService;
import com.sonakshi.ebookMaker.services.BookTemplateService;
import com.sonakshi.ebookMaker.services.FileStorageService;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookTemplateService bookTemplateService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestParam("title") String title,
                                            @RequestParam("author") String author,
                                            @RequestParam(value = "isbn", required = false) String isbn,
                                            @RequestParam(value = "file", required = false) MultipartFile file) {
        Book book = new Book(title, author);

        if (isbn == null) {
            String generatedIsbn = bookService.generateIsbn();
            book.setIsbn(generatedIsbn);
        } else {
            book.setIsbn(isbn);
        }

        if (file != null) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            fileStorageService.save(file, fileName);
            book.setCoverImage(fileName);
        }

        bookService.createBook(book);

        return ResponseEntity.ok(book);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book existingBook = bookService.getBookById(id);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setPublisher(book.getPublisher());
        existingBook.setPublishingDate(book.getPublishingDate());
        existingBook.setCoverImage(book.getCoverImage());
        existingBook.setEbookFormat(book.getEbookFormat());
        bookService.updateBook(existingBook);
        return ResponseEntity.ok(existingBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Book existingBook = bookService.getBookById(id);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }
        if (existingBook.getCoverImage() != null) {
            fileStorageService.delete(existingBook.getCoverImage());
        }
        bookService.deleteBook(existingBook);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/ebook")
    public ResponseEntity<Resource> downloadEbook(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book == null || book.getEbookFormat() == null) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = fileStorageService.loadFile(book.getEbookFileName());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<byte[]> getPreview(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] preview = bookTemplateService.applyTemplate(book, "preview");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(preview, headers, HttpStatus.OK);

