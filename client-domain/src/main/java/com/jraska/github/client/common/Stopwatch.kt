package com.jraska.github.client.common

import com.jraska.github.client.time.RealTimeProvider
import com.jraska.github.client.time.TimeProvider

class Stopwatch(private val timeProvider: TimeProvider) {

  private var start = NOT_SET
  private var stop = NOT_SET

  fun start(): Stopwatch {
    if (running()) {
      return this
    }

    if (stop == NOT_SET) {
      start = timeProvider.elapsed()
    } else {
      stop = NOT_SET
    }

    return this
  }

  fun stop(): Stopwatch {
    if (!running()) {
      return this
    }

    stop = timeProvider.elapsed()
    return this
  }

  fun reset(): Stopwatch {
    start = NOT_SET
    stop = NOT_SET
    return this
  }

  fun restart(): Stopwatch {
    return reset().start()
  }

  fun running(): Boolean {
    return start != NOT_SET && stop == NOT_SET
  }

  fun time(): Long {
    return if (running()) {
      timeProvider.elapsed() - start
    } else {
      stop - start
    }
  }

  companion object {
    private val NOT_SET: Long = -1

    fun create(): Stopwatch {
      return Stopwatch(RealTimeProvider.INSTANCE)
    }

    fun started(): Stopwatch {
      return create().start()
    }
  }
}
