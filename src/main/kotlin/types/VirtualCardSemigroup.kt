package types

import arrow.typeclasses.Semigroup
import domain.VirtualCard

interface VirtualCardSemigroup : Semigroup<VirtualCard> {

    override fun VirtualCard.combine(b: VirtualCard): VirtualCard =
            VirtualCard(
                    this.id,
                    this@combine.chargeList + b.chargeList,
                    this.cvc,
                    expireDate
            )
}
