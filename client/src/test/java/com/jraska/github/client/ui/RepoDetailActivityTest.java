package com.jraska.github.client.ui;

import com.jraska.github.client.BuildConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RepoDetailActivityTest {

  @Test
  public void whenRepoLoaded_repoSetProperly() {
    // TODO: #21 Implement test with mocked ViewModel
  }
}
