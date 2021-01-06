package com.example.todolistworkflow.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TodosItem(
    @Json(name = "completed")
    val completed: Boolean,
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "userId")
    val userId: Int
)