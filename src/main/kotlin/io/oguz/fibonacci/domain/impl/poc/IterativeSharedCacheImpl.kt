package io.oguz.fibonacci.domain.impl.poc

import io.oguz.fibonacci.domain.FibonacciSequenceCalculator
import io.oguz.fibonacci.domain.models.FibonacciSequenceModel
import java.math.BigInteger
import java.util.concurrent.ConcurrentSkipListMap

/**
 * Contains implementation of [FibonacciSequenceCalculator] utilizing [ConcurrentSkipListMap] as shared in-memory cache.
 *
 * WARNING: This implementation is prone to crashing the Application with [OutOfMemoryError] because all the available memory is
 * consumed by the cache.
 *
 */
class IterativeSharedCacheImpl(
    private var sharedCache: ConcurrentSkipListMap<Int, BigInteger>
): FibonacciSequenceCalculator {

    override fun calculate(number: Int, pageCount: Int, pageSize: Int): FibonacciSequenceModel {
        validate(number, pageCount, pageSize)
        kickStartLocalCache(sharedCache)
        val (pageStart, pageEnd) = getPageRange(number, pageCount, pageSize)
        calculateInternal(pageEnd)
        return slicePage(ConcurrentSkipListMap(sharedCache), pageStart, pageEnd)
    }

    private fun calculateInternal(number: Int) {
        if (number > sharedCache.count() - 1) {
            for (i in sharedCache.count() - 1..number) {
                sharedCache.computeIfAbsent(i) {
                    sharedCache.getValue(i - 1).add(
                        sharedCache.getValue(i - 2)
                    )
                }
            }
        }
    }
}
