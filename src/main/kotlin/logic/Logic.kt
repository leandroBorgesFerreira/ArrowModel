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
import domain.monoid
import java.time.LocalDate

fun combineCard(newCardId: Long, oldCardId: Long) {
    getVirtualCards() pipe { cardMap ->
        Option.applicative().tupled(cardMap[newCardId].toOption(), cardMap[oldCardId].toOption())
                .fix()
                .map { (newCard, oldCard) ->
                    cardMap[newCard.id] = VirtualCard.monoid().combineAll(oldCard, newCard)
                    cardMap.remove(oldCard.id)
                }
    }
}

fun getTotalBill(localDate: LocalDate) = totalBill(localDate, getVirtualCards().values)

private fun totalBill(dueDate: LocalDate, virtualCards: Iterable<VirtualCard>) : Bill =
        virtualCards
            .map { virtualCard -> virtualCardToBill(virtualCard, dueDate) }
            .reduce { acc, bill -> Bill.monoid().combineAll(acc, bill) }

fun virtualCardToBill(virtualCard: VirtualCard, dueDate: LocalDate) : Bill =
        Bill(totalAmountFromCharges(virtualCard.chargeList), dueDate)

private fun totalAmountFromCharges(chargeList: Iterable<Charge>) : Double =
        chargeList.sumByDouble { charge -> charge.amount }
