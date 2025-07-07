package com.jmballangca.smcsmonitoringsystem.presentation.ui.auth.login

import com.jmballangca.formbuilder.FormBuilder
import com.jmballangca.formbuilder.FormControl
import com.jmballangca.formbuilder.Validator
import com.jmballangca.smcsmonitoringsystem.data.model.User


class LoginForm : FormBuilder(
    mapOf(
        "username" to FormControl("", listOf(Validator.Required(
            message = "Username is required"
        ))),
        "password" to FormControl("", listOf(Validator.Required(
            message = "Password is required"
        ),
            Validator.Required(),
        ))
    )
)

data class LoginState(
    val isLoading : Boolean = false,
    val loginForm: LoginForm = LoginForm(),
    val isPasswordVisible : Boolean = false,
    val user : User? = null
)