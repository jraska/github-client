package com.jraska.github.client.users;

import org.threeten.bp.LocalDateTime;

import io.reactivex.annotations.Nullable;

public class RepoDetail {
  public final RepoHeader header;

  @Nullable
  public final Data data;

  public RepoDetail(RepoHeader header, Data data) {
    this.header = header;
    this.data = data;
  }

  public static class Data {
    public final LocalDateTime created;
    public final String description;
    public final int issuesCount;
    public final String language;
    public final LocalDateTime pushedAt;

    public Data(LocalDateTime created, String description, int issuesCount, String language, LocalDateTime pushedAt) {
      this.created = created;
      this.description = description;
      this.issuesCount = issuesCount;
      this.language = language;
      this.pushedAt = pushedAt;
    }
  }
}
