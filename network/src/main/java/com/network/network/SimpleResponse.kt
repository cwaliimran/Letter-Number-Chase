package com.network.network

data class SimpleResponse(
    var data: Data = Data()
) {
    data class Data(
        var success: Boolean = false
    )
}