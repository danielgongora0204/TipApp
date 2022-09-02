package com.gig.gettipapp.util

fun calculateTotalTip(totalBill: String, percentage: Int): Double {
    var result = 0.0
    totalBill.toDoubleOrNull()?.let {
        result = if(it > 1) (it * percentage) / 100 else 0.0
    }
    return result;
}

fun calculateTotalPerPerson(totalBill: String, totalTip: Double, people: Int) {

}