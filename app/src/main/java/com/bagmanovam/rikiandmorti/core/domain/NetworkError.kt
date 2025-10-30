package com.bagmanovam.rikiandmorti.core.domain

enum class NetworkError : DomainError {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    CONNECTION_ERROR,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN,
}