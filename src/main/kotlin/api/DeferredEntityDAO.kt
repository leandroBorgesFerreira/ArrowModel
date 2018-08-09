package api

import arrow.Kind
import arrow.core.Option
import arrow.effects.typeclasses.MonadDefer

interface DeferredEntityDAO<T> {

    fun <F> getData(monadDefer: MonadDefer<F>) : Kind<F, List<T>>

    fun <F> getEntity(monadDefer: MonadDefer<F>, id: Long) : Kind<F, Option<T>>

    fun <F> removeEntity(monadDefer: MonadDefer<F>, id: Long) : Kind<F, Unit>

    fun <F> saveEntity(monadDefer: MonadDefer<F>, entity: T) : Kind<F, Unit>

}