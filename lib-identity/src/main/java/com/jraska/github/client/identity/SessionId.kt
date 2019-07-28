package com.jraska.github.client.identity

import java.util.UUID

data class SessionId(val value: UUID) {
  override fun toString(): String {
    return value.toString()
  }

  companion object {
    fun newSessionId(): SessionId {
      return SessionId(UUID.randomUUID())
    }
  }
}
