package com.jraska.github.client.users;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface UsersRepository {
  Single<List<User>> getUsers(int since);

  Observable<UserDetail> getUserDetail(String login, int reposInSection);

  Observable<RepoDetail> getRepoDetail(String owner, String repoName);
}
