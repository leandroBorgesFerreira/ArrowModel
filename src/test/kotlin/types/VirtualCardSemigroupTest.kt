package types

import arrow.instances.eq
import arrow.test.laws.MonoidLaws
import arrow.test.laws.SemigroupLaws
import arrow.typeclasses.Eq
import domain.Bill
import domain.Charge
import domain.VirtualCard
import io.kotlintest.matchers.fail
import io.kotlintest.matchers.shouldEqual
import org.junit.Test
import java.time.LocalDate

class VirtualCardSemigroupTest {

    @Test
    fun `test to assert that Bill is a Monoid`() {
        val v1 = VirtualCard(
                1,
                listOf(Charge(3, 300.0, LocalDate.of(2018, 3, 3))),
                "123",
                LocalDate.of(2020, 1, 3)
        )

        val v2 = VirtualCard(
                5,
                listOf(Charge(2, 100.0, LocalDate.of(2018, 3, 6))),
                "124",
                LocalDate.of(2020, 1, 6)
        )

        val v3 = VirtualCard(
                7,
                listOf(Charge(22, 200.0, LocalDate.of(2018, 3, 8))),
                "223",
                LocalDate.of(2020, 2, 1)
        )

        val equality : Eq<VirtualCard> = Eq.invoke { v1, v2 ->
            v1.shouldEqual(v2)
            true
        }

        SemigroupLaws.laws(VirtualCard.semigroup(), v1, v2, v3, equality).forEach { law ->
            law.test()
        }
    }
}