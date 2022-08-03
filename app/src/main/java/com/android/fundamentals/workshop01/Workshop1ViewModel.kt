package com.android.fundamentals.workshop01

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.fundamentals.workshop01.solution.Workshop1SolutionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class Workshop1ViewModel(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _mutableLoginState = MutableLiveData<LoginResult>()
    private val _mutableLogoutState = MutableLiveData<LogoutResult>()

    val loginState: LiveData<LoginResult> get() = _mutableLoginState
    val logoutState: LiveData<LogoutResult> get() = _mutableLogoutState

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _mutableLoginState.value = LoginResult.Loading()

                withContext(Dispatchers.IO) {
                    delay(DELAY_MILLIS)
                }

                _mutableLoginState.value = when {
                    userName.isEmpty() -> LoginResult.Error.UserName()
                    password.isEmpty() -> LoginResult.Error.Password()
                    else -> {
                        // TODO 02: create updateUserToken fun that will add or update user token in SP
                        updateUserToken()
                        LoginResult.Success()
                    }
                }
            }
        }
    }


    // TODO 03: create logout fun that will wait for logout clear user token from SP
    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _mutableLogoutState.value = LogoutResult.Loading()
                withContext(Dispatchers.IO) {
                    delay(DELAY_MILLIS)
                }
                // TODO 04: create clearUserToken fun that will clear user token from SP when user is logged in
                clearUserToken()
                _mutableLogoutState.value = LogoutResult.Success()
            }
        }
    }

    fun checkUserIsLoggedIn(): Boolean {
        // TODO 05: check sharedPreferences is it has saved user token
        return sharedPreferences.contains(USER_TOKEN_KEY)
    }

    private fun updateUserToken() {
        val newToken = UUID.randomUUID().toString()
        val editor = sharedPreferences.edit()
        editor.putString(USER_TOKEN_KEY, newToken)
        editor.apply()

    }

    private fun clearUserToken() {
        val editor = sharedPreferences.edit()
        editor.remove(USER_TOKEN_KEY)
        editor.apply()
    }

    companion object {
        const val DELAY_MILLIS: Long = 3_000
        const val USER_TOKEN_KEY: String = "user_token_key"
    }
}

sealed class LoginResult {

    class Loading : LoginResult()

    class Success : LoginResult()

    sealed class Error : LoginResult() {

        class UserName : Error()

        class Password : Error()
    }
}

sealed class LogoutResult {

    class Loading : LogoutResult()

    class Success : LogoutResult()

    sealed class Error : LogoutResult()
}