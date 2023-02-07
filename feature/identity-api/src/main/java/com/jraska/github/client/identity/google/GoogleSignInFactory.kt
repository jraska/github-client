package com.jraska.github.client.identity.google

import androidx.fragment.app.Fragment

interface GoogleSignInFactory {
  fun signInFragment(): Fragment
}
