package com.jraska.github.client.users.test

import com.jraska.github.client.core.android.BaseApp

class TestUsersUITestApp : BaseApp() {
  override val appComponent
    get() = testUsersComponent

  val testUsersComponent by lazy { DaggerTestUsersComponent.factory().create(this) }
}
