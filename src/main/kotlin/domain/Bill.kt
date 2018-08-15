package domain

import types.BillSemigroup
import java.time.LocalDate

data class Bill(val amount: Double, val dueDate: LocalDate) {
    companion object {
        fun semigroup() = object : BillSemigroup {}
    }
}