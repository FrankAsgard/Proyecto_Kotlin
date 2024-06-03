package com.example.shopnow.models

import com.google.firebase.firestore.PropertyName

enum class Role(val role: String) {
    SUPER_ADMIN("super_admin"),
    CLIENT("client");
}


data class User(
    @get:PropertyName("userId")
    @set:PropertyName("userId")
    var userId: String? = null,

    @get:PropertyName("username")
    @set:PropertyName("username")
    var username: String? = null,

    @get:PropertyName("password")
    @set:PropertyName("password")
    var password: String? = null,

    @get:PropertyName("email")
    @set:PropertyName("email")
    var email: String? = null,

    @get:PropertyName("fullName")
    @set:PropertyName("fullName")
    var fullName: String? = null,

    @get:PropertyName("role")
    @set:PropertyName("role")
    var role: Role? = null
) {
    constructor() : this(null, null, null, null, null, null)
}
