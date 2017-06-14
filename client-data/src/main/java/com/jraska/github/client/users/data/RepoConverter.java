package com.jraska.github.client.users.data;

import com.jraska.github.client.users.RepoDetail;
import com.jraska.github.client.users.RepoHeader;

class RepoConverter {
  static final RepoConverter INSTANCE = new RepoConverter();

  RepoHeader convert(GitHubRepo gitHubRepo) {
    return new RepoHeader(gitHubRepo.owner.login, gitHubRepo.name, gitHubRepo.description,
      gitHubRepo.stargazersCount, gitHubRepo.forks, gitHubRepo.size);
  }

  RepoDetail convertToDetail(GitHubRepo gitHubRepo) {
    RepoHeader header = convert(gitHubRepo);

    return new RepoDetail(header, null);
  }
}
