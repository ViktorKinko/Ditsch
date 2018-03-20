package com.bytepace.ditsch.database

import android.arch.persistence.room.*

/**
 * Created by Viktor on 20.03.2018.
 */
@Entity
class SearchRequest {
    @PrimaryKey
    var uid: Int? = null

    @ColumnInfo(name = "content")
    var content: String? = null
}

@Dao
interface SearchRequestDao {
    @Query("SELECT * FROM SearchRequest")
    fun getAll(): List<SearchRequest>

    @Query("SELECT * FROM SearchRequest WHERE content = :text")
    fun getSame(text: String): List<SearchRequest>

    @Insert
    fun insert(vararg searchRequest: SearchRequest)

    @Delete
    fun remove(vararg searchRequest: SearchRequest)
}