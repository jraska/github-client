package com.jraska.github.client;

import android.support.test.rule.ActivityTestRule;

import com.jraska.github.client.ui.UsersActivity;

/**
 * Currently we have to always start activity first
 */
public class MakeTestsPassRule extends ActivityTestRule<UsersActivity> {
  private MakeTestsPassRule() {
    super(UsersActivity.class);
  }

  public static MakeTestsPassRule create(){
    return new MakeTestsPassRule();
  }
}
