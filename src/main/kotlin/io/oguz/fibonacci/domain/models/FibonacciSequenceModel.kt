package io.oguz.fibonacci.domain.models

import java.math.BigInteger

/**
 * This class contains a slice of Fibonacci numbers
 */
data class FibonacciSequenceModel(
    val data: List<BigInteger>,
)
