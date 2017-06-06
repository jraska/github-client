package com.jraska.github.client;

import com.jraska.github.client.users.data.UsersDataModule;
import com.jraska.github.client.http.HttpComponent;
import com.jraska.github.client.users.UserViewModelModule;
import dagger.Component;

@PerApp
@Component(dependencies = HttpComponent.class,
  modules = {
    UsersDataModule.class,
    UserViewModelModule.class,
    FirebaseModule.class,
    NavigationModule.class,
    AppModule.class
  })
public interface AppComponent {
  void inject(GitHubClientApp app);
}
