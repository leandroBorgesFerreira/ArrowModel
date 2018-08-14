package types

import arrow.instances.eq
import arrow.test.laws.MonoidLaws
import arrow.typeclasses.Eq
import domain.Bill
import domain.monoid
import io.kotlintest.matchers.fail
import io.kotlintest.matchers.shouldEqual
import org.junit.Test
import java.time.LocalDate

class BillMonoidTest {

    @Test
    fun `test to assert that Bill is a Monoid`() {
        val bill = Bill(100.0, LocalDate.now())

        val equality : Eq<Bill> = Eq.invoke { b1, b2 ->
            b1.shouldEqual(b2)
            true
        }

        MonoidLaws.laws(Bill.monoid(), bill, equality).forEach {
            it.test()
        }
    }
}