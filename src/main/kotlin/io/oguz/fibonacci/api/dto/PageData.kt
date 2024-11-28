package io.oguz.fibonacci.api.dto

data class PageData(
    val count: Int,
    val pageCount: Int,
    val totalPages: Int,
    val pageSize: Int,
)
