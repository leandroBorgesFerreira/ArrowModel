package logic

import api.DeferredDAO
import api.DeferredEntityDAO
import api.EntityDAO
import api.VirtualCardDb
import arrow.Kind
import arrow.core.*
import arrow.effects.IO
import arrow.effects.fix
import arrow.effects.monadDefer
import arrow.effects.typeclasses.MonadDefer
import arrow.syntax.function.pipe
import arrow.typeclasses.Monoid
import arrow.typeclasses.Semigroup
import arrow.typeclasses.binding
import arrow.typeclasses.bindingCatch
import domain.Bill
import domain.Charge
import domain.VirtualCard
import java.time.LocalDate

fun mergeCards(newCardId: Long, oldCardId: Long) =
    VirtualCardDb() pipe { dao ->
        Option.monad().binding {
            val (newCard, oldCard) = Option.applicative()
                    .tupled(dao.getEntity(newCardId), dao.getEntity(oldCardId))
                    .fix()
                    .bind()

            dao.saveEntity(VirtualCard.semigroup().run { newCard + oldCard }).bind()
            dao.removeEntity(oldCard.id).bind()
        }
}

fun getTotalBill(localDate: LocalDate) = totalBill(localDate, VirtualCardDb().getData())

private fun totalBill(dueDate: LocalDate, virtualCards: Iterable<VirtualCard>) : Bill =
        virtualCards
            .map { virtualCard -> virtualCardToBill(virtualCard, dueDate) }
            .reduce { acc, bill -> Bill.monoid().combineAll(acc, bill) }

fun virtualCardToBill(virtualCard: VirtualCard, dueDate: LocalDate) : Bill =
        Bill(totalAmountFromCharges(virtualCard.chargeList), dueDate)

private fun totalAmountFromCharges(chargeList: Iterable<Charge>) : Double =
        chargeList.sumByDouble { charge -> charge.amount }

fun <A> mergeEntity(newId: Long, oldId: Long, dao: EntityDAO<A>, monoid: Monoid<A>) =
    Option.monad().binding {
        Option.applicative().tupled(dao.getEntity(newId), dao.getEntity(oldId))
                .fix()
                .map { (newEntity, oldEntity) ->
                    dao.saveEntity(monoid.combineAll(newEntity, oldEntity))
                }.bind()

        dao.removeEntity(oldId).bind()
    }

fun <F, A> mergeDeferred(newId: Long,
                         oldId: Long,
                         dao: DeferredEntityDAO<F, A>,
                         semigroup: Semigroup<A>): Kind<F, Option<A>> =
        dao.run {
            bindingCatch {
                val newEntity = getEntity(newId).bind()
                val oldEntity = getEntity(oldId).bind()

                val entity = invoke {
                    Option.applicative()
                            .tupled(newEntity, oldEntity)
                            .fix()
                            .map { (newEntity, oldEntity) ->
                                semigroup.run { newEntity + oldEntity }
                            }.getOrElse {
                                throw Exception("Ops!")
                            }
                }.bind()

                dao.saveEntity(entity).bind()
                dao.removeEntity(oldId).bind()
            }
        }

fun concreteMerge() {
    mergeDeferred(1, 2, DeferredDAO(VirtualCardDb(), IO.monadDefer()), VirtualCard.semigroup())
            .fix()
            .attempt()
            .unsafeRunSync()
            .fold({
                //It didn't succeed
            }, {
                //Success!
            })
}
