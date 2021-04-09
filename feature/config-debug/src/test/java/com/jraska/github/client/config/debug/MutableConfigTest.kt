package com.jraska.github.client.config.debug

import com.jraska.github.client.Config
import com.jraska.github.client.FakeConfig
import com.jraska.github.client.Owner.CORE_TEAM
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MutableConfigTest {
  @Test
  fun whenModifyingBooleanConfigsThenStateIsKept() {
    val testKey = Config.Key("booleanTest", CORE_TEAM)
    val fakeConfig = FakeConfig.create(mapOf(testKey.name to true))
    val mutableConfig = MutableConfig(fakeConfig)

    assertThat(mutableConfig.getBoolean(testKey)).isTrue

    mutableConfig.setBoolean(testKey, false)
    assertThat(mutableConfig.getBoolean(testKey)).isFalse

    mutableConfig.resetToDefault(testKey)
    assertThat(mutableConfig.getBoolean(testKey)).isTrue
  }

  @Test
  fun whenModifyingLongConfigsThenStateIsKept() {
    val testKey = Config.Key("longTest", CORE_TEAM)
    val fakeConfig = FakeConfig.create(mapOf(testKey.name to 5L))
    val mutableConfig = MutableConfig(fakeConfig)

    assertThat(mutableConfig.getLong(testKey)).isEqualTo(5)

    mutableConfig.setLong(testKey, 6)
    assertThat(mutableConfig.getLong(testKey)).isEqualTo(6)

    mutableConfig.resetToDefault(testKey)
    assertThat(mutableConfig.getLong(testKey)).isEqualTo(5)
  }

  @Test
  fun whenModifyingStringConfigsThenStateIsKept() {
    val testKey = Config.Key("stringTest", CORE_TEAM)
    val fakeConfig = FakeConfig.create(mapOf(testKey.name to "hey dude"))
    val mutableConfig = MutableConfig(fakeConfig)

    assertThat(mutableConfig.getString(testKey)).isEqualTo("hey dude")

    mutableConfig.setString(testKey, "bye dude")
    assertThat(mutableConfig.getString(testKey)).isEqualTo("bye dude")

    mutableConfig.resetToDefault(testKey)
    assertThat(mutableConfig.getString(testKey)).isEqualTo("hey dude")
  }
}
