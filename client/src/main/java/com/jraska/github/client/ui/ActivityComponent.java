package com.jraska.github.client.ui;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
  void inject(UriHandlerActivity uriHandlerActivity);
}
