package com.jraska.github.client.users;

public final class RepoHeader {
  public final String owner;
  public final String name;
  public final String description;
  public final int stars;
  public final int forks;

  public RepoHeader(String owner, String name, String description, int stars, int forks) {
    this.owner = owner;
    this.name = name;
    this.description = description;
    this.stars = stars;
    this.forks = forks;
  }

  public String fullName() {
    return owner + "/" + name;
  }

  public static String owner(String fullName) {
    return fullName.substring(0, fullName.indexOf("/"));
  }

  public static String name(String fullName) {
    return fullName.substring(fullName.indexOf("/") + 1);
  }
}
