package uk.ac.tees.mad.univid.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.univid.models.local.Items

@Dao
interface ItemDao {

    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems() : Flow<List<Items>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item : List<Items>)

    @Delete
    suspend fun delete(item : Items)

    @Query("DELETE FROM items")
    suspend fun deleteAll()
}