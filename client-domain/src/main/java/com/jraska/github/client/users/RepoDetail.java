package com.jraska.github.client.users;

import org.threeten.bp.LocalDateTime;

public class RepoDetail {
  public final RepoHeader header;
  public final Data data;

  public RepoDetail(RepoHeader header, Data data) {
    this.header = header;
    this.data = data;
  }

  public static class Data {
    public final LocalDateTime created;
    public final int issuesCount;
    public final String language;
    public final int subscribersCount;
    public final LocalDateTime pushedAt;

    public Data(LocalDateTime created, int issuesCount, String language, int subscribersCount, LocalDateTime pushedAt) {
      this.created = created;
      this.issuesCount = issuesCount;
      this.language = language;
      this.subscribersCount = subscribersCount;
      this.pushedAt = pushedAt;
    }
  }
}
