package domain

import types.BillMonoid
import java.time.LocalDate

data class Bill(val amount: Double,
                val dueDate: LocalDate) {
    companion object
}