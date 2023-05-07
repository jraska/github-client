package com.jraska.github.client

import com.jraska.github.client.time.TimeProvider

class TestTimeProvider(var elapsed: Long = 0) : TimeProvider {
  override fun elapsed() = elapsed

  fun advanceTime(difference: Long) {
    elapsed += difference
  }
}
