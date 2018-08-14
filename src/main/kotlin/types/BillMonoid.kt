package types

import arrow.instance
import arrow.typeclasses.Monoid
import domain.Bill
import java.time.LocalDate

@instance(Bill::class)
interface BillMonoid : Monoid<Bill> {

    override fun empty(): Bill = Bill(0.0, LocalDate.MIN)

    override fun Bill.combine(b: Bill): Bill = Bill(
            this.amount + b.amount,
            localDateMonoid().combineAll(this.dueDate, b.dueDate)
    )
}

