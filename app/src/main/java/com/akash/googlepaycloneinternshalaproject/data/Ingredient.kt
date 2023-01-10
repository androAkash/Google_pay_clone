package com.akash.googlepaycloneinternshalaproject.data

import androidx.annotation.DrawableRes
import com.akash.googlepaycloneinternshalaproject.R


data class GooglePay(
    val title: String,
    val category: String,
    val barcode: String,
    val bank: String,
    val selfPay: String,
    val mobilerecharge: String,
    val reviews: String,
    val description :String,
    val ingredients: List<Ingredient>,
    val people: List<People>
)

data class Ingredient(@DrawableRes val image: Int, val title: String)
data class People(@DrawableRes val image: Int,val peopleName:String)

val Assets = GooglePay(
    title = "Google Pay",
    category = "Pay Bills With Chill",
    description= "Add Extra UPI IDs for smart routing of payments.Sms Charges Apply",
    barcode = "Scan any QR code",
    bank = "Bank Transfer",
    selfPay = "Self Transfer",
    mobilerecharge = "Mobile recharge",
    reviews = "84 photos     430 comments",

    ingredients = listOf(
        Ingredient(R.drawable.bank,"Bank Transfer"),
        Ingredient(R.drawable.barcode,"Scan any QR code"),
        Ingredient(R.drawable.paybills,"Pay bills"),
        Ingredient(R.drawable.mobilerecharge,"Mobile recharge"),
        Ingredient(R.drawable.selfpay,"Self transfer")
    ),
    people = listOf(
        People(R.drawable.personfirst,"First Person"),
        People(R.drawable.secondperson,"Second Person"),
        People(R.drawable.thirdperson,"Third Person"),
        People(R.drawable.fourthperson,"Fourth Person"),
        People(R.drawable.fifthperson,"Fifth Person"),
    )
)