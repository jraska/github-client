package com.jraska.appsize

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AppSizeTest {
  @Test
  fun testRoundsProperly() {
    val appSize = AppSize(1000 * 900 + 58, 1000 * 1026)

    val toString = appSize.toString()

    assertThat(toString).contains("900.06 kB")
    assertThat(toString).contains("1.03 MB")
  }
}
