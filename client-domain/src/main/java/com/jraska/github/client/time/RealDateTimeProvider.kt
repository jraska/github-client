package com.jraska.github.client.time

import org.threeten.bp.LocalDateTime

class RealDateTimeProvider : DateTimeProvider {
  override fun now(): LocalDateTime {
    return LocalDateTime.now()
  }
}
