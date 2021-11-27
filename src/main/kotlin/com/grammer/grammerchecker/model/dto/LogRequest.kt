package com.grammer.grammerchecker.model.dto

import lombok.Data

@Data
data class LogRequest(
    val errorText: String,
    val fixedText: String,
    val fixedCount: Int = 0,
    val ip: String = ""
)