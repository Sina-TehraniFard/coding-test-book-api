package com.quocard.codingtestbookapi.presentation.controller

import com.quocard.codingtestbookapi.application.service.BookService
import com.quocard.codingtestbookapi.domain.model.Book
import com.quocard.codingtestbookapi.domain.model.PublicationStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping
class BookController(
    private val bookService: BookService
) {
    /** POST /books : 新規書籍登録 */
    @PostMapping("/books")
    fun createBook(@RequestBody request: CreateBookRequest): ResponseEntity<Book> {
        val book = bookService.createBook(
            title = request.title,
            price = request.price,
            status = request.status,
            authorIds = request.authorIds
        )
        return ResponseEntity.ok(book)
    }

    /** GET /authors/{id}/books : 著者に紐づく書籍一覧取得 */
    @GetMapping("/authors/{id}/books")
    fun getBooksByAuthor(@PathVariable id: Long): ResponseEntity<List<Book>> {
        val books = bookService.getBookByAuthor(id)
        return ResponseEntity.ok(books)
    }

    data class CreateBookRequest(
        val title: String,
        val price: BigDecimal,
        val status: PublicationStatus,
        val authorIds: List<Long>
    )
}