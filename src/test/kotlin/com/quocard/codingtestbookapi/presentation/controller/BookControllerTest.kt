package com.quocard.codingtestbookapi.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.quocard.codingtestbookapi.application.service.BookService
import com.quocard.codingtestbookapi.domain.model.Book
import com.quocard.codingtestbookapi.domain.model.PublicationStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

@WebMvcTest(BookController::class)
@SpringJUnitConfig
class BookControllerTest : FunSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var bookService: BookService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    init {
        test("POST /books - 正しいリクエストを送信した場合、200(OK)が返り、レスポンスボディに書籍情報が含まれる") {
            // Given: 書籍作成リクエストとサービスの戻り値を準備
            val request = BookController.CreateBookRequest(
                title = "Kotlin入門",
                price = BigDecimal("2980"),
                status = PublicationStatus.PUBLISHED,
                authorIds = listOf(1L)
            )
            val savedBook = Book(
                id = 1L,
                title = "Kotlin入門",
                price = BigDecimal("2980"),
                status = PublicationStatus.PUBLISHED
            )

            every { bookService.createBook("Kotlin入門", BigDecimal("2980"), PublicationStatus.PUBLISHED, listOf(1L)) } returns savedBook

            // When & Then: POST リクエストを送信して結果を検証
            mockMvc.perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Kotlin入門"))
                .andExpect(jsonPath("$.price").value(2980))
                .andExpect(jsonPath("$.status").value("PUBLISHED"))
        }

        test("GET /authors/{id}/books - 存在する著者IDを指定した場合、200(OK)が返り、書籍一覧が返る") {
            // Given: 著者に紐づく書籍一覧をサービスが返すようにモック化
            val books = listOf(
                Book(id = 1L, title = "Spring Boot入門", price = BigDecimal("3200"), status = PublicationStatus.PUBLISHED),
                Book(id = 2L, title = "Kotlin実践", price = BigDecimal("3500"), status = PublicationStatus.DRAFT)
            )

            every { bookService.getBookByAuthor(1L) } returns books

            // When & Then: GET リクエストを送信して結果を検証
            mockMvc.perform(get("/authors/1/books"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$").isArray)
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Spring Boot入門"))
                .andExpect(jsonPath("$[0].price").value(3200))
                .andExpect(jsonPath("$[0].status").value("PUBLISHED"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Kotlin実践"))
                .andExpect(jsonPath("$[1].price").value(3500))
                .andExpect(jsonPath("$[1].status").value("DRAFT"))
        }

        test("GET /authors/{id}/books - 書籍が存在しない著者IDを指定した場合、200(OK)が返り、空の配列が返る") {
            // Given: 書籍が存在しない著者に対して空のリストを返すようにモック化
            every { bookService.getBookByAuthor(999L) } returns emptyList()

            // When & Then: GET リクエストを送信して空の配列が返ることを検証
            mockMvc.perform(get("/authors/999/books"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$").isArray)
                .andExpect(jsonPath("$.length()").value(0))
        }
    }
}