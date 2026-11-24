package com.tunepacer.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PastRunDao {
    @Query("SELECT * FROM past_runs ORDER BY date DESC")
    fun observeAll(): Flow<List<PastRunEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pastRun: PastRunEntity): Long

    @Delete
    suspend fun delete(pastRun: PastRunEntity)
}
