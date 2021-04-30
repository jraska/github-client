package com.jraska.github.client.users.test

import androidx.test.platform.app.InstrumentationRegistry
import com.jraska.github.client.android.test.DeepLinksRecorder
import com.jraska.github.client.core.android.BaseApp
import com.jraska.github.client.core.android.LinkLauncher
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
object FakeDeepLinkRecordingModule {

  @Provides
  @Singleton
  fun recordingLauncher() = DeepLinksRecorder()

  @Provides
  @IntoSet
  fun linkLauncher(launcher: DeepLinksRecorder): LinkLauncher = launcher
}

interface DeepLinkRecordingComponent {
  val deepLinksRecorder: DeepLinksRecorder
}

fun usingLinkRecording(block: (DeepLinksRecorder) -> Unit) {
  val appComponent = (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as BaseApp).appComponent

  (appComponent as DeepLinkRecordingComponent).deepLinksRecorder.usingLinkRecording(block)
}
