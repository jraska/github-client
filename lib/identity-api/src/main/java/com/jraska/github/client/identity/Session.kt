package com.jraska.github.client.identity

import java.util.UUID

data class Session(
  val id: SessionId,
  val userId: UUID
)
