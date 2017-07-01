package com.jraska.github.client.time;

import org.threeten.bp.LocalDateTime;

public interface DateTimeProvider {
  LocalDateTime now();
}
