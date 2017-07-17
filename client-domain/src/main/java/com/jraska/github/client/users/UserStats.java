package com.jraska.github.client.users;

import org.threeten.bp.LocalDateTime;

public final class UserStats {
  public final int followers;
  public final int following;
  public final int publicRepos;
  public final LocalDateTime joined;

  public UserStats(int followers, int following, int publicRepos, LocalDateTime joined) {
    this.followers = followers;
    this.following = following;
    this.publicRepos = publicRepos;
    this.joined = joined;
  }
}
