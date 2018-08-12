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
import arrow.typeclasses.binding
import arrow.typeclasses.bindingCatch
import domain.Bill
import domain.Charge
import domain.VirtualCard
import domain.monoid
import java.time.LocalDate

fun mergeCards(newCardId: Long, oldCardId: Long) =
    VirtualCardDb() pipe { dao ->
        Option.monad().binding {
            val (newCard, oldCard) = Option.applicative()
                    .tupled(dao.getEntity(newCardId), dao.getEntity(oldCardId))
                    .fix()
                    .bind()

            dao.saveEntity(VirtualCard.monoid().combineAll(newCard, oldCard)).bind()
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

fun <A> mergeEntity(newId: Long, oldId: Long, dao: EntityDAO<A>, monoid: Monoid<A>) {
    Option.applicative().tupled(dao.getEntity(newId), dao.getEntity(oldId))
            .fix()
            .map { (newEntity, oldEntity) ->
                dao.saveEntity(monoid.combineAll(newEntity, oldEntity))
                dao.removeEntity(oldId)
            }.toEither {
                //Entity couldn't be merged
            }
}

fun <F, A> mergeDeferred(newId: Long,
                         oldId: Long,
                         dao: DeferredEntityDAO<A>,
                         monad: MonadDefer<F>,
                         monoid: Monoid<A>): Kind<F, Option<A>> =
        monad.bindingCatch {
            val newEntity = dao.getEntity(monad, newId).bind()
            val oldEntity = dao.getEntity(monad, oldId).bind()

            val entity = monad.invoke {
                Option.applicative()
                        .tupled(newEntity, oldEntity)
                        .fix()
                        .map { (newEntity, oldEntity) ->
                            monoid.combineAll(newEntity, oldEntity)
                        }.getOrElse {
                            throw Exception("Ops!")
                        }
            }.bind()

            dao.saveEntity(monad, entity).bind()
            dao.removeEntity(monad, oldId).bind()
        }

fun concreteMerge() {
    mergeDeferred(1, 2, DeferredDAO(VirtualCardDb()), IO.monadDefer(), VirtualCard.monoid())
            .fix()
            .attempt()
            .unsafeRunSync()
            .fold({
                //It didn't succeed
            }, {
                //Success!
            })
}
