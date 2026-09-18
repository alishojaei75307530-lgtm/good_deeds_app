package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalysisDao {

    @Query("SELECT * FROM analysis_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<AnalysisEntity>>

    @Query("SELECT * FROM analysis_history WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): AnalysisEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AnalysisEntity): Long

    @Query("DELETE FROM analysis_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM analysis_history")
    suspend fun clearAll()
}
