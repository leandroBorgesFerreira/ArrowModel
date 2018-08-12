package api

import arrow.Kind
import arrow.core.Option
import arrow.effects.typeclasses.MonadDefer

class DeferredDAO<F, T>(
        private val dao: EntityDAO<T>,
        private val monadDefer: MonadDefer<F>)
    : DeferredEntityDAO<F, T>, MonadDefer<F> by monadDefer {

    override fun MonadDefer<F>.getData(): Kind<F, List<T>> = invoke { dao.getData() }

    override fun MonadDefer<F>.getEntity(id: Long): Kind<F, Option<T>> = invoke { dao.getEntity(id) }

    override fun MonadDefer<F>.removeEntity(id: Long): Kind<F, Option<T>> = invoke { dao.removeEntity(id) }

    override fun MonadDefer<F>.saveEntity(entity: T): Kind<F, Option<T>> = invoke { dao.saveEntity(entity) }

}