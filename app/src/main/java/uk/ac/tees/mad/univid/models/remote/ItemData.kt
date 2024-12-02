package uk.ac.tees.mad.univid.models.remote

data class ItemData (
    val id : String = "",
    val title : String,
    val image : String,
    val description : String,
    val location : String
)