package com.jraska.github.client.time

import java.util.concurrent.TimeUnit

class RealTimeProvider private constructor() : TimeProvider {

    override fun elapsed(): Long {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime())
    }

    companion object {
        val INSTANCE = RealTimeProvider()
    }
}
