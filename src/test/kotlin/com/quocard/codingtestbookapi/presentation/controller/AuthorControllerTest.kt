package com.quocard.codingtestbookapi.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.quocard.codingtestbookapi.application.service.AuthorService
import com.quocard.codingtestbookapi.domain.model.Author
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
import java.time.LocalDate

@WebMvcTest(AuthorController::class)
@SpringJUnitConfig
class AuthorControllerTest : FunSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var authorService: AuthorService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    init {
        test("POST /authors - 正しいリクエストを送信した場合、201(CREATED)が返り、レスポンスボディに著者情報が含まれる") {
            // Given: 著者作成リクエストとサービスの戻り値を準備
            val request = AuthorController.CreateAuthorRequest("山田太郎", LocalDate.of(1980, 1, 1))
            val savedAuthor = Author(id = 1L, name = "山田太郎", birthDate = LocalDate.of(1980, 1, 1))
            
            every { authorService.createAuthor("山田太郎", LocalDate.of(1980, 1, 1)) } returns savedAuthor

            // When & Then: POST リクエストを送信して結果を検証
            mockMvc.perform(
                post("/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("山田太郎"))
                .andExpect(jsonPath("$.birthDate").value("1980-01-01"))
        }

        test("GET /authors/{id} - 存在するIDを指定した場合、200(OK)が返り、著者情報が返る") {
            // Given: 存在する著者データをサービスが返すようにモック化
            val author = Author(id = 1L, name = "田中花子", birthDate = LocalDate.of(1990, 5, 15))
            
            every { authorService.getAuthorById(1L) } returns author

            // When & Then: GET リクエストを送信して結果を検証
            mockMvc.perform(get("/authors/1"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("田中花子"))
                .andExpect(jsonPath("$.birthDate").value("1990-05-15"))
        }

        test("GET /authors/{id} - 存在しないIDを指定した場合、404(NOT FOUND)が返る") {
            // Given: 存在しないIDに対してnullを返すようにモック化
            every { authorService.getAuthorById(999L) } returns null

            // When & Then: GET リクエストを送信して404が返ることを検証
            mockMvc.perform(get("/authors/999"))
                .andExpect(status().isNotFound)
        }
    }
}