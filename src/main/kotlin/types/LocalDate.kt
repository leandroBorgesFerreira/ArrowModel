package types

import arrow.typeclasses.Semigroup
import java.time.LocalDate

interface LocalDateSemigroup : Semigroup<LocalDate> {

    override fun LocalDate.combine(b: LocalDate): LocalDate =
            if (this == LocalDate.MIN) b else this

}

fun localDateSemiGroup() = object : LocalDateSemigroup {}
