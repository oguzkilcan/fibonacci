package io.oguz.fibonacci.api

import io.oguz.fibonacci.api.dto.PagedFibonacciSequenceDto
import io.oguz.fibonacci.domain.FibonacciService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("fibonacci")
class FibonacciController(
    private val fibonacciService: FibonacciService
) {

    companion object {
        const val PAGE_SIZE_DEFAULT = 20
        const val PAGE_COUNT_DEFAULT = 1
    }


    @Operation(summary = "Returns {number} Fibonacci numbers as an ascending sequence factoring in Pagination")
    @GetMapping("{number}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFibonacciSequence(
        @PathVariable number: Int,
        @RequestParam(defaultValue = PAGE_SIZE_DEFAULT.toString()) pageSize: Int,
        @RequestParam(defaultValue = PAGE_COUNT_DEFAULT.toString()) pageCount: Int,
    ): ResponseEntity<PagedFibonacciSequenceDto> =
        fibonacciService.calculateFibonacciSequence(number, pageSize, pageCount)
            .let {
                ResponseEntity.ok(
                    PagedFibonacciSequenceDto.create(it.data, number, pageSize, pageCount)
                )
            }

}
