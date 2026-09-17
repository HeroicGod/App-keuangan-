package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Pocket::class, Transaction::class, FinancialGoal::class, Schedule::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun financeDao(): FinanceDao
}
