package com.jraska.github.client

import android.support.test.rule.ActivityTestRule

import com.jraska.github.client.ui.UsersActivity

/**
 * Currently we have to always start activity first
 */
class MakeTestsPassRule : ActivityTestRule<UsersActivity>(UsersActivity::class.java)
