package com.jraska.github.client.users;

import java.util.Collections;
import java.util.List;

import io.reactivex.annotations.Nullable;

public class UserDetail {
  public final User user;

  @Nullable
  public final UserStats basicStats;

  public final List<RepoHeader> popularRepos;
  public final List<RepoHeader> contributedRepos;

  public UserDetail(User user, UserStats basicStats, List<RepoHeader> popularRepos,
                    List<RepoHeader> contributedRepos) {
    this.user = user;
    this.basicStats = basicStats;
    this.popularRepos = Collections.unmodifiableList(popularRepos);
    this.contributedRepos = Collections.unmodifiableList(contributedRepos);
  }
}
