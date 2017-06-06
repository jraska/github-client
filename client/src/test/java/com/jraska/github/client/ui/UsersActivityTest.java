package com.jraska.github.client.ui;

import com.jraska.github.client.BuildConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UsersActivityTest {
  @Test
  public void whenCreated_thenUsersRecyclerSetUp() throws Exception {
    UsersActivity usersActivity = Robolectric.setupActivity(UsersActivity.class);

    assertThat(usersActivity.usersRecyclerView).isNotNull();
  }
}
