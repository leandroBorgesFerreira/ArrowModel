package api

import arrow.core.Option

interface EntityDAO<T> {

    fun getData() : List<T>

    fun getEntity(id: Long) : Option<T>

    fun removeEntity(id: Long)

    fun saveEntity(entity: T)
}