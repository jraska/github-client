package com.jraska.github.client.users.di

import com.jraska.github.client.FakeUnitTestModules
import com.jraska.github.client.users.UserDetailViewModel
import com.jraska.github.client.users.UsersModule
import com.jraska.github.client.users.UsersViewModel
import dagger.Component
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Singleton
@Component(
  modules = [FakeUnitTestModules::class, UsersModule::class]
)
internal interface TestUsersComponent {
  fun usersViewModel(): UsersViewModel

  fun userDetailViewModel(): UserDetailViewModel
}
