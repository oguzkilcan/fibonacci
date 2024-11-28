package io.oguz.fibonacci.api

import io.oguz.fibonacci.api.dto.PagedFibonacciSequenceDto
import io.oguz.fibonacci.domain.FibonacciServicePoc
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("fibonacci-poc")
class FibonacciControllerPoc(
    private val fibonacciServicePoc: FibonacciServicePoc
) {

    companion object {
        const val PAGE_SIZE_DEFAULT = 20
        const val PAGE_COUNT_DEFAULT = 1
    }

    @GetMapping("{number}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFibonacciSequencePoc(
        @PathVariable number: Int,
        @RequestParam(defaultValue = PAGE_SIZE_DEFAULT.toString()) pageSize: Int,
        @RequestParam(defaultValue = PAGE_COUNT_DEFAULT.toString()) pageCount: Int,
        @RequestParam option: CacheOptionPoc,
    ): ResponseEntity<PagedFibonacciSequenceDto> =
        fibonacciServicePoc.calculateFibonacciSequence(number, pageSize, pageCount, option)
            .let {
                ResponseEntity.ok(
                    PagedFibonacciSequenceDto.create(it.data, number, pageSize, pageCount)
                )
            }

    enum class CacheOptionPoc {
        RECURSIVE_BASIC_CACHE,
        ITERATIVE_BASIC_CACHE,
        ITERATIVE_BOUNDED_CACHE,
        ITERATIVE_UNLIMITED_SHARED_CACHE
        ;
    }
}
