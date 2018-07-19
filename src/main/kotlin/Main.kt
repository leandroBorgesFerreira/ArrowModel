import logic.getTotalBill
import java.time.LocalDate

fun main(args : Array<String>) {
    println("Combined bill value: " + getTotalBill(LocalDate.now()).amount)
}