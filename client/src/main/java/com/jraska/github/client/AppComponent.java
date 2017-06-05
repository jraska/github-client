package com.jraska.github.client;

import com.jraska.github.client.data.users.UsersModule;
import com.jraska.github.client.http.HttpComponent;
import com.jraska.github.client.ui.ActivityComponent;
import com.jraska.github.client.ui.ActivityModule;
import com.jraska.github.client.ui.UsersViewModel;

import dagger.Component;

@PerApp
@Component(dependencies = HttpComponent.class,
    modules = {
        UsersModule.class,
        FirebaseModule.class,
        AppModule.class
    })
public interface AppComponent {
  ActivityComponent activityComponent(ActivityModule activityModule);

  void inject(GitHubClientApp app);

  void inject(UsersViewModel viewModel);
}
