package com.example.drivety.data

/**
 * Created by ericksumargo on 01/11/19
 */

data class Payload(
    val images: List<String>,
    val description: String,
    val coordinates: Pair<String, String>
)
