package com.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val id: String? = null,
    val name: String,
    val age: Int
)