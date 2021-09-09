package com.example.asyncs3.web

import java.util.*

data class UploadResult(
    val key: String,
    val uuid: UUID,
    val url: String,
)