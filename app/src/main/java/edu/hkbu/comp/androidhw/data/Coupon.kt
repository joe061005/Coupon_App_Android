package edu.hkbu.comp.androidhw.data

data class Coupon(
    val id: Int,
    val title: String,
    val name: String,
    val region: String,
    val mall:String,
    val image: String,
    val quota: Int,
    val coins: Int,
    val validtill: String,
    val detail: String
)
