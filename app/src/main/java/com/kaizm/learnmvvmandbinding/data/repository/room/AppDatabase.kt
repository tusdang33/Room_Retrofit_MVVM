package com.kaizm.learnmvvmandbinding.data.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kaizm.learnmvvmandbinding.data.model.Cart
import com.kaizm.learnmvvmandbinding.data.service.DatabaseService

@Database(entities = [Cart::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DatabaseService
}