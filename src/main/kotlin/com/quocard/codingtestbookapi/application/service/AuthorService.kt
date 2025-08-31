package com.quocard.codingtestbookapi.application.service

import com.quocard.codingtestbookapi.domain.model.Author
import com.quocard.codingtestbookapi.domain.repository.AuthorRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    /**
     * 新規著者登録
     */
    fun createAuthor(name: String, birthDate: LocalDate): Author {
        val author = Author(id = null, name = name, birthDate = birthDate)
        return authorRepository.save(author)
    }

    /**
     * 著者の取得
     */
    fun getAuthorById(id: Long): Author? {
        return authorRepository.findById(id)
    }
}

