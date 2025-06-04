package com.example.reflect.domain.model

data class ReportUserModel(
    val id: Int,
    val reason: String,
    val createdAt: String,
    val isResolved: Boolean,
    val isAccepted: Boolean?,
    val reporter: Int,
    val state: Int
)