package com.quocard.codingtestbookapi.application.service

import com.quocard.codingtestbookapi.domain.model.Book
import com.quocard.codingtestbookapi.domain.model.PublicationStatus
import com.quocard.codingtestbookapi.domain.repository.BookRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class BookService(private val bookRepository: BookRepository) {

    /**
     * 新規書籍追加
     */
    fun createBook(title: String, price: BigDecimal, status: PublicationStatus): Book {
        val book = Book(id = null, title = title, price = price, status = status)
        return bookRepository.save(book)
    }

    /**
     * 著者に紐づく書籍一覧
     */
    fun getBookByAuthor(authorId: Long): List<Book> {
        return bookRepository.findByAuthorId(authorId)
    }
}