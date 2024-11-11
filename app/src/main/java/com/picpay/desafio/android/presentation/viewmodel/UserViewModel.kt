package com.picpay.desafio.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.model.ResultState
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class UserViewModel(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _userState =
        MutableStateFlow<ResultState<List<User>>>(ResultState(isLoading = true))
    open var userState: StateFlow<ResultState<List<User>>> = _userState.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            getUsersUseCase().collect { result ->
                _userState.value = result
            }
        }
    }

    fun refreshUsers() {
        _userState.value = ResultState(isLoading = true)
        fetchUsers()
    }
}
