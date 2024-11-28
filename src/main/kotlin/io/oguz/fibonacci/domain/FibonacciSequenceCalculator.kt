package io.oguz.fibonacci.domain

import io.oguz.fibonacci.domain.exceptions.EntityOutOfAllowedRangeException
import io.oguz.fibonacci.domain.models.FibonacciSequenceModel
import java.math.BigInteger
import java.util.concurrent.ConcurrentSkipListMap
import kotlin.math.ceil

interface FibonacciSequenceCalculator {

    companion object {

        const val PAGE_SIZE_MIN = 5
        const val PAGE_SIZE_MAX = 200
        const val PAGE_COUNT_MIN = 1

        /**
         * Minimum allowed number for the size of the requested Fibonacci sequence
         */
        const val MIN_NUMBER = 0

        /**
         * Maximum allowed number for the size of the requested Fibonacci sequence
         */
        const val MAX_NUMBER = 1000000
    }

    /**
     * Calculates and returns [FibonacciSequenceModel] containing a Fibonacci sequence where contents are determined
     * based on [number], [pageCount] and [pageSize].
     *
     * @param number Specifies how many Fibonacci numbers might need to be calculated in total
     * @param pageCount Specifies according to pagination which page will be returned
     * @param pageSize Specifies the size of the returned data
     */
    fun calculate(number: Int, pageCount: Int, pageSize: Int): FibonacciSequenceModel


    /**
     * Calculates and extracts a page slice from a [sequence] of Fibonacci numbers using [pageStart] and [pageEnd] to construct [FibonacciSequenceModel]
     */
    fun slicePage(
        sequence: ConcurrentSkipListMap<Int, BigInteger>,
        pageStart: Int,
        pageEnd: Int
    ): FibonacciSequenceModel = FibonacciSequenceModel(
        data = sequence.subMap(pageStart, true, pageEnd, false).values.toList()
    )


    fun kickStartLocalCache(sequence: ConcurrentSkipListMap<Int, BigInteger>): ConcurrentSkipListMap<Int, BigInteger> =
        sequence.apply {
            this[0] = BigInteger.ZERO
            this[1] = BigInteger.ONE
            this[2] = BigInteger.ONE
        }

    fun getPageRange(number: Int, pageCount: Int, pageSize: Int): Pair<Int, Int> =
        ((pageCount - 1) * pageSize to minOf(number, pageSize * pageCount))

    fun validate(number: Int, pageCount: Int, pageSize: Int) {
        val (pageStart, pageEnd) = getPageRange(number, pageCount, pageSize)
        if (number < MIN_NUMBER || number > MAX_NUMBER) throw EntityOutOfAllowedRangeException("number", number, MIN_NUMBER, MAX_NUMBER)
        if (pageSize < PAGE_SIZE_MIN || pageSize > PAGE_SIZE_MAX) throw EntityOutOfAllowedRangeException("pageSize", number, PAGE_SIZE_MIN, PAGE_SIZE_MAX)
        if (pageCount < PAGE_COUNT_MIN || pageStart > pageEnd) throw EntityOutOfAllowedRangeException("pageCount", pageCount, PAGE_COUNT_MIN, ceil(number/pageSize.toDouble()).toInt())
    }
}