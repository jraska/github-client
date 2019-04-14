package com.jraska.github.client.http

import com.jraska.github.client.logging.VerboseLogger
import dagger.Module
import dagger.Provides
import timber.log.Timber
import java.io.File

@Module
class HttpDependenciesModule(private val cacheDir: File) {

  @Provides internal fun timberLogger(): VerboseLogger {
    return object : VerboseLogger {
      override fun v(message: String) {
        Timber.tag("Network").v(message)
      }
    }
  }

  @Provides internal fun cacheDir(): File {
    return cacheDir
  }
}
