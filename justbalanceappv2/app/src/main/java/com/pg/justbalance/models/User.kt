package com.pg.justbalance.models

import com.google.firebase.firestore.PropertyName

data class User (
    @PropertyName("email")
    var email: String? = null,

    @PropertyName("document_id")
    var userID: String
)