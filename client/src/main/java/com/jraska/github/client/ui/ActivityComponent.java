package com.jraska.github.client.ui;

import com.jraska.github.client.NavigationModule;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class, NavigationModule.class})
public interface ActivityComponent {
  void inject(UriHandlerActivity uriHandlerActivity);

  void inject(UsersActivity usersActivity);

  void inject(UserDetailActivity userDetailActivity);
}
