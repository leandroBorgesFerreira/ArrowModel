package api

import arrow.Kind
import arrow.core.Option
import arrow.effects.typeclasses.MonadDefer

interface DeferredEntityDAO<F, T> : MonadDefer<F> {

    fun getData() : Kind<F, List<T>>

    fun getEntity(id: Long) : Kind<F, Option<T>>

    fun removeEntity(id: Long) : Kind<F, Option<T>>

    fun saveEntity(entity: T) : Kind<F, Option<T>>

}