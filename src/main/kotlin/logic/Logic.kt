package logic

import api.getVirtualCards
import arrow.core.Option
import arrow.core.applicative
import arrow.core.fix
import arrow.core.toOption
import arrow.syntax.function.pipe
import domain.Bill
import domain.Charge
import domain.VirtualCard
import types.monoid
import java.time.LocalDate

fun combineCard(newCardId: Long, oldCardId: Long) {
    getVirtualCards() pipe { cardMap ->
        Option.applicative().tupled(cardMap[newCardId].toOption(), cardMap[oldCardId].toOption())
                .fix()
                .map { (newCard, oldCard) ->
                    cardMap[newCard.id] = newCard.monoid().combineAll(oldCard)
                    cardMap.remove(oldCard.id)
                }
    }
}

fun totalBill(dueDate: LocalDate) : Bill =
        getVirtualCards().values
            .map { virtualCard -> virtualCardToBill(virtualCard, dueDate) }
            .pipe { Bill.monoid().combineAll(*it.toTypedArray()) }

fun virtualCardToBill(virtualCard: VirtualCard, dueDate: LocalDate) : Bill =
        Bill(totalAmountFromCharges(virtualCard.chargeList), dueDate)

private fun totalAmountFromCharges(chargeList: List<Charge>) : Double =
        chargeList.sumByDouble { charge -> charge.amount }
