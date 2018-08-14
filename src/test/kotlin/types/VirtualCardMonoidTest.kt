package types

import arrow.instances.eq
import arrow.test.laws.MonoidLaws
import arrow.typeclasses.Eq
import domain.Bill
import domain.Charge
import domain.VirtualCard
import domain.monoid
import io.kotlintest.matchers.fail
import io.kotlintest.matchers.shouldEqual
import org.junit.Test
import java.time.LocalDate

class VirtualCardMonoidTest {

    @Test
    fun `test to assert that Bill is a Monoid`() {
        val virtualCard = VirtualCard(
                1,
                listOf(Charge(1, 100.0, LocalDate.of(2018, 3, 3))),
                "123",
                LocalDate.of(2020, 1, 1)
        )

        val equality : Eq<VirtualCard> = Eq.invoke { v1, v2 ->
            v1.shouldEqual(v2)
            true
        }

        MonoidLaws.laws(VirtualCard.monoid(), virtualCard, equality).forEach {
            it.test()
        }
    }
}