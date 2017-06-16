package com.jraska.github.client;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import com.jraska.github.client.common.Preconditions;
import org.robolectric.RuntimeEnvironment;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

public class ViewModelFactoryDecorator implements ViewModelProvider.Factory {
  private final ViewModelProvider.Factory decoratedFactory;
  private final Map<Class, Provider<ViewModel>> providersMap;

  public ViewModelFactoryDecorator(ViewModelProvider.Factory decoratedFactory,
                                   Map<Class, Provider<ViewModel>> providersMap) {
    this.decoratedFactory = Preconditions.argNotNull(decoratedFactory);
    this.providersMap = providersMap;
  }

  @Override @SuppressWarnings("unchecked")
  public <T extends ViewModel> T create(Class<T> aClass) {
    Provider<ViewModel> viewModelProvider = providersMap.get(aClass);
    if (viewModelProvider != null) {
      return (T) viewModelProvider.get();
    }

    return decoratedFactory.create(aClass);
  }

  public static void setToApp(Class<? extends ViewModel> viewModelClass, ViewModel implementation) {
    setToApp((GitHubClientApp) RuntimeEnvironment.application, viewModelClass, implementation);
  }

  static void setToApp(GitHubClientApp app,
                       Class<? extends ViewModel> viewModelClass, ViewModel implementation) {
    HashMap<Class, Provider<ViewModel>> map = new HashMap<>();
    map.put(viewModelClass, () -> implementation);

    ViewModelFactoryDecorator decorator = new ViewModelFactoryDecorator(app.viewModelFactory, map);
    app.viewModelFactory = decorator;
  }
}
