package domain

import types.VirtualCardSemigroup
import java.time.LocalDate

data class VirtualCard(
        val id: Long,
        val chargeList: List<Charge>,
        val cvc: String,
        val expireDate: LocalDate
) {
    companion object {
        fun semigroup() = object : VirtualCardSemigroup {}
    }
}