package com.gig.gettipapp.util

fun calculateTotalTip(totalBill: String, percentage: Int): Double {
    var result = 0.0
    totalBill.toDoubleOrNull()?.let {
        result = if (it > 1) (it * percentage) / 100 else 0.0
    }
    return result
}

fun calculateTotalPerPerson(totalAmount: Double, people: Int): Double = totalAmount / people

fun calculateTotalAmount(totalBill: String, tipAmount: Double): Double {
    var result = 0.0
    totalBill.toDoubleOrNull()?.let {
        result = it + tipAmount
    }
    return result
}
