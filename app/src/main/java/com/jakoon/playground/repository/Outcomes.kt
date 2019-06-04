package com.jakoon.playground.repository

sealed class DataResult<T> {
    data class Success<T>(val data: T) : DataResult<T>()
    data class Failure<T>(val error: Throwable) : DataResult<T>()
}