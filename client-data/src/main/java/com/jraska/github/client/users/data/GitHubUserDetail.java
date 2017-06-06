package com.jraska.github.client.users.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

final class GitHubUserDetail {

  @SerializedName("login")
  @Expose
  public String login;
  @SerializedName("id")
  @Expose
  public Integer id;
  @SerializedName("avatar_url")
  @Expose
  public String avatarUrl;
  @SerializedName("gravatar_id")
  @Expose
  public String gravatarId;
  @SerializedName("url")
  @Expose
  public String url;
  @SerializedName("html_url")
  @Expose
  public String htmlUrl;
  @SerializedName("followers_url")
  @Expose
  public String followersUrl;
  @SerializedName("following_url")
  @Expose
  public String followingUrl;
  @SerializedName("gists_url")
  @Expose
  public String gistsUrl;
  @SerializedName("starred_url")
  @Expose
  public String starredUrl;
  @SerializedName("subscriptions_url")
  @Expose
  public String subscriptionsUrl;
  @SerializedName("organizations_url")
  @Expose
  public String organizationsUrl;
  @SerializedName("repos_url")
  @Expose
  public String reposUrl;
  @SerializedName("events_url")
  @Expose
  public String eventsUrl;
  @SerializedName("received_events_url")
  @Expose
  public String receivedEventsUrl;
  @SerializedName("type")
  @Expose
  public String type;
  @SerializedName("site_admin")
  @Expose
  public Boolean siteAdmin;
  @SerializedName("name")
  @Expose
  public String name;
  @SerializedName("company")
  @Expose
  public Object company;
  @SerializedName("blog")
  @Expose
  public String blog;
  @SerializedName("location")
  @Expose
  public String location;
  @SerializedName("email")
  @Expose
  public String email;
  @SerializedName("hireable")
  @Expose
  public Object hireable;
  @SerializedName("bio")
  @Expose
  public Object bio;
  @SerializedName("public_repos")
  @Expose
  public Integer publicRepos;
  @SerializedName("public_gists")
  @Expose
  public Integer publicGists;
  @SerializedName("followers")
  @Expose
  public Integer followers;
  @SerializedName("following")
  @Expose
  public Integer following;
  @SerializedName("created_at")
  @Expose
  public String createdAt;
  @SerializedName("updated_at")
  @Expose
  public String updatedAt;

}
