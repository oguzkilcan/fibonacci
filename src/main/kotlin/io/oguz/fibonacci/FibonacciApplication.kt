package io.oguz.fibonacci

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FibonacciApplication

fun main(args: Array<String>) {
	runApplication<FibonacciApplication>(*args)
}
