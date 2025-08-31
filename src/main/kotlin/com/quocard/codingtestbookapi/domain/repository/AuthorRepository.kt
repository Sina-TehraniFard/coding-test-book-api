package com.quocard.codingtestbookapi.domain.repository

import com.quocard.codingtestbookapi.domain.model.Author

/**
 * 著者を保存・取得するメソッド
 */
interface AuthorRepository {
    fun save(author: Author): Author
    fun findById(id: Long): Author?
}