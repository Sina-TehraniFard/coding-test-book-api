package com.quocard.codingtestbookapi.presentation.controller

import com.quocard.codingtestbookapi.application.service.AuthorService
import com.quocard.codingtestbookapi.domain.model.Author
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate


@RestController
@RequestMapping("/authors")
class AuthorController(private val authorService: AuthorService) {

    /** POST /authors : 新規著者登録 */
    @PostMapping
    fun createAuthor(@RequestBody request: CreateAuthorRequest): ResponseEntity<Author> {
        val author = authorService.createAuthor(request.name, request.birthDate)
        return ResponseEntity(author, HttpStatus.CREATED)
    }

    /** GET /authors/{id} : 著者取得 */
    @GetMapping("/{id}")
    fun getAuthorById(@PathVariable id: Long): ResponseEntity<Author> {
        val author = authorService.getAuthorById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity(author, HttpStatus.OK)
    }

    class CreateAuthorRequest(
        val name: String,
        val birthDate: LocalDate
    )
}