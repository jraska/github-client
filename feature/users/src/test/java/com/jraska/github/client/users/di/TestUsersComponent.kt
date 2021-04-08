package com.jraska.github.client.users.di

import com.jraska.github.client.FakeModules
import com.jraska.github.client.users.UserDetailViewModel
import com.jraska.github.client.users.UsersModule
import com.jraska.github.client.users.UsersViewModel
import dagger.Component
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Singleton
@Component(
  modules = [FakeModules::class, UsersModule::class]
)
internal interface TestUsersComponent {
  fun usersViewModel(): UsersViewModel

  fun userDetailViewModel(): UserDetailViewModel

  val mockWebServer: MockWebServer
}
