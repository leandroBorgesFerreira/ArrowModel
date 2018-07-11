package domain

import java.time.LocalDate

class Charge(val id: Long,
             val amount: Double,
             val dateTime: LocalDate)