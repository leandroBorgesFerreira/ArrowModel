package types

import arrow.instance
import arrow.typeclasses.Monoid
import domain.Bill
import domain.Charge
import domain.VirtualCard
import java.time.LocalDate

@instance(VirtualCard::class)
interface VirtualCardMonoid : Monoid<VirtualCard> {
    override fun empty(): VirtualCard = VirtualCard(1, listOf(), "000", LocalDate.MIN)

    override fun VirtualCard.combine(b: VirtualCard): VirtualCard =
            VirtualCard(
                    this.id,
                    mutableListOf<Charge>().apply {
                        addAll(this@combine.chargeList)
                        addAll(b.chargeList)
                    },
                    this.cvc,
                    this.expireDate
            )
}