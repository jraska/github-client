package com.jraska.github.client.users;

import com.jraska.github.client.rx.AppSchedulers;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class UsersScreen {
  private final UsersRepository usersRepository;
  private final AppSchedulers appSchedulers;

  @Inject
  public UsersScreen(UsersRepository usersRepository, AppSchedulers appSchedulers) {
    this.usersRepository = usersRepository;
    this.appSchedulers = appSchedulers;
  }

  public Single<List<User>> users() {
    return usersRepository.getUsers(0)
      .subscribeOn(appSchedulers.io())
      .observeOn(appSchedulers.mainThread());
  }
}
