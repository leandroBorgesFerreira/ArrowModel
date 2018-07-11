import logic.totalBill
import java.time.LocalDate

fun main(args : Array<String>) {
    println("Combined bill value: " + totalBill(LocalDate.now()).amount)
}