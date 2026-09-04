package com.unsa.smartflu.models

import java.io.Serializable

data class Device(
    val email_creator: String,
    var id: String,
    var last_value: Int,
    val name: String,
    val tag_description: String,
    val size: Double
) : Serializable