package com.jraska.github.client;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import com.jraska.github.client.common.Preconditions;

import org.robolectric.RuntimeEnvironment;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

public class ViewModelFactoryJavaDecorator implements ViewModelProvider.Factory {
  private final ViewModelProvider.Factory decoratedFactory;
  private final Map<Class, Provider<ViewModel>> providersMap;

  public ViewModelFactoryJavaDecorator(ViewModelProvider.Factory decoratedFactory,
                                       Map<Class, Provider<ViewModel>> providersMap) {
    this.decoratedFactory = Preconditions.argNotNull(decoratedFactory);
    this.providersMap = providersMap;
  }

  public ViewModelProvider.Factory decoratedFactory() {
    return decoratedFactory;
  }

  @Override @SuppressWarnings("unchecked")
  public <T extends ViewModel> T create(Class<T> aClass) {
    Provider<ViewModel> viewModelProvider = providersMap.get(aClass);
    if (viewModelProvider != null) {
      return (T) viewModelProvider.get();
    }

    return decoratedFactory.create(aClass);
  }

  public static void setToApp(Class<? extends ViewModel> viewModelClass,
                              ViewModel implementation) {
    Context applicationContext = RuntimeEnvironment.application;
    setToApp((GitHubClientApp) applicationContext, viewModelClass, implementation);
  }

  static void setToApp(GitHubClientApp app,
                       Class<? extends ViewModel> viewModelClass, ViewModel implementation) {
    HashMap<Class, Provider<ViewModel>> map = new HashMap<>();
    map.put(viewModelClass, () -> implementation);

    app.viewModelFactory = new ViewModelFactoryJavaDecorator(app.viewModelFactory, map);
  }
}
