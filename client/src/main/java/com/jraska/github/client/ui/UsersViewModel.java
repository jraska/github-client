package com.jraska.github.client.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.jraska.github.client.GitHubClientApp;
import com.jraska.github.client.rx.RxLiveDataAdapter;
import com.jraska.github.client.users.User;
import com.jraska.github.client.users.UsersScreen;

import java.util.List;

import javax.inject.Inject;

public class UsersViewModel extends AndroidViewModel {
  @Inject UsersScreen usersScreen;

  private LiveData<List<User>> users;

  public UsersViewModel(Application application) {
    super(application);

    ((GitHubClientApp) application).component().inject(this);
    users = RxLiveDataAdapter.adapt(usersScreen.users());
  }

  LiveData<List<User>> users() {
    return users;
  }
}
