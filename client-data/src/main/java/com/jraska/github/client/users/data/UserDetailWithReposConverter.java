package com.jraska.github.client.users.data;

import com.jraska.github.client.common.Pair;
import com.jraska.github.client.users.RepoHeader;
import com.jraska.github.client.users.User;
import com.jraska.github.client.users.UserDetail;
import com.jraska.github.client.users.UserStats;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleTransformer;

final class UserDetailWithReposConverter
    implements SingleTransformer<Pair<GitHubUserDetail, List<GitHubRepo>>, UserDetail> {
  static final Comparator<GitHubRepo> BY_STARS_REPO_COMPARATOR = (lhs, rhs) -> rhs.stargazersCount.compareTo(lhs.stargazersCount);
  static final int MAX_REPOS_TO_DISPLAY = 5;

  static final UserDetailWithReposConverter INSTANCE = new UserDetailWithReposConverter();

  @Override
  public Single<UserDetail> apply(Single<Pair<GitHubUserDetail, List<GitHubRepo>>> single) {
    return single.map(result -> translateUserDetail(result.first, result.second));
  }

  UserDetail translateUserDetail(GitHubUserDetail gitHubUserDetail, List<GitHubRepo> gitHubRepos) {
    LocalDateTime joined = LocalDateTime.parse(gitHubUserDetail.createdAt, DateTimeFormatter.ISO_DATE_TIME);

    UserStats stats = new UserStats(gitHubUserDetail.followers, gitHubUserDetail.following,
        gitHubUserDetail.publicRepos, joined);

    Collections.sort(gitHubRepos, BY_STARS_REPO_COMPARATOR);

    List<RepoHeader> usersRepos = new ArrayList<>();
    List<RepoHeader> contributedRepos = new ArrayList<>();

    for (GitHubRepo gitHubRepo : gitHubRepos) {
      if (usersRepos.size() < MAX_REPOS_TO_DISPLAY && gitHubUserDetail.login.equals(gitHubRepo.owner.login)) {
        RepoHeader repo = convert(gitHubRepo);
        usersRepos.add(repo);
      } else if (contributedRepos.size() < MAX_REPOS_TO_DISPLAY) {
        RepoHeader repo = convert(gitHubRepo);
        contributedRepos.add(repo);
      }
    }

    User user = convert(gitHubUserDetail);
    return new UserDetail(user, stats, usersRepos, contributedRepos);
  }

  RepoHeader convert(GitHubRepo gitHubRepo) {
    return new RepoHeader(gitHubRepo.name, gitHubRepo.description,
        gitHubRepo.stargazersCount, gitHubRepo.forks, gitHubRepo.size);
  }

  private User convert(GitHubUserDetail gitHubUser) {
    boolean isAdmin = gitHubUser.siteAdmin == null ? false : gitHubUser.siteAdmin;
    return new User(gitHubUser.login, gitHubUser.avatarUrl, isAdmin, gitHubUser.htmlUrl);
  }
}
