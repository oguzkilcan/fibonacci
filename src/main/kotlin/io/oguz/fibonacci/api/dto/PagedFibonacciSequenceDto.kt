package io.oguz.fibonacci.api.dto

import java.math.BigInteger
import kotlin.math.ceil

data class PagedFibonacciSequenceDto(
    val pageData: PageData,
    val data: List<BigInteger>
) {
    companion object Factory {
        fun create(data: List<BigInteger>, number: Int, pageSize: Int, pageCount: Int): PagedFibonacciSequenceDto =
            PagedFibonacciSequenceDto(
                pageData = PageData(count = data.size, pageCount = pageCount, pageSize = pageSize, totalPages = ceil(number/pageSize.toDouble()).toInt()),
                data = data
            )
    }
}
