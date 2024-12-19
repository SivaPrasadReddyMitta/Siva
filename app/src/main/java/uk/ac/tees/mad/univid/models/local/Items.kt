package uk.ac.tees.mad.univid.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Items(
    @PrimaryKey(autoGenerate = true) val idNum : Int = 0,
    val id : String,
    val name : String,
    val number : String,
    val title : String,
    val image : String,
    val description : String,
    val location : String
)