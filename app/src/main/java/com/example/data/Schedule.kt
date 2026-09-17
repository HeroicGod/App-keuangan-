package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val amount: Double,
    val type: String, // "INCOME", "EXPENSE"
    val nextDate: Long,
    val frequency: String, // "DAILY", "WEEKLY", "MONTHLY"
    val pocketId: Int
)
