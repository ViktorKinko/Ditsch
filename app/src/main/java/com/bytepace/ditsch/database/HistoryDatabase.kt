package com.bytepace.ditsch.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


/**
 * Created by Viktor on 20.03.2018.
 */
@Database(entities = [SearchRequest::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun searchRequestDao(): SearchRequestDao

    companion object {
        private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase {
            if (INSTANCE == null) {
                synchronized(Database::class) {
                    INSTANCE = Room.databaseBuilder(context, HistoryDatabase::class.java, "history.db").allowMainThreadQueries().build()
                }
            }
            return INSTANCE as HistoryDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    fun loadHistory(): ArrayList<String> {
        val list = ArrayList<String>()
        val history = searchRequestDao().getAll()
        for (h: SearchRequest in history) {
            val c: String? = h.content
            c ?: continue
            list.add(c)
        }
        return list
    }

    fun insertHistory(text: String) {
        val searchRequest = SearchRequest()
        searchRequest.content = text
        findAndRemove(text)
        searchRequestDao().insert(searchRequest)
    }

    fun findAndRemove(text: String) {
        val sameStuff = searchRequestDao().getSame(text)
        if (sameStuff.isNotEmpty()) {
            for (sameRequest in sameStuff) {
                searchRequestDao().remove(sameRequest)
            }
        }
    }
}
