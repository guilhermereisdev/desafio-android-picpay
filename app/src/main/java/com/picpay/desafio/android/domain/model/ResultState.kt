package com.picpay.desafio.android.domain.model

data class ResultState<T>(
    val data: T? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)
