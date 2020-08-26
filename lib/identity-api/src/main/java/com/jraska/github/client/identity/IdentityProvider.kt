package com.jraska.github.client.identity

interface IdentityProvider {
  fun session(): Session
}
