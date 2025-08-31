package com.quocard.codingtestbookapi.domain.repository

import com.quocard.codingtestbookapi.domain.model.Book

/**
 * 書籍を保存・検索するリポジトリ
 */
interface BookRepository  {
    fun save(book: Book, authorIds: List<Long> = emptyList()): Book
    fun findByAuthorId(authorId: Long): List<Book>
}