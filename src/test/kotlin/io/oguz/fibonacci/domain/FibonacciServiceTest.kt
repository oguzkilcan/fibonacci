package io.oguz.fibonacci.domain

import io.oguz.fibonacci.domain.exceptions.EntityOutOfAllowedRangeException
import io.oguz.fibonacci.fibonacciSequenceFirst500
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FibonacciServiceTest{
    private lateinit var service: FibonacciService

    @BeforeEach
    fun setUp() {
        service = FibonacciService()
    }

    @Test
    fun `when fibonacci sequence is requested for a number, pageCount is 1 and pageSize is bigger than number, service should calculate and return the whole sequence`(){
        // given
        val number = 40
        val pageCount = 1
        val pageSize = 50
        val sequence = fibonacciSequenceFirst500.take(number)

        // when
        val calculatedSequence = service.calculateFibonacciSequence(number, pageSize, pageCount)

        // then
        assertEquals(sequence, calculatedSequence.data)
    }

    @Test
    fun `when fibonacci sequence is requested for a number, pageCount is 1 and pageSize is smaller than number, service should calculate and return only requested amount`(){
        // given
        val number = 500
        val pageCount = 1
        val pageSize = 50
        val sequence = fibonacciSequenceFirst500.take(number).drop((pageCount - 1) * pageSize).take(pageSize)

        // when
        val calculatedSequence = service.calculateFibonacciSequence(number, pageSize, pageCount)

        // then
        assertEquals(sequence, calculatedSequence.data)
    }

    @Test
    fun `when fibonacci sequence is requested for a number, pageCount is 2 and there are more than 2 pages, service should calculate and return only requested amount`(){
        // given
        val number = 500
        val pageCount = 2
        val pageSize = 50
        val sequence = fibonacciSequenceFirst500.take(number).drop((pageCount - 1) * pageSize).take(pageSize)

        // when
        val calculatedSequence = service.calculateFibonacciSequence(number, pageSize, pageCount)

        // then
        assertEquals(sequence, calculatedSequence.data)
    }

    @Test
    fun `when fibonacci sequence is requested for a number, pageCount is 2 and there are only 2 pages, service should calculate and return only requested amount`(){
        // given
        val number = 80
        val pageCount = 2
        val pageSize = 50
        val sequence = fibonacciSequenceFirst500.take(number).drop((pageCount - 1) * pageSize).take(pageSize)

        // when
        val calculatedSequence = service.calculateFibonacciSequence(number, pageSize, pageCount)

        // then
        assertEquals(sequence, calculatedSequence.data)
    }

    @Test
    fun `when fibonacci sequence is requested for a number lower than allowed minimum, service should throw an exception`(){
        // given
        val number = FibonacciSequenceCalculator.MIN_NUMBER - 1
        val pageCount = 1
        val pageSize = 50

        // when
        // then
        assertThrowsExactly(EntityOutOfAllowedRangeException::class.java) {
            service.calculateFibonacciSequence(number, pageSize, pageCount)
        }
    }

    @Test
    fun `when fibonacci sequence is requested for a number higher than allowed maximum, service should throw an exception`(){
        // given
        val number = FibonacciSequenceCalculator.MAX_NUMBER + 1
        val pageCount = 1
        val pageSize = 50

        // when
        // then
        assertThrowsExactly(EntityOutOfAllowedRangeException::class.java) {
            service.calculateFibonacciSequence(number, pageSize, pageCount)
        }
    }

    @Test
    fun `when fibonacci sequence is requested for a number and pageSize is higher than allowed maximum, service should throw an exception`(){
        // given
        val number = 40
        val pageCount = 1
        val pageSize = FibonacciSequenceCalculator.PAGE_SIZE_MAX + 1

        // when
        // then
        assertThrowsExactly(EntityOutOfAllowedRangeException::class.java) {
            service.calculateFibonacciSequence(number, pageSize, pageCount)
        }
    }

    @Test
    fun `when fibonacci sequence is requested for a number and pageSize is lower than allowed minimum, service should throw an exception`(){
        // given
        val number = 40
        val pageCount = 1
        val pageSize = FibonacciSequenceCalculator.PAGE_SIZE_MIN - 1

        // when
        // then
        assertThrowsExactly(EntityOutOfAllowedRangeException::class.java) {
            service.calculateFibonacciSequence(number, pageSize, pageCount)
        }
    }

    @Test
    fun `when fibonacci sequence is requested for a number and pageCount is lower than allowed minimum, service should throw an exception`(){
        // given
        val number = 40
        val pageCount = 0
        val pageSize = 50

        // when
        // then
        assertThrowsExactly(EntityOutOfAllowedRangeException::class.java) {
            service.calculateFibonacciSequence(number, pageSize, pageCount)
        }
    }

    @Test
    fun `when fibonacci sequence is requested for a number and pageCount is higher than potential maximum, service should throw an exception`(){
        // given
        val number = 40
        val pageCount = 2
        val pageSize = 50

        // when
        // then
        assertThrowsExactly(EntityOutOfAllowedRangeException::class.java) {
            service.calculateFibonacciSequence(number, pageSize, pageCount)
        }
    }
}