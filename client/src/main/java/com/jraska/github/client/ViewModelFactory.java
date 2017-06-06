package com.jraska.github.client;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.Map;

public final class ViewModelFactory implements ViewModelProvider.Factory {
  private final Map<Class, Provider<ViewModel>> providersMap;

  @Inject ViewModelFactory(Map<Class<?>, Provider<ViewModel>> providersMap) {
    this.providersMap = Collections.unmodifiableMap(providersMap);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends ViewModel> T create(Class<T> aClass) {
    Provider<ViewModel> provider = providersMap.get(aClass);

    if (provider == null) {
      throw new IllegalArgumentException("There is no provider registered for " + aClass);
    }

    return (T) provider.get();
  }
}
