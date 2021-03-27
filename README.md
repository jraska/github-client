# github-client
Experimental architecture app with example usage intended to be a showcase, test and skeleton app.

<img width="1667" alt="Screenshot 2021-01-25 at 23 16 04" src="https://user-images.githubusercontent.com/6277721/105773043-71c3f200-5f63-11eb-9a28-99073c2e92ce.png">

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
- UI Instrumentation testing using Espresso and mocking network layer to achieve isolation [OkReplay](https://github.com/airbnb/okreplay) See `ReplayHttpComponent`
- Tests are run on Firebase Test Lab. [See PR](https://github.com/jraska/github-client/pull/233)
- UI tests on Firebase Test Lab results reporting to Mixpanel - see [this PR](https://github.com/jraska/github-client/pull/342).
- Uses [LiveData-Testing](https://github.com/jraska/livedata-testing) to test ViewModel. [Article](https://android.jlelse.eu/effective-livedata-and-viewmodel-testing-17f25069fcd4)
- Push integration is tested end to end through UI test - see [this PR](https://github.com/jraska/github-client/pull/300).

## Release & CI
- Release publishing by [Triple-T/google-play-publisher plugin](https://github.com/Triple-T/gradle-play-publisher)
- [GitHub Actions implemented](https://github.com/jraska/github-client/tree/master/.github/workflows) with full releasing to Play Store with [automatic version bump](https://github.com/jraska/github-client/blob/master/.github/workflows/release_trigger.yml).
- Automatic creation of release, release tag, release milestone and tagging all PRs which belong to that release with certain mileston. See [this issue](https://github.com/jraska/github-client/issues/236#issuecomment-802366339), [this PR](https://github.com/jraska/github-client/pull/437), [example release](https://github.com/jraska/github-client/releases/tag/0.23.4) or [tagged pull requests](https://github.com/jraska/github-client/pulls?q=is%3Apr+is%3Aclosed).

## Architecture
- Android Architectue Components `LiveData` and `ViewModel` are used to connect Activities with app logic
- Deep Link navigation used across the app - [Article](https://proandroiddev.com/in-app-deep-link-navigation-because-deep-links-matter-17f0c91f2658)
- All core services have its lightweight fake implementation. See `Fakes`
- Dependency replacement in test is done by Dagger components in `TestUITestApp`
- RxJava is used for threading everywhere, allowing proper idling of UI tests. Also `AppSchedulers` dependency makes all threading testable.
- Push is implemented by using Firebase Cloud Messaging. See `PushActionCommand`. Thanks to deep link navigation app can be controlled remotely by executing deep links - `LaunchDeepLinkCommand : PushActionCommand`
- `Navigator` pattern to be able to easily navigate without `Context`
- `TopActivityProvider` to avoid having `Context` dependencie everywhere and to be able to have cleaner pure Kotlin interfaces
- Enforced ownership of remote configuration and analytics events - [Details on PR](https://github.com/jraska/github-client/pull/230). More on why these need to be explicitly owned on [this article](https://proandroiddev.com/remote-feature-flags-do-not-always-come-for-free-a372f1768a70).

## Metrics
- Build time tracking with reporting to Mixpanel - see [this PR](https://github.com/jraska/github-client/pull/303).
- Modularisation statistics reporting to Mixpanel - see [this PR](https://github.com/jraska/github-client/pull/334).
- Dependencies reporting to see which modules depend on too much - see [this PR](https://github.com/jraska/github-client/pull/371
)
- Lint issues tracking to see the health of modules - see [this PR](https://github.com/jraska/github-client/pull/372)
- UI tests on Firebase Test Lab results reporting to Mixpanel - see [this PR](https://github.com/jraska/github-client/pull/342).
- [Key 4 DevOps metrics](https://circleci.com/blog/how-to-measure-devops-success-4-key-metrics/) implemented through build time reporting and GitHub Webhooks
![Screenshot 2021-03-27 at 12 15 08](https://user-images.githubusercontent.com/6277721/112718893-30fe4000-8ef6-11eb-9878-593f1cfd2b7e.png)
