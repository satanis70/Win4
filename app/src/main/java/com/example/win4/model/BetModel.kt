package com.example.win4.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bet_table")
data class BetModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "rollNo")
    val roll: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "odd")
    val odd: String,
    @ColumnInfo(name = "amount")
    val amount: String,
    @ColumnInfo(name= "status")
    val status: String,
    @ColumnInfo(name = "capital")
    val capital: String
)
