package io.oguz.fibonacci.domain

import io.oguz.fibonacci.api.FibonacciControllerPoc
import io.oguz.fibonacci.config.LogExecutionTime
import io.oguz.fibonacci.domain.impl.poc.*
import io.oguz.fibonacci.domain.models.FibonacciSequenceModel
import org.springframework.stereotype.Service

@Service
class FibonacciServicePoc {
    private var sharedCache = FibonacciSequenceSharedCacheFactory().newCache(name = "Shared Cache")

    @LogExecutionTime
    fun calculateFibonacciSequence(number: Int, pageSize: Int, pageCount: Int, cacheOption: FibonacciControllerPoc.CacheOptionPoc): FibonacciSequenceModel =
        when (cacheOption) {
            FibonacciControllerPoc.CacheOptionPoc.RECURSIVE_BASIC_CACHE -> RecursiveBasicCacheImpl().calculate(number, pageCount, pageSize)
            FibonacciControllerPoc.CacheOptionPoc.ITERATIVE_BASIC_CACHE -> IterativeBasicCacheImpl().calculate(number, pageCount, pageSize)
            FibonacciControllerPoc.CacheOptionPoc.ITERATIVE_BOUNDED_CACHE -> IterativeBoundedCacheImpl().calculate(number, pageCount, pageSize)
            FibonacciControllerPoc.CacheOptionPoc.ITERATIVE_UNLIMITED_SHARED_CACHE -> IterativeSharedCacheImpl(sharedCache).calculate(number, pageCount, pageSize)
        }
}
