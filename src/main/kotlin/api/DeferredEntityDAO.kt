package api

import arrow.Kind
import arrow.core.Option
import arrow.effects.typeclasses.MonadDefer

interface DeferredEntityDAO<F, T> : MonadDefer<F> {

    fun MonadDefer<F>.getData() : Kind<F, List<T>>

    fun MonadDefer<F>.getEntity(id: Long) : Kind<F, Option<T>>

    fun MonadDefer<F>.removeEntity(id: Long) : Kind<F, Option<T>>

    fun MonadDefer<F>.saveEntity(entity: T) : Kind<F, Option<T>>

}