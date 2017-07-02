package com.jraska.github.client.push;

import com.jraska.github.client.common.Maps;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RemoteMessageToActionConverterTest {
  @Test
  public void whenRemoteMessageWithoutAction_thenDefaultReturned() {
    PushAction action = RemoteMessageToActionConverter.convert(Collections.emptyMap());

    assertThat(action.name).isEqualTo(PushAction.DEFAULT.name);
  }

  @Test
  public void whenRemoteMessageWithAction_thenActionReturned() {
    PushAction action = RemoteMessageToActionConverter.convert(Maps.newHashMap("action", "heeeya"));

    assertThat(action.name).isEqualTo("heeeya");
    assertThat(action.parameters).isEmpty();
  }

  @Test
  public void whenRemoteMessageWithParameters_thenActionReturned() {
    Map<String, String> map = new HashMap<>();
    map.put("action", "heeeya");
    map.put("parameter", "true");

    PushAction action = RemoteMessageToActionConverter.convert(map);

    assertThat(action.name).isEqualTo("heeeya");
    assertThat(action.parameters).hasSize(1);
    assertThat(action.parameters.get("parameter")).isEqualTo("true");
  }
}
