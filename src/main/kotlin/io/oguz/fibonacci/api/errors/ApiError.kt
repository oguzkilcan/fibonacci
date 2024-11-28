package io.oguz.fibonacci.api.errors

data class ApiError(val httpStatus: Int, val message: String)
