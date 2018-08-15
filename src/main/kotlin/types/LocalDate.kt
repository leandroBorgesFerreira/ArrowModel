package types

import arrow.typeclasses.Monoid
import java.time.LocalDate

interface LocalDateMonoid : Monoid<LocalDate> {

    override fun empty(): LocalDate = LocalDate.MIN

    override fun LocalDate.combine(b: LocalDate): LocalDate =
            if (this == LocalDate.MIN) b else this

}

fun localDateMonoid() = object : LocalDateMonoid {}
