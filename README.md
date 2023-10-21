# github-client
Experimental architecture app with example usage intended to be a showcase, test and skeleton app.

<img width="1640" alt="Module graph" src="https://user-images.githubusercontent.com/6277721/116779526-ed5ba080-aa76-11eb-805d-ffaff2889f8b.png">

# Topics demonstrated
- If you are interested in any of these topics, feel free to reach out by creating an issue or [contact me on Twitter](https://twitter.com/josef_raska) - I'm happy to chat, exchange opinions or development stories.

## Modularisation
- Modularised app with flat structure: `:app -> :feature* -> *-api` with a graph height of 2.
- Example usage of [module graph assertion](https://proandroiddev.com/module-rules-protect-your-build-time-and-architecture-d1194c7cc6bc) - see [here](https://github.com/jraska/github-client/blob/be3b06558118721968547de9237e9b48d1a8833d/app/build.gradle#L141).
- Modularisation statistics reporting to Mixpanel - see [this PR](https://github.com/jraska/github-client/pull/334).
- Core features (Analytics, Configuration, Crash reporting, Networking, Logging, Deep Linking) are behing simple pure Kotlin interfaces to achieve convenient core interfaces.
- Features are composed together within `AppComponent` in plugin based manner. Each feature contributes by Dagger module. To add a feature only module and Gradle dependency lines are needed.
- Plugin based composition of features and modules contributing to collection of "plugins" - see: `OnAppCreate`, or `LinkLauncher`

## Testing
- UI Instrumentation testing using Espresso and mocking network layer with `okhttp3.MockWebServer` to achieve isolation. See [UsersActivityFlowTest](https://github.com/jraska/github-client/blob/master/app/src/androidTest/java/com/jraska/github/client/users/UsersActivityFlowTest.kt) or [DeepLinkLaunchTest](https://github.com/jraska/github-client/blob/master/app/src/androidTest/java/com/jraska/github/client/DeepLinkLaunchTest.kt)
- Tests are run on Firebase Test Lab. [See PR](https://github.com/jraska/github-client/pull/233)
- UI tests on Firebase Test Lab results reporting to Mixpanel - see [this PR](https://github.com/jraska/github-client/pull/342).
- Instrumented tests can live in modules for fast local iteration - see [this PR](https://github.com/jraska/github-client/pull/477) or this [issue track](https://github.com/jraska/github-client/issues/436).
- Uses [LiveData-Testing](https://github.com/jraska/livedata-testing) to test ViewModel. [Article](https://android.jlelse.eu/effective-livedata-and-viewmodel-testing-17f25069fcd4)
- ViewModels are tested with real dependencies, faking only network code and therefore simulating the real usage, increasing the confidence in tests - [Example](https://github.com/jraska/github-client/pull/467/files#diff-7ef3a06920375e0c0ccd785bc53f127aa8700f6f76171f784e0ddcf9dcde7634), [Related article on philosophy](https://kentcdodds.com/blog/write-tests). They are fast as they run on JVM.
- Repository tests are implemented similar like ViewModels tests - using real dependnecies and faking network requests only - [Example](https://github.com/jraska/github-client/blob/master/feature/users/src/test/java/com/jraska/github/client/users/model/GitHubApiUsersRepositoryTest.kt)
- Push integration is tested end to end through UI test - see [this PR](https://github.com/jraska/github-client/pull/300).

## Release & CI
- Release publishing by [Triple-T/google-play-publisher plugin](https://github.com/Triple-T/gradle-play-publisher)
- [GitHub Actions implemented](https://github.com/jraska/github-client/tree/master/.github/workflows) with full releasing to Play Store with [automatic version bump](https://github.com/jraska/github-client/blob/master/.github/workflows/release_trigger.yml).
- Automatic creation of release, release tag, release milestone and tagging all PRs which belong to that release with certain mileston. See [this issue](https://github.com/jraska/github-client/issues/236#issuecomment-802366339), [this PR](https://github.com/jraska/github-client/pull/437), [example release](https://github.com/jraska/github-client/releases/tag/0.23.4) or [tagged pull requests](https://github.com/jraska/github-client/pulls?q=is%3Apr+is%3Aclosed).
- [Dependabot configured](https://github.com/jraska/github-client/blob/master/.github/dependabot.yml) to keep project dependencies up to date.

## Architecture
- Android Architectue Components `LiveData` and `ViewModel` are used to connect Activities with app logic
- Deep Link navigation used across the app - [Article](https://proandroiddev.com/in-app-deep-link-navigation-because-deep-links-matter-17f0c91f2658)
- All core services have its lightweight fake implementation. See `Fakes`
- Dependency replacement in test is done by Dagger components in `TestUITestApp`
- Coroutines is used for threading everywhere, allowing proper idling of UI tests. Also `AppDispatchers` dependency makes all threading testable.
- Push is implemented by using Firebase Cloud Messaging. See `PushActionCommand`. Thanks to deep link navigation app can be controlled remotely by executing deep links - `LaunchDeepLinkCommand : PushActionCommand`
- `TopActivityProvider` to avoid having `Context` dependencie everywhere and to be able to have cleaner pure Kotlin interfaces
- Enforced ownership of remote configuration and analytics events - [Details on PR](https://github.com/jraska/github-client/pull/230). More on why these need to be explicitly owned on [this article](https://proandroiddev.com/remote-feature-flags-do-not-always-come-for-free-a372f1768a70).

## Metrics
In case you want to copy and use any of metrics with your analytics platform, the only adjustment could be your own [`AnalyticsReporter` implementation](https://github.com/jraska/github-client/blob/master/plugins/src/main/java/com/jraska/analytics/AnalyticsReporter.kt#L6).
- Build time tracking with reporting to Mixpanel - see [this PR](https://github.com/jraska/github-client/pull/303).
- Modularisation statistics reporting to Mixpanel - see [this PR](https://github.com/jraska/github-client/pull/334).
- Dependencies reporting to see which modules depend on too much - see [this PR](https://github.com/jraska/github-client/pull/371)
- Lint issues tracking to see the health of modules - see [this PR](https://github.com/jraska/github-client/pull/372)
- UI tests on Firebase Test Lab results reporting to Mixpanel - see [this PR](https://github.com/jraska/github-client/pull/342).
- [Sonarqube Cloud integrated](https://github.com/jraska/github-client/pull/467#issuecomment-816293325).
- [Key 4 DevOps metrics](https://circleci.com/blog/how-to-measure-devops-success-4-key-metrics/) implemented through build time reporting and GitHub Webhooks
- [Lead Time](https://cloud.google.com/blog/products/devops-sre/using-the-four-keys-to-measure-your-devops-performance) of commit to production - see [this PR](https://github.com/jraska/github-client/pull/474).
![Screenshot 2021-04-10 at 17 47 05](https://user-images.githubusercontent.com/6277721/114275959-d51ed580-9a24-11eb-8787-f3e84672ce85.png)
