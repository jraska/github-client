package com.jraska.github.client.users

import java.io.Serializable

class User(val login: String, val avatarUrl: String,
           val isAdmin: Boolean) : Serializable
