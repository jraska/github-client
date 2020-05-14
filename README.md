# github-client
Experimental architecture app with example usage intended to be a showcase, test and skeleton app.

[![Build Status](https://circleci.com/gh/jraska/github-client.svg?style=shield&circle-token=7af979ba3177a70daee39260f230a756edbab6b2)](https://circleci.com/gh/jraska/github-client)

## Topics demonstrated
- Modularised app with flat structure: `:app -> :feature -> :lib -> :core-android -> core`.
- Core features (Analytics, Configuration, Crash reporting, Networking, Logging, Deep Linking) are behing simple pure Kotlin interfaces to achieve convenient simple core interfaces.
- Features are composed together within `AppComponent` in plugin based manner. Each feature contributes by Dagger module. To add a feature only module and Gradle dependency lines are needed.
- Plugin based composition of features and modules contributing to collection of "plugins" - see: `OnAppCreate`, or `LinkLauncher`
- Android Architectue Components `LiveData` and `ViewModel` are used to connect Activities with app logic
- Deep Link navigation used across the app - [Article](https://proandroiddev.com/in-app-deep-link-navigation-because-deep-links-matter-17f0c91f2658)
- UI Instrumentation testing using Espresso and mocking network layer to achieve isolation [OkReplay](https://github.com/airbnb/okreplay) See `ReplayHttpComponent`
- All core services have its lightweight fake implementation. See `Fakes`
- Dependency replacement in test is done by Dagger components in `TestUITestApp`
- RxJava is used for threading everywhere, allowing proper idling of UI tests. Also `AppSchedulers` dependency makes all threading testable.
- Push is implemented by using Firebase Cloud Messaging. See `PushActionCommand`. Thanks to deep link navigation app can be controlled remotely by executing deep links - `LaunchDeepLinkCommand : PushActionCommand`
- `Navigator` pattern to be able to easily navigate without `Context`
- `TopActivityProvider` to avoid having `Context` dependencie everywhere and to be able to have cleaner pure Kotlin interfaces
- Uses [LiveData-Testing](https://github.com/jraska/livedata-testing) to test ViewModel. [Article](https://android.jlelse.eu/effective-livedata-and-viewmodel-testing-17f25069fcd4)
- Example usage of [module graph assertion](https://proandroiddev.com/module-rules-protect-your-build-time-and-architecture-d1194c7cc6bc) - see [here](https://github.com/jraska/github-client/blob/be3b06558118721968547de9237e9b48d1a8833d/app/build.gradle#L141).
- Tests are run on Firebase Test Lab. [See PR](https://github.com/jraska/github-client/pull/233)
- Release publishing by [Triple-T/google-play-publisher plugin](https://github.com/Triple-T/gradle-play-publisher)
