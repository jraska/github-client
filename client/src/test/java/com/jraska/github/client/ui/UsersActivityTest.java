package com.jraska.github.client.ui;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class UsersActivityTest {
  @Test
  public void whenCreated_thenUsersRecyclerSetUp() throws Exception {
    UsersActivity usersActivity = Robolectric.setupActivity(UsersActivity.class);

    assertThat(usersActivity.usersRecyclerView).isNotNull();
  }
}
