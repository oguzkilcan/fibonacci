package io.oguz.fibonacci.domain

import io.oguz.fibonacci.config.LogExecutionTime
import io.oguz.fibonacci.domain.impl.*
import io.oguz.fibonacci.domain.models.FibonacciSequenceModel
import org.springframework.stereotype.Service

@Service
class FibonacciService {
    private var sharedCache = FibonacciSequenceSharedCacheFactory().newCache(name = "Shared Cache", warmUp = true)

    @LogExecutionTime
    fun calculateFibonacciSequence(number: Int, pageSize: Int, pageCount: Int): FibonacciSequenceModel =
        IterativeIntermittentSharedCacheImpl(sharedCache).calculate(number, pageCount, pageSize)
}

