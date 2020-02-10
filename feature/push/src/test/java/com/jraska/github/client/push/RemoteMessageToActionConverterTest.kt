package com.jraska.github.client.push

import java.util.Collections
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RemoteMessageToActionConverterTest {
  @Test
  fun whenRemoteMessageWithoutAction_thenDefaultReturned() {
    val action =
      RemoteMessageToActionConverter.convert(Collections.emptyMap<String, String>())

    assertThat(action.name).isEqualTo(PushAction.DEFAULT.name)
  }

  @Test
  fun whenRemoteMessageWithAction_thenActionReturned() {
    val action = RemoteMessageToActionConverter.convert(mapOf("action" to "heeeya"))

    assertThat(action.name).isEqualTo("heeeya")
    assertThat(action.parameters).isEmpty()
  }

  @Test
  fun whenRemoteMessageWithParameters_thenActionReturned() {
    val map = mapOf("action" to "heeeya", "parameter" to "true")

    val action = RemoteMessageToActionConverter.convert(map)

    assertThat(action.name).isEqualTo("heeeya")
    assertThat(action.parameters).hasSize(1)
    assertThat(action.parameters["parameter"]).isEqualTo("true")
  }
}
