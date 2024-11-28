package io.oguz.fibonacci.domain

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.util.concurrent.ConcurrentSkipListMap

class FibonacciSequenceSharedCacheFactory {
    private val logger = KotlinLogging.logger {}

    companion object {
        const val INTERMITTENT_CACHE_SKIP_DISTANCE = 200
    }

    fun newCache(
        name: String,
        warmUp: Boolean = false,
    ): ConcurrentSkipListMap<Int, BigInteger> = ConcurrentSkipListMap<Int, BigInteger>()
        .also { cache ->
            if (warmUp) {
                CoroutineScope(Dispatchers.Default).launch { warmUp(name, cache) }
            }
        }

    /**
     * Calculate Fibonacci sequence until [FibonacciSequenceCalculator.MAX_NUMBER] to help warming-up created cache in a
     * memory efficient way.
     *
     * Continuously repeats below action until the end:
     *  - Cache two consecutive numbers
     *  - Skip [INTERMITTENT_CACHE_SKIP_DISTANCE] numbers
     *
     *  This will create a cache filled with intermittently distributed Fibonacci number pairs
     */
    private fun warmUp(name: String, cache: ConcurrentSkipListMap<Int, BigInteger>) {
        val skipPoints: Int = FibonacciSequenceCalculator.MAX_NUMBER/ INTERMITTENT_CACHE_SKIP_DISTANCE
        logger.debug { "Warming-up shared intermittent cache $name for $skipPoints skip points is starting" }
        var skipUntil: Int = INTERMITTENT_CACHE_SKIP_DISTANCE

        var oneDown: BigInteger = BigInteger.ONE
        var current: BigInteger = BigInteger.ONE
        var temp: BigInteger

        for (i in 3..FibonacciSequenceCalculator.MAX_NUMBER) {
            temp = current.add(oneDown)
            oneDown = current
            current = temp

            if (i > skipUntil) {
                cache.putAll(
                    listOf(
                        i-1 to oneDown,
                        i to current,
                    )
                )
                skipUntil += INTERMITTENT_CACHE_SKIP_DISTANCE
            }
        }
        logger.debug { "Warming-up shared intermittent cache $name for $skipPoints skip points is completed" }
    }
}