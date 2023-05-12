package com.sonakshi.ebookMaker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sonakshi.ebookMaker.entities.Book;

@Service
public class BookTemplateService {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookTemplateConfigService bookTemplateConfigService;

    @Autowired
    private BookTemplateLoaderService bookTemplateLoaderService;

    public void applyTemplate(Book book, String templateName) {
        // Load the book template configuration
        BookTemplateConfig templateConfig = bookTemplateConfigService.getTemplateConfig(templateName);

        // Generate title page
        String titlePageHtml = generateTitlePage(book);

        // Replace placeholders in template with actual content
        String bookHtml = templateConfig.getHtml()
                .replace("{titlePage}", titlePageHtml)
                .replace("{author}", book.getAuthor())
                .replace("{publisher}", book.getPublisher())
                .replace("{isbn}", book.getIsbn())
                .replace("{genre}", book.getGenre())
                .replace("{price}", Double.toString(book.getPrice()))
                .replace("{description}", book.getDescription());

        // Update the book's HTML content
        book.setHtml(bookHtml);
        bookService.updateBook(book);
    }

    public String generateTitlePage(Book book) {
        // Load the title page template
        String titlePageTemplate = bookTemplateLoaderService.loadTitlePageTemplate();

        // Replace placeholders in title page template with actual content
        return titlePageTemplate
                .replace("{title}", book.getTitle())
                .replace("{author}", book.getAuthor())
                .replace("{publisher}", book.getPublisher())
                .replace("{isbn}", book.getIsbn());
    }
}
