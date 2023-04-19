package com.example.icmproject1

data class UserModel(
    var userId : String? = null,
    var username : String? = null,
    var email : String? = null,
    var password : String? = null,
    var chosenFestival : String? = null,
    var listOfBuddies : ArrayList<String>? = null,
    var listOfFestivals : ArrayList<String>? = null,
    var coordinates : String? = null,
    var qrCode : String? = null // make sure
)