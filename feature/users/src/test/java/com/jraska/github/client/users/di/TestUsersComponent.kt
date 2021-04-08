package com.jraska.github.client.users.di

import com.jraska.github.client.Config
import com.jraska.github.client.FakeConfigModule
import com.jraska.github.client.FakeCoreAndroidModule
import com.jraska.github.client.FakeCoreModule
import com.jraska.github.client.http.FakeHttpModule
import com.jraska.github.client.users.UsersModule
import com.jraska.github.client.users.UsersViewModel
import dagger.Component
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Singleton
@Component(
  modules = [FakeCoreModule::class, FakeConfigModule::class,
    UsersModule::class, FakeCoreAndroidModule::class, FakeHttpModule::class]
)
internal interface TestUsersComponent {
  fun usersViewModel(): UsersViewModel

  fun mockWebServer(): MockWebServer
}
