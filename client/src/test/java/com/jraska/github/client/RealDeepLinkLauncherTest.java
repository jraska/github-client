package com.jraska.github.client;

import android.content.Intent;

import com.jraska.github.client.ui.BaseActivity;
import com.jraska.github.client.ui.RepoDetailActivity;
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
public class RealDeepLinkLauncherTest {
  @Captor ArgumentCaptor<Intent> intentCaptor;
  @Mock BaseActivity activity;

  RealDeepLinkLauncher deepLinkLauncher;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);

    deepLinkLauncher = new RealDeepLinkLauncher(() -> activity);
  }

  @Test(expected = IllegalArgumentException.class)
  public void whenUnknownHostUrl_thenException() {
    deepLinkLauncher.launch(HttpUrl.parse("https://jraska.com/users"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void whenUnsupportedUrl_thenIllegalStateException() {
    deepLinkLauncher.launch(HttpUrl.parse("https://github.com/jraska/repos/johny"));
  }

  @Test
  public void whenUsersUrl_thenUsersActivityStarts() {
    deepLinkLauncher.launch(HttpUrl.parse("https://github.com/users"));

    verify(activity).startActivity(intentCaptor.capture());
    assertThat(intentCaptor.getValue().getComponent().getClassName())
      .isEqualTo(UsersActivity.class.getName());
  }

  @Test
  public void whenRepoUrl_thenUsersActivityStarts() {
    deepLinkLauncher.launch(HttpUrl.parse("https://github.com/jraska/Falcon"));

    verify(activity).startActivity(intentCaptor.capture());
    assertThat(intentCaptor.getValue().getComponent().getClassName())
      .isEqualTo(RepoDetailActivity.class.getName());
  }

  @Test
  public void whenUserDetailUrl_thenUsersActivityStarts() {
    deepLinkLauncher.launch(HttpUrl.parse("https://github.com/jraska"));

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
