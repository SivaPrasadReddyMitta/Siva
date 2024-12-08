package uk.ac.tees.mad.univid.models.remote

data class UserData(
    val uid: String? = null,
    val name: String,
    val profileimage: String? = null,
    val email: String,
    val phonenumber: String,
    val password: String,
    val location: String? = null
) {
    // No-argument constructor for Firestore
    constructor() : this(null, "", null, "", "", "", null)
}