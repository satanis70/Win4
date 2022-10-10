package com.example.win4.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.win4.model.BetModel

@Dao
interface BetDao {
    @Query("SELECT * FROM bet_table")
    fun getAll():List<BetModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(betModel: BetModel)

    @Delete()
    suspend fun delete(betModel: BetModel)

    @Query("UPDATE bet_table SET status=:status WHERE rollNo LIKE:roll")
    suspend fun update(status: String, roll: Int)

    @Query("UPDATE bet_table SET capital=:capital WHERE rollNo LIKE:roll")
    suspend fun updateCapital(capital:String, roll: Int)

    @Query("DELETE FROM bet_table")
    suspend fun deleteDb()
}