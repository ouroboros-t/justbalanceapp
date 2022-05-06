package com.pg.justbalance.models

import com.google.firebase.firestore.PropertyName

class PaymentModel {

    var paymentId: String = ""

    //needs to be foreign key constraint
    @PropertyName("balanceId")
    var balanceId: String = ""

    @PropertyName("propertyAmount")
    var paymentAmount: Double = 0.0

    @PropertyName("paymentDate")
    var paymentDate: String = ""
}