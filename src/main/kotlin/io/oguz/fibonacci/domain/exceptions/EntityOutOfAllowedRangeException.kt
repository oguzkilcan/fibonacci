package io.oguz.fibonacci.domain.exceptions

class EntityOutOfAllowedRangeException(entity: String, current: Int, min: Int, max: Int) :
    Exception("'$entity' [$current] is outside of allowed range [min: $min, max: $max]")