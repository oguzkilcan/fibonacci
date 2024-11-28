package io.oguz.fibonacci.domain.impl.poc

import io.oguz.fibonacci.domain.FibonacciSequenceCalculator
import io.oguz.fibonacci.domain.models.FibonacciSequenceModel
import java.math.BigInteger
import java.util.concurrent.ConcurrentSkipListMap

/**
 * Contains implementation of [FibonacciSequenceCalculator] utilizing [ConcurrentSkipListMap] as in-memory cache.
 *
 * WARNING: This implementation is prone to crashing the Application with [OutOfMemoryError] because all the available memory is
 * consumed by the cache.
 *
 */
class IterativeBasicCacheImpl : FibonacciSequenceCalculator {
    private var localCache: ConcurrentSkipListMap<Int, BigInteger> = ConcurrentSkipListMap()

    override fun calculate(number: Int, pageCount: Int, pageSize: Int): FibonacciSequenceModel {
        validate(number, pageCount, pageSize)
        kickStartLocalCache(localCache)
        val (pageStart, pageEnd) = getPageRange(number, pageCount, pageSize)
        calculateInternal(pageEnd)
        return slicePage(localCache, pageStart, pageEnd)
    }

    private fun calculateInternal(number: Int) {
        val startPoint = localCache.size
        for (i in startPoint..number) {
            localCache.computeIfAbsent(i) {
                localCache.getValue(i - 1).add(
                    localCache.getValue(i - 2)
                )
            }
        }
    }
}
