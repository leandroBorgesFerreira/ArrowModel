package domain

import java.time.LocalDate

data class Charge(val id: Long, val amount: Double, val dateTime: LocalDate)