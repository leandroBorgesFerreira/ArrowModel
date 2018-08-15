package types

import arrow.typeclasses.Semigroup
import domain.VirtualCard

interface VirtualCardSemigroup : Semigroup<VirtualCard> {

    override fun VirtualCard.combine(b: VirtualCard): VirtualCard =
            VirtualCard(
                    selectId(this.id, b.id),
                    this@combine.chargeList + b.chargeList,
                    "${this.cvc}${b.cvc}",
                    localDateSemiGroup().run { this@combine.expireDate + b.expireDate }
            )
}


private fun selectId(id1 : Long, id2 : Long) = if(id1 != 0L) id1 else id2
