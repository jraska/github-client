package com.jraska.github.client.users;

public class RepoHeader {
  public final String name;
  public final String description;
  public final int stars;
  public final int forks;
  public final int size;

  public RepoHeader(String name, String description, int stars, int forks, int size) {
    this.name = name;
    this.description = description;
    this.stars = stars;
    this.forks = forks;
    this.size = size;
  }
}
