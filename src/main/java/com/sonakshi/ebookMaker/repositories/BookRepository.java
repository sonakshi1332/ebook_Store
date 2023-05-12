package com.sonakshi.ebookMaker.repositories;


import com.sonakshi.ebookMaker.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
