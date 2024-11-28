package io.oguz.fibonacci.domain.impl

import io.oguz.fibonacci.domain.FibonacciSequenceCalculator
import io.oguz.fibonacci.domain.models.FibonacciSequenceModel
import java.math.BigInteger
import java.util.concurrent.ConcurrentSkipListMap

/**
 * Contains implementation of [FibonacciSequenceCalculator] utilizing [ConcurrentSkipListMap] as bounded in-memory cache assisted
 * by a pre-warmed intermittent shared cache.
 *
 */
class IterativeIntermittentSharedCacheImpl(
    private val sharedCache: ConcurrentSkipListMap<Int, BigInteger>
) : FibonacciSequenceCalculator {

    private var localCache: ConcurrentSkipListMap<Int, BigInteger> = ConcurrentSkipListMap()

    companion object {
        const val LOCAL_CACHE_BOUNDARY_SIZE = 2000
    }

    override fun calculate(number: Int, pageCount: Int, pageSize: Int): FibonacciSequenceModel {
        validate(number, pageCount, pageSize)
        kickStartLocalCache(localCache)
        val (pageStart, pageEnd) = getPageRange(number, pageCount, pageSize)
        calculateInternal(pageStart, pageEnd)
        return slicePage(localCache, pageStart, pageEnd)
    }

    private fun calculateInternal(pageStart: Int, pageEnd: Int) {
        val startPoint = enrichLocalCacheUsingSharedCache(pageStart)

        for (i in startPoint..pageEnd) {
            handleCacheBoundary()
            localCache.computeIfAbsent(i) {
                localCache.getValue(i - 1).add(
                    localCache.getValue(i - 2)
                )
            }
        }
    }

    // Find previous skip point if possible and populate local cache
    private fun enrichLocalCacheUsingSharedCache(pageStart: Int): Int {
        val floorKey = sharedCache.floorKey(pageStart)
        var startPoint = localCache.size
        if (floorKey != null && sharedCache.containsKey(floorKey) && sharedCache.containsKey(floorKey - 1)) {
            startPoint = floorKey + 1
            localCache.putAll(
                listOf(
                    floorKey to sharedCache.getValue(floorKey),
                    floorKey - 1 to sharedCache.getValue(floorKey - 1)
                )
            )
        } else if (floorKey != null && sharedCache.containsKey(floorKey) && sharedCache.containsKey(floorKey + 1)) {
            startPoint = floorKey + 2
            localCache.putAll(
                listOf(
                    floorKey + 1 to sharedCache.getValue(floorKey + 1),
                    floorKey to sharedCache.getValue(floorKey)
                )
            )
        }
        return startPoint
    }

    private fun handleCacheBoundary() {
        if (localCache.size > LOCAL_CACHE_BOUNDARY_SIZE * 2) {
            do {
                localCache.pollFirstEntry()
            } while (localCache.size > LOCAL_CACHE_BOUNDARY_SIZE)
        }
    }
}
