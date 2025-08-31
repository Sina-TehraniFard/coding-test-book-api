package com.quocard.codingtestbookapi.application.service

import com.quocard.codingtestbookapi.domain.model.Author
import com.quocard.codingtestbookapi.domain.repository.AuthorRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate


class AuthorServiceTest : FunSpec({

    val repository = mockk<AuthorRepository>()
    val service = AuthorService(repository)

    test("新規著者登録できる") {
        // Given: 著者リポジトリに新規著者が保存される想定
        val input = Author(id = null, name = "山田太郎", birthDate = LocalDate.of(1980, 1, 1))
        val saved = input.copy(id = 1L)

        every { repository.save(input) } returns saved

        // When: サービス経由で新規著者を作成
        val result = service.createAuthor("山田太郎", LocalDate.of(1980, 1, 1))

        // Then: 保存された著者のIDと名前が正しく返る
        result.id shouldBe 1L
        result.name shouldBe "山田太郎"
    }

    test("存在する著者IDを取得したら著者が返る") {
        // Given: リポジトリが著者を返す想定
        val author = Author(id = 1L, name = "山田太郎", birthDate = LocalDate.of(1980, 1, 1))
        every { repository.findById(1L) } returns author

        // When: サービス経由で著者を取得
        val result = service.getAuthorById(1L)

        // Then: 正しい著者情報が返る
        result shouldBe author
    }

    test("存在しない著者IDを取得したら null を返す") {
        // Given
        every { repository.findById(999L) } returns null

        // When
        val result = service.getAuthorById(999L)

        // Then
        result shouldBe null
    }
})