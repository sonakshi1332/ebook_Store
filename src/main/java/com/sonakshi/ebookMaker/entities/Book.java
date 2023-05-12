package com.sonakshi.ebookMaker.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title must be less than 256 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(max = 255, message = "Author must be less than 256 characters")
    private String author;

    @NotBlank(message = "ISBN is mandatory")
    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
    private String isbn;

    @NotNull(message = "Publish date is mandatory")
    private LocalDate publishDate;

    @NotNull(message = "Price is mandatory")
    @Min(value = 0, message = "Price must be equal or greater than 0")
    private BigDecimal price;

    @Size(max = 1000, message = "Description must be less than 1001 characters")
    private String description;
    
    @Size(max = 255, message = "Template must be less than 255 characters")
    private String template;
    
    @Size(max = 256, message = "Template must be less than 256 characters")
    private String titlePageDesign;

    @Enumerated(EnumType.STRING)
    private EbookFormat ebookFormat;
    
    public enum EbookFormat {
        PDF,
        EPUB,
        MOBI
    }

	
    // Other fields like coverImageUrl, pageCount, etc., can be added here

}