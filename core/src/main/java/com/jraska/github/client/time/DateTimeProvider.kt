package com.jraska.github.client.time

import org.threeten.bp.Instant

interface DateTimeProvider {
    fun now(): Instant
}
