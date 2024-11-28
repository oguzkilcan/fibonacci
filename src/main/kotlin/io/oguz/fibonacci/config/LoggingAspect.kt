package io.oguz.fibonacci.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.CodeSignature
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Aspect
@Component
class LoggingAspect {
    private val logger = KotlinLogging.logger {}

    @Around("@annotation(LogExecutionTime)")
    @Throws(Throwable::class)
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any {
        val stopWatch = StopWatch()
        stopWatch.start(joinPoint.signature.toShortString())
        val proceed: Any = joinPoint.proceed()
        stopWatch.stop()
        logger.info { "${joinPoint.signature.toShortString()} with args ${(joinPoint.signature as CodeSignature).parameterNames.zip(joinPoint.args)} executed in ${stopWatch.totalTimeMillis} ms" }
        return proceed
    }
}
