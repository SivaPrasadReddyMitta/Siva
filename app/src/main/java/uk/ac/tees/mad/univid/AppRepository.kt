package uk.ac.tees.mad.univid

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.univid.data.ItemDao
import uk.ac.tees.mad.univid.models.local.Items
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val itemDao : ItemDao
) {

    suspend fun addData(item : List<Items>){
        itemDao.insert(item)
    }

    fun getAllFromDB(): Flow<List<Items>> {
        val response = itemDao.getAllItems()
        return response
    }

    suspend fun delete(){
        itemDao.deleteAll()
    }

}