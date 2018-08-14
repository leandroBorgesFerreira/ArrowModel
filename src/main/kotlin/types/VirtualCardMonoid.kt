package types

import arrow.instance
import arrow.typeclasses.Monoid
import domain.VirtualCard
import java.time.LocalDate

@instance(VirtualCard::class)
interface VirtualCardMonoid : Monoid<VirtualCard> {
    override fun empty(): VirtualCard = VirtualCard(1, listOf(), "", LocalDate.MIN)

    override fun VirtualCard.combine(b: VirtualCard): VirtualCard =
            VirtualCard(
                    this.id,
                    this@combine.chargeList + b.chargeList,
                    "${this.cvc}${b.cvc}",
                    localDateMonoid().combineAll(this.expireDate, b.expireDate)
            )
}