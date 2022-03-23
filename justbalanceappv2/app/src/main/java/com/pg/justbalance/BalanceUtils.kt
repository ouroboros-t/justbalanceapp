package com.pg.justbalance

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

fun decimalFormatDouble(item: BigDecimal): String{
    var decimalFormat = DecimalFormat("#.##")
    decimalFormat.roundingMode = RoundingMode.DOWN
    val formattedItem = decimalFormat.format(item)
    return "$ ${formattedItem}"
}
