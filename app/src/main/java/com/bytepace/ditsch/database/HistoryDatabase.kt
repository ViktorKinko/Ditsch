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
                    INSTANCE = Room.databaseBuilder(context.applicationContext, HistoryDatabase::class.java, "history.db").allowMainThreadQueries().build()
                }
            }
            return INSTANCE as HistoryDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
