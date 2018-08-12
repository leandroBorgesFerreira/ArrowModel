package api

import arrow.core.*
import domain.Charge
import domain.VirtualCard
import java.time.LocalDate

class VirtualCardDb : EntityDAO<VirtualCard> {

    private val cardsMap: MutableMap<Long, VirtualCard> =
            mutableMapOf(
                    Pair(1L, VirtualCard(1,
                            listOf(Charge(1, 100.0, LocalDate.of(2018, 3, 3))),
                            "123",
                            LocalDate.of(2020, 1, 1))),
                    Pair(2L, VirtualCard(2,
                            listOf(Charge(2, 300.0, LocalDate.of(2018, 3, 1))),
                            "456",
                            LocalDate.of(2021, 1, 1))),
                    Pair(3L, VirtualCard(3,
                            listOf(Charge(3, 500.0, LocalDate.of(2018, 1, 1))),
                            "789",
                            LocalDate.of(2019, 1, 1)))
            )

    override fun getData(): List<VirtualCard> = cardsMap.values.toList()

    override fun removeEntity(id: Long) : Option<VirtualCard> = cardsMap.remove(id).toOption()

    override fun getEntity(id: Long): Option<VirtualCard> = cardsMap[id].toOption()

    override fun saveEntity(entity: VirtualCard) : Option<VirtualCard> = cardsMap.put(entity.id, entity).toOption()

}
