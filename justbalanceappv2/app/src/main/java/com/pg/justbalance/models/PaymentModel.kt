package com.pg.justbalance.models

import com.google.firebase.firestore.PropertyName
import java.util.*

class PaymentModel {

    var paymentId: String = ""

    //needs to be foreign key constraint
    @PropertyName("balanceId")
    var balanceId: String = ""

    @PropertyName("paymentAmount")
    var paymentAmount: Double = 0.0

    @PropertyName("paymentDate")
    var paymentDate: Date?= null
}