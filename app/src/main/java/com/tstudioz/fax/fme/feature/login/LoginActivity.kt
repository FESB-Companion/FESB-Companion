package com.tstudioz.fax.fme.feature.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.tstudioz.fax.fme.routing.LoginRouter
import com.tstudioz.fax.fme.theme.AppTheme
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(InternalCoroutinesApi::class)
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()
    private val router: LoginRouter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        router.register(this)

        isUserLoggedIn()
        onBackListen()
        setContent {
            AppTheme {
                val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
                val error = loginViewModel.error
                LaunchedEffect(Unit) {
                    error.collectLatest {
                        it?.let { message ->
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                }
                LoginScreen(
                    showLoading = loginViewModel.showLoading,
                    snackbarHostState = snackbarHostState,
                    username = loginViewModel.username,
                    password = loginViewModel.password,
                    passwordHidden = loginViewModel.passwordHidden,
                    tryUserLogin = { loginViewModel.tryUserLogin() }
                )
            }
        }

        loginViewModel.checkIfFirstTimeInApp()
        loginViewModel.checkIfLoggedIn()
    }

    private fun onBackListen() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val a = Intent(Intent.ACTION_MAIN)
                a.addCategory(Intent.CATEGORY_HOME)
                a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(a)
            }
        })
    }

    private fun isUserLoggedIn() {
        loginViewModel.loggedIn.observe(this) { _ ->
            router.routeToHome()
        }
    }

}