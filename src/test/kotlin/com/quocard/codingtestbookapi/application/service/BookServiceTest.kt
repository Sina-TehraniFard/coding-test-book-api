package com.quocard.codingtestbookapi.application.service

import com.quocard.codingtestbookapi.domain.model.Book
import com.quocard.codingtestbookapi.domain.model.PublicationStatus
import com.quocard.codingtestbookapi.domain.repository.BookRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal

class BookServiceTest : FunSpec({

    val repository = mockk<BookRepository>()
    val service = BookService(repository)

    test("存在する著者IDに紐づく書籍一覧を返す") {
        // Given: 著者1に紐づく書籍が存在する
        val books = listOf(
            Book(id = 1L, title = "Kotlin入門", price = BigDecimal(2000), status = PublicationStatus.DRAFT),
            Book(id = 2L, title = "Spring徹底攻略", price = BigDecimal(3000), status = PublicationStatus.PUBLISHED)
        )
        every { repository.findByAuthorId(1L) } returns books

        // When: サービス経由で取得
        val result = service.getBookByAuthor(1L)

        // Then: 書籍一覧が返る
        result shouldBe books
    }

    test("存在しない著者IDに紐づく書籍一覧は空リストを返す") {
        // Given: 著者999に紐づく書籍は存在しない
        every { repository.findByAuthorId(999L) } returns emptyList()

        // When: サービス経由で取得
        val result = service.getBookByAuthor(999L)

        // Then: 空リストが返る
        result.shouldBeEmpty()
    }
    test("createBookで新しい書籍を作成できる") {
        // Given: id=nullのBookを作成し、repository.saveがid=1LのBookを返す
        val book = Book(id = null, title = "Kotlin入門", price = BigDecimal(2000), status = PublicationStatus.DRAFT)
        val savedBook = book.copy(id = 1L)
        every { repository.save(book, listOf(1L)) } returns savedBook

        // When: サービス経由で作成
        val result = service.createBook("Kotlin入門", BigDecimal(2000), PublicationStatus.DRAFT, listOf(1L))

        // Then: 戻り値のBookの内容を検証
        result.id shouldBe 1L
        result.title shouldBe "Kotlin入門"
        result.price shouldBe BigDecimal(2000)
        result.status shouldBe PublicationStatus.DRAFT
    }
})