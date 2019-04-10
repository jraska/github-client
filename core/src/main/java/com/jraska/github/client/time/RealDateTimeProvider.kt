package com.jraska.github.client.time

import org.threeten.bp.Instant

class RealDateTimeProvider : DateTimeProvider {
  override fun now(): Instant {
    return Instant.now()
  }
}
