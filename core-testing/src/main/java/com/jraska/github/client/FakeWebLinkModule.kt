package com.jraska.github.client

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FakeWebLinkModule {
  @Provides
  @Singleton
  fun provideRecordingLinkLauncher(): RecordingWebLinkLauncher {
    return Fakes.recordingWebLinkLauncher()
  }

  @Provides
  fun provideLinkLauncher(recordingLauncher: RecordingWebLinkLauncher): WebLinkLauncher = recordingLauncher
}
