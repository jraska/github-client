package com.jraska.github.client

import com.jraska.github.client.http.HttpComponent
import com.jraska.github.client.users.UserViewModelModule
import com.jraska.github.client.users.data.UsersDataModule
import dagger.Component

@PerApp
@Component(dependencies = arrayOf(HttpComponent::class),
  modules = arrayOf(UsersDataModule::class, UserViewModelModule::class,
    FirebaseModule::class, NavigationModule::class, AppModule::class))
interface AppComponent {
  fun inject(app: GitHubClientApp)
}
