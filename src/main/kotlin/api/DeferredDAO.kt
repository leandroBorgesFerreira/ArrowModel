package api

import arrow.Kind
import arrow.core.Option
import arrow.effects.typeclasses.MonadDefer

class DeferredDAO<T>(private val dao: EntityDAO<T>) : DeferredEntityDAO<T> {

    override fun <F> getData(monadDefer: MonadDefer<F>): Kind<F, List<T>> =
            monadDefer { dao.getData() }

    override fun <F> getEntity(monadDefer: MonadDefer<F>, id: Long): Kind<F, Option<T>> =
            monadDefer { dao.getEntity(id) }

    override fun <F> removeEntity(monadDefer: MonadDefer<F>, id: Long): Kind<F, Unit> =
            monadDefer { dao.removeEntity(id) }

    override fun <F> saveEntity(monadDefer: MonadDefer<F>, entity: T): Kind<F, Unit> =
            monadDefer { dao.saveEntity(entity) }

}