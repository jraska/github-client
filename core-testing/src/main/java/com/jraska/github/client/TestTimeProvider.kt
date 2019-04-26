package com.jraska.github.client

import com.jraska.github.client.time.TimeProvider

class TestTimeProvider(private var elapsed: Long = 0) : TimeProvider {

  override fun elapsed(): Long {
    return elapsed
  }

  fun elapsed(elapsedMillis: Long): TestTimeProvider {
    this.elapsed = elapsedMillis
    return this
  }

  fun advanceTime(difference: Long): TestTimeProvider {
    if (difference < 0) {
      throw IllegalArgumentException("Can only advance time forward")
    }

    elapsed += difference
    return this
  }
}
