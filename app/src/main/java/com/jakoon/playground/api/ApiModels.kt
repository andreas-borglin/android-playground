package com.jakoon.playground.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Post(val id: Int, val userId: Int, val title: String, val body: String)

@JsonClass(generateAdapter = true)
data class User(
    val id: Int, val name: String, val username: String, val email: String, val address: Address,
    val phone: String, val website: String, val company: Company
)

@JsonClass(generateAdapter = true)
data class Comment(val id: Int, val postId: Int, val name: String, val email: String, val body: String)

data class Geo(val lat: String, val lng: String)

data class Address(val street: String, val suite: String, val city: String, val zipcode: String, val geo: Geo)

data class Company(val name: String, val catchPhrase: String, val bs: String)

