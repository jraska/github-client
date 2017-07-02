package com.jraska.github.client;

import com.jraska.github.client.http.FakeHttpComponent;
import com.jraska.github.client.http.HttpComponent;

import org.junit.Assume;

import static org.hamcrest.core.Is.is;

public final class TestGitHubClientApp extends GitHubClientApp {
  @Override protected HttpComponent httpComponent() {
    return FakeHttpComponent.create();
  }

  @Override protected DaggerAppComponent.Builder componentBuilder() {
    return super.componentBuilder()
      .firebaseModule(new FakeFirebaseModule());
  }

  @Override public void onCreate() {
    super.onCreate();

    assumeNoCI();
  }

  /**
   * Travis cannot download Robolectric from Sonatype from some reason
   */
  public static void assumeNoCI() {
    String ciValue = System.getenv("CI");
    boolean isCi = Boolean.parseBoolean(ciValue);

    Assume.assumeThat("Test should not run on CI server.", isCi, is(false));
  }
}
