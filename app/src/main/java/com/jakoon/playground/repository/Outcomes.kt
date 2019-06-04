package com.jakoon.playground.repository

sealed class DataRetrievalOutcome<T> {
    data class Success<T>(val list: List<T>) : DataRetrievalOutcome<T>()
    data class Failure<T>(val error: Throwable) : DataRetrievalOutcome<T>()
}