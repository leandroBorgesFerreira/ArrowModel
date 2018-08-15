package types

import arrow.test.laws.SemigroupLaws
import arrow.typeclasses.Eq
import domain.Bill
import io.kotlintest.matchers.shouldEqual
import org.junit.Test
import java.time.LocalDate

class BillSemigroupTest {

    @Test
    fun `test to assert that Bill is a Monoid`() {
        val bill1 = Bill(100.0, LocalDate.now())
        val bill2 = Bill(200.0, LocalDate.now().plusDays(2))
        val bill3 = Bill(300.0, LocalDate.now().plusDays(3))

        val equality : Eq<Bill> = Eq.invoke { b1, b2 ->
            b1.shouldEqual(b2)
            true
        }

        SemigroupLaws.laws(Bill.semigroup(), bill1, bill2, bill3, equality).forEach {
            it.test()
        }
    }
}