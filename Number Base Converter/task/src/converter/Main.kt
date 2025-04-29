package converter
import java.math.BigInteger
import java.math.BigDecimal
import java.math.RoundingMode
import java.math.MathContext

const val BASE10 = "10"

fun convertToHex(numInDec: BigDecimal): String {
    val result: String = when (numInDec) {
        BigDecimal("10") -> "A"
        BigDecimal("11") -> "B"
        BigDecimal("12") -> "C"
        BigDecimal("13") -> "D"
        BigDecimal("14") -> "E"
        BigDecimal("15") -> "F"
        BigDecimal("16") -> "G"
        BigDecimal("17") -> "H"
        BigDecimal("18") -> "I"
        BigDecimal("19") -> "J"
        BigDecimal("20") -> "K"
        BigDecimal("21") -> "L"
        BigDecimal("22") -> "M"
        BigDecimal("23") -> "N"
        BigDecimal("24") -> "O"
        BigDecimal("25") -> "P"
        BigDecimal("26") -> "Q"
        BigDecimal("27") -> "R"
        BigDecimal("28") -> "S"
        BigDecimal("29") -> "T"
        BigDecimal("30") -> "U"
        BigDecimal("31") -> "V"
        BigDecimal("32") -> "W"
        BigDecimal("33") -> "X"
        BigDecimal("34") -> "Y"
        BigDecimal("35") -> "Z"
        else -> numInDec.toString()
    }
    return result
}

fun convertFromHex(numInHex: Char): BigDecimal {
    val result: BigDecimal = when (numInHex.uppercase()) {
        "A" -> BigDecimal("10")
        "B" -> BigDecimal("11")
        "C" -> BigDecimal("12")
        "D" -> BigDecimal("13")
        "E" -> BigDecimal("14")
        "F" -> BigDecimal("15")
        "G" -> BigDecimal("16")
        "H" -> BigDecimal("17")
        "I" -> BigDecimal("18")
        "J" -> BigDecimal("19")
        "K" -> BigDecimal("20")
        "L" -> BigDecimal("21")
        "M" -> BigDecimal("12")
        "N" -> BigDecimal("23")
        "O" -> BigDecimal("24")
        "P" -> BigDecimal("25")
        "Q" -> BigDecimal("26")
        "R" -> BigDecimal("27")
        "S" -> BigDecimal("28")
        "T" -> BigDecimal("29")
        "U" -> BigDecimal("30")
        "V" -> BigDecimal("31")
        "W" -> BigDecimal("32")
        "X" -> BigDecimal("33")
        "Y" -> BigDecimal("34")
        "Z" -> BigDecimal("35")
        else -> numInHex.toString().toBigDecimal()
    }
    return result
}

/* Берет дробную часть из числа */
//fun getFractionPart(num: String): String {
//    val dotIndex = num.indexOf(".", 0)
//    var fractionPart = "0."
//    if (dotIndex == -1) {
//        fractionPart = ""
//    } else {
//        for (i in (dotIndex + 1) until num.length) {
//            fractionPart += num[i]
//        }
//    }
//    return fractionPart
//}

/* Переводит целую и дробную часть числа в нужное основание */
//fun convertInNumberBase(num: BigDecimal, base: BigDecimal): String {
//    var quotient = num.toString().substringBefore(".").toBigDecimal()
//    var remainder = quotient
//    var integerPart = ""
//    if (base == BigDecimal(BASE10)) return num.toString()
//    else {
//        if ("." in num.toString()) {
//            while (remainder >= base) {
//                quotient = remainder % base
//                remainder = remainder.setScale(0) / base
//                integerPart += convertToHex(quotient)
//            }
//            integerPart = convertToHex(remainder) + integerPart.reversed()
//            var fractionPart = getFractionPart(num.toString())
//            fractionPart = convertFractionPartInNumberBase(fractionPart.toBigDecimal(), base).substringAfter(".")
//            return if (fractionPart == "") "$integerPart.00000"
//            else "$integerPart.$fractionPart"
//        } else {
//            while (remainder >= base) {
//                    quotient = remainder.toString().substringBefore(".").toBigDecimal() % base
//                    remainder = remainder.setScale(5, RoundingMode.HALF_DOWN) / base
//                    integerPart += convertToHex(quotient)
//            }
//            integerPart = convertToHex(remainder.toString().substringBefore(".").toBigDecimal()) + integerPart.reversed()
//            return integerPart
//    }
//}

operator fun BigInteger.times(other: Int): BigInteger = this.multiply(other.toBigInteger())

operator fun BigDecimal.times(other: Int): BigDecimal = this.multiply(other.toBigDecimal())

fun Int.mapToChar(): Char = if (this > 9) 'a' + this - 10 else '0' + this

fun Char.mapToInt() = if (this.toLowerCase() >= 'a') this.toLowerCase() - 'a' + 10 else this - '0'

/* Переводит дробную часть десятичного числа в дробную часть нужного основания */
fun convertFractionalPart(part: String, sourceBase: Int, targetBase: Int): String {
    val dividend = convertToDecimal(part, sourceBase).toBigDecimal().setScale(10)
    val divisor = sourceBase.toBigDecimal().pow(part.length)
    var num = dividend / divisor

    var result = ""
    while (result.length < 5) {
        val product = num * targetBase
        val integerPart = product.setScale(0, RoundingMode.DOWN)
        result += integerPart.toInt().mapToChar()
        num = product - integerPart
    }
    return result
}

/* Переводит целую часть числа в десятичное основание */
fun convertToDecimal(part: String, sourceBase: Int): BigInteger {
    val charList: List<Char> = part.reversed().toList()
    var num = BigInteger.ZERO
    var powerOfBase = BigInteger.ONE
    for (c in charList) {
        num += powerOfBase * c.mapToInt()
        powerOfBase *= sourceBase.toBigInteger()
    }
    return num
}

/* Переводит целую часть числа в целевое основание */
fun convertIntegerPart(part: String, sourceBase: Int, targetBase: Int): String {
    var num = convertToDecimal(part, sourceBase)
    var result = ""
    val targetBaseBigInteger = targetBase.toBigInteger()
    while (num > BigInteger.ZERO) {
        result = (num % targetBaseBigInteger).toInt().mapToChar() + result
        num /= targetBaseBigInteger
    }
    return if (result == "") "0" else result
}

fun String.convertFromBaseToBase(sourceBase: Int, targetBase: Int): String {
    val integer = this.substringBefore('.')
    val fractional = this.substringAfter('.', "")
    return convertIntegerPart(integer, sourceBase, targetBase) +
            if (fractional.isBlank()) "" else "." + convertFractionalPart(fractional, sourceBase, targetBase)
}

//fun solution(): String {
//    return print("<div>\n\t<p>Hello!, \"Kotlin is the best\"</p>\n</div>").toString()
//}

fun main() {
//    solution()
    do {
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
        val commandLevel1 = readln()
        if (commandLevel1 != "/exit") {
            val (sourceBase, targetBase) = commandLevel1.split(" ")
            do {
                println("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back)")
                val commandLevel2 = readln()
                if (commandLevel2 != "/back") {
                    //val afterConvertToDecimal = convertToDecimal(commandLevel2, sourceBase.toBigDecimal())
                    //println("Conversion result: ${convertInNumberBase(afterConvertToDecimal, targetBase.toBigDecimal())}")
                    println("Conversion result: ${commandLevel2.convertFromBaseToBase(sourceBase.toInt(), targetBase.toInt())}")
                }
            } while (commandLevel2 != "/back")
        }
    } while (commandLevel1 != "/exit")
}