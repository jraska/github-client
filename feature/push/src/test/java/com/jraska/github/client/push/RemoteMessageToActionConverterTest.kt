package com.jraska.github.client.push

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RemoteMessageToActionConverterTest {
  @Test
  fun whenRemoteMessageWithoutActionThenDefaultReturned() {
    val action =
      RemoteMessageToActionConverter.convert(emptyMap())

    assertThat(action.name).isEqualTo(RemoteMessageToActionConverter.DEFAULT_ACTION)
  }

  @Test
  fun whenRemoteMessageWithActionThenActionReturned() {
    val action = RemoteMessageToActionConverter.convert(mapOf("action" to "heeeya"))

    assertThat(action.name).isEqualTo("heeeya")
    assertThat(action.parameters).isEmpty()
  }

  @Test
  fun whenRemoteMessageWithParametersThenActionReturned() {
    val map = mapOf("action" to "heeeya", "parameter" to "true")

    val action = RemoteMessageToActionConverter.convert(map)

    assertThat(action.name).isEqualTo("heeeya")
    assertThat(action.parameters).hasSize(1)
    assertThat(action.parameters["parameter"]).isEqualTo("true")
  }
}
