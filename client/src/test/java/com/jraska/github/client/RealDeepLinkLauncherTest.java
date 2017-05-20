package com.jraska.github.client;

import android.app.Activity;
import android.content.Intent;

import com.jraska.github.client.ui.UserDetailActivity;
import com.jraska.github.client.ui.UsersActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.Set;

import okhttp3.HttpUrl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
public class RealDeepLinkLauncherTest {
  @Captor ArgumentCaptor<Intent> intentCaptor;
  @Mock Activity activity;

  RealDeepLinkLauncher deepLinkLauncher;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);

    deepLinkLauncher = new RealDeepLinkLauncher(activity);
  }

  @Test(expected = IllegalArgumentException.class)
  public void whenUnknownHostUrl_thenException() {
    deepLinkLauncher.launch(HttpUrl.parse("https://jraska.com/users"));
  }

  @Test
  public void whenUnsupportedUrl_thenFalse() {
    boolean launch = deepLinkLauncher.launch(HttpUrl.parse("https://github.com/jraska/repos"));
    assertThat(launch).isFalse();
  }

  @Test
  public void whenUsersUrl_thenUsersActivityStarts() {
    RealDeepLinkLauncher deepLinkLauncher = new RealDeepLinkLauncher(activity);
    boolean launch = deepLinkLauncher.launch(HttpUrl.parse("https://github.com/users"));

    assertThat(launch).isTrue();
    verify(activity).startActivity(intentCaptor.capture());
    assertThat(intentCaptor.getValue().getComponent().getClassName())
      .isEqualTo(UsersActivity.class.getName());
  }

  @Test
  public void whenUserDetailUrl_thenUsersActivityStarts() {
    RealDeepLinkLauncher deepLinkLauncher = new RealDeepLinkLauncher(activity);
    boolean launch = deepLinkLauncher.launch(HttpUrl.parse("https://github.com/jraska"));

    assertThat(launch).isTrue();
    verify(activity).startActivity(intentCaptor.capture());
    Intent intent = intentCaptor.getValue();
    assertThat(intent.getComponent().getClassName())
      .isEqualTo(UserDetailActivity.class.getName());
    Set<String> keySet = intent.getExtras().keySet();
    for (String key : keySet) {
      assertThat(intent.getStringExtra(key)).isEqualTo("jraska");
    }
  }
}
