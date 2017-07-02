package com.jraska.github.client.time;

import org.threeten.bp.LocalDateTime;

public final class RealDateTimeProvider implements DateTimeProvider {
  @Override public LocalDateTime now() {
    return LocalDateTime.now();
  }
}
