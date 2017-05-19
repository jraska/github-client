package com.jraska.github.client.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.jraska.github.client.Navigator;
import com.jraska.github.client.R;
import com.jraska.github.client.analytics.EventReporter;
import com.jraska.github.client.rx.AppSchedulers;
import com.jraska.github.client.users.User;
import com.jraska.github.client.users.UsersPresenter;
import com.jraska.github.client.users.UsersRepository;
import com.jraska.github.client.users.UsersView;
import com.jraska.github.client.users.UsersViewEvents;

import java.util.List;

import javax.inject.Inject;

public class UsersActivity extends BaseActivity implements UsersFragment.UserListener, UsersView {
  @Inject UsersRepository repository;
  @Inject AppSchedulers schedulers;
  @Inject Navigator navigator;
  @Inject EventReporter eventReporter;

  private UsersFragment usersFragment;
  private UsersPresenter presenter;
  private UsersViewEvents events;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    component().inject(this);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_users_list);

    usersFragment = (UsersFragment) findFragmentById(R.id.fragment_users);

    presenter = new UsersPresenter(this, repository, schedulers, navigator, eventReporter);
    events = presenter;

    presenter.onCreate();
  }

  @Override protected void onDestroy() {
    presenter.onDestroy();

    super.onDestroy();
  }

  @Override
  public void onUserClicked(User user) {
    events.onUserItemClick(user);
  }

  @Override
  public void onUserGitHubIconClicked(User user) {
    events.onUserGitHubIconClick(user);
  }

  void refresh() {
    events.onRefresh();
  }

  @Override public void setUsers(List<User> users) {
    usersFragment.setUsers(users);
  }

  @Override public void startDisplayProgress() {
    usersFragment.showProgressIndicator();
  }

  @Override public void stopDisplayProgress() {
    usersFragment.hideProgressIndicator();
  }

  @Override public void showMessage(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  public static void start(Activity inActivity) {
    Intent intent = new Intent(inActivity, UsersActivity.class);
    inActivity.startActivity(intent);
  }
}
