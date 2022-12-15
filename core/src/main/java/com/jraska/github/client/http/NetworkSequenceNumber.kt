package com.jraska.github.client.http

import java.util.concurrent.atomic.AtomicInteger

class NetworkSequenceNumber {
  private val sequenceNumber = AtomicInteger(0)

  fun getNext() = sequenceNumber.incrementAndGet()
}
