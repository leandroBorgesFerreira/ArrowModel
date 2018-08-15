package types

import arrow.instance
import arrow.typeclasses.Semigroup
import domain.Bill
import java.time.LocalDate

@instance(Bill::class)
interface BillSemigroup : Semigroup<Bill> {

    override fun Bill.combine(b: Bill): Bill = Bill(
            this.amount + b.amount,
            dueDate
    )
}
