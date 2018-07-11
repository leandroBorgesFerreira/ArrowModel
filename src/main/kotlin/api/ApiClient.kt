package api

import domain.Charge
import domain.VirtualCard
import java.time.LocalDate

private val virtualCardsMap : MutableMap<Long, VirtualCard> =
        mutableMapOf(
                Pair(1L, VirtualCard(1,
                        listOf(Charge(1, 100.0, LocalDate.of(2018, 3, 3))),
                        "123",
                        LocalDate.of(2020, 1,1))),
                Pair(2L, VirtualCard(2,
                        listOf(Charge(2, 300.0, LocalDate.of(2018, 3, 1))),
                        "456",
                        LocalDate.of(2021, 1,1))),
                Pair(3L, VirtualCard(3,
                        listOf(Charge(3, 500.0, LocalDate.of(2018, 1, 1))),
                        "789",
                        LocalDate.of(2019, 1,1)))
        )

fun getVirtualCards() : MutableMap<Long, VirtualCard> = virtualCardsMap