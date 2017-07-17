package com.jraska.github.client.users.data;

import com.jraska.github.client.users.RepoDetail;
import com.jraska.github.client.users.RepoHeader;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

class RepoConverter {
  static final RepoConverter INSTANCE = new RepoConverter();

  RepoHeader convert(GitHubRepo gitHubRepo) {
    return new RepoHeader(gitHubRepo.owner.login, gitHubRepo.name, gitHubRepo.description,
      gitHubRepo.stargazersCount, gitHubRepo.forks);
  }

  RepoDetail convertToDetail(GitHubRepo repo) {
    RepoHeader header = convert(repo);

    LocalDateTime created = LocalDateTime.parse(repo.createdAt, DateTimeFormatter.ISO_DATE_TIME);
RepoDetail.Data data = new RepoDetail.Data(created, repo.openIssuesCount, repo.language,
      repo.subscribersCount);

    return new RepoDetail(header, data);
  }
}
