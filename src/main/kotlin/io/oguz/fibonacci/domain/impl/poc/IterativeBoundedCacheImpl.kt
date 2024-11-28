package io.oguz.fibonacci.domain.impl.poc

import io.oguz.fibonacci.domain.FibonacciSequenceCalculator
import io.oguz.fibonacci.domain.models.FibonacciSequenceModel
import java.math.BigInteger
import java.util.concurrent.ConcurrentSkipListMap

/**
 * Contains implementation of [FibonacciSequenceCalculator] utilizing [ConcurrentSkipListMap] as bounded in-memory cache.
 *
 * WARNING: This implementation is prone to performance problems when calculating a Fibonacci sequence where requested number
 * is approaching the allowed maximum.
 *
 */
class IterativeBoundedCacheImpl : FibonacciSequenceCalculator {
    private var localCache: ConcurrentSkipListMap<Int, BigInteger> = ConcurrentSkipListMap()

    companion object {
        const val MAX_CACHE_SIZE = 2000
    }

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
            handleCacheBoundary()
            localCache.computeIfAbsent(i) {
                localCache.getValue(i - 1).add(
                    localCache.getValue(i - 2)
                )
            }
        }
    }

    private fun handleCacheBoundary() {
        if (localCache.size > MAX_CACHE_SIZE * 2) {
            do {
                localCache.pollFirstEntry()
            } while (localCache.size > MAX_CACHE_SIZE)
        }
    }
}
