package com.akash.googlepaycloneinternshalaproject

sealed class Screen(val route:String) {
    object Payment:Screen(route = "payment_screen")
}