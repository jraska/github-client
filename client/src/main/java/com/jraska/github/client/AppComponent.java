package com.jraska.github.client;

import com.jraska.github.client.data.users.UsersModule;
import com.jraska.github.client.http.HttpComponent;
import com.jraska.github.client.ui.UserModelModule;
import dagger.Component;

@PerApp
@Component(dependencies = HttpComponent.class,
  modules = {
    UsersModule.class,
    UserModelModule.class,
    FirebaseModule.class,
    NavigationModule.class,
    AppModule.class
  })
public interface AppComponent {
  void inject(GitHubClientApp app);
}
