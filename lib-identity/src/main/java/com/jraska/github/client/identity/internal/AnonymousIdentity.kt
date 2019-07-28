package com.jraska.github.client.identity.internal

import com.jraska.github.client.identity.Session
import java.util.UUID
import javax.inject.Inject

internal class AnonymousIdentity @Inject constructor(
  private val sessionIdProvider: SessionIdProvider
) {
  private val userId: UUID by lazy { UUID.randomUUID() }

  fun session(): Session {
    return Session(sessionIdProvider.sessionId(), userId)
  }
}
