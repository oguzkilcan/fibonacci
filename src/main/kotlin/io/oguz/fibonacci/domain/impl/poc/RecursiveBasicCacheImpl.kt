package io.oguz.fibonacci.domain.impl.poc

import io.oguz.fibonacci.domain.FibonacciSequenceCalculator
import io.oguz.fibonacci.domain.models.FibonacciSequenceModel
import java.math.BigInteger
import java.util.concurrent.ConcurrentSkipListMap

/**
 * Contains implementation of [FibonacciSequenceCalculator] utilizing [ConcurrentSkipListMap] as in-memory cache.
 *
 * WARNING: This implementation is prone to failing with [StackOverflowError] because all the available memory in the stack
 * consumed by recursive nature of the algorithm.
 *
 */
class RecursiveBasicCacheImpl: FibonacciSequenceCalculator {
    private var localCache: ConcurrentSkipListMap<Int, BigInteger> = ConcurrentSkipListMap()

    override fun calculate(number: Int, pageCount: Int, pageSize: Int): FibonacciSequenceModel {
        validate(number, pageCount, pageSize)
        kickStartLocalCache(localCache)
        val (pageStart, pageEnd) = getPageRange(number, pageCount, pageSize)
        calculateInternal(pageEnd)
        return slicePage(localCache, pageStart, pageEnd)
    }

    private fun calculateInternal(number: Int): BigInteger =
        when (number) {
            0 -> localCache.getOrPut(number) { BigInteger.ZERO }
            1 -> localCache.getOrPut(number) { BigInteger.ONE }
            else -> localCache.getOrPut(number - 1) { calculateInternal(number - 1) }.add(
                localCache.getOrPut(number - 2) { calculateInternal(number - 2) }
            )
        }
}
