package com.jraska.github.client.users.data;

import android.support.annotation.NonNull;

import com.jraska.github.client.common.Pair;
import com.jraska.github.client.users.User;
import com.jraska.github.client.users.UserDetail;
import com.jraska.github.client.users.UsersRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static java.util.Collections.emptyList;

final class GitHubApiUsersRepository implements UsersRepository {
  private final GitHubUsersApi gitHubUsersApi;
  private final GitHubUserDetailApi gitHubUserDetailApi;

  private List<User> lastUsers;

  public GitHubApiUsersRepository(@NonNull GitHubUsersApi gitHubUsersApi,
                                  @NonNull GitHubUserDetailApi gitHubUserDetailApi) {
    this.gitHubUsersApi = gitHubUsersApi;
    this.gitHubUserDetailApi = gitHubUserDetailApi;
  }

  @Override public Single<List<User>> getUsers(int since) {
    return gitHubUsersApi.getUsers(since)
        .map(this::translateUsers)
        .doOnSuccess(users -> lastUsers = users);
  }

  @Override public Observable<UserDetail> getUserDetail(String login) {
    return gitHubUserDetailApi.getUserDetail(login)
        .subscribeOn(Schedulers.io()) //this has to be here now to run requests in parallel
        .zipWith(gitHubUserDetailApi.getRepos(login), Pair::new)
        .compose(UserDetailWithReposConverter.INSTANCE)
        .toObservable()
        .startWith(cachedUser(login));
  }

  private Observable<UserDetail> cachedUser(String login) {
    List<User> lastUsers = this.lastUsers;
    if (lastUsers == null) {
      return Observable.empty();
    }

    for (User lastUser : lastUsers) {
      if (login.equals(lastUser.login)) {
        return Observable.just(new UserDetail(lastUser, null, emptyList(), emptyList()));
      }
    }

    return Observable.empty();
  }

  List<User> translateUsers(List<GitHubUser> gitHubUsers) {
    ArrayList<User> users = new ArrayList<>();
    for (GitHubUser gitHubUser : gitHubUsers) {
      users.add(convert(gitHubUser));
    }

    return Collections.unmodifiableList(users);
  }

  private User convert(GitHubUser gitHubUser) {
    boolean isAdmin = gitHubUser.siteAdmin == null ? false : gitHubUser.siteAdmin;
    return new User(gitHubUser.login, gitHubUser.avatarUrl, isAdmin, gitHubUser.htmlUrl);
  }
}
