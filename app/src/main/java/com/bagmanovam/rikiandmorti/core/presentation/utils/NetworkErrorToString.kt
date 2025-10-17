package com.bagmanovam.rikiandmorti.core.presentation.utils

import android.content.Context
import com.bagmanovam.rikiandmorti.R
import com.bagmanovam.rikiandmorti.core.domain.NetworkError


fun NetworkError.toString(context: Context): String {
    return when(this) {
        NetworkError.REQUEST_TIMEOUT -> context.resources.getString(R.string.request_timeout)
        NetworkError.TOO_MANY_REQUESTS -> context.resources.getString(R.string.too_many_requests)
        NetworkError.NO_INTERNET -> context.resources.getString(R.string.no_internet)
        NetworkError.CONNECTION_ERROR -> context.resources.getString(R.string.no_internet)
        NetworkError.SERVER_ERROR -> context.resources.getString(R.string.unknown)
        NetworkError.SERIALIZATION -> context.resources.getString(R.string.unknown)
        NetworkError.UNKNOWN -> context.resources.getString(R.string.unknown)
    }
}