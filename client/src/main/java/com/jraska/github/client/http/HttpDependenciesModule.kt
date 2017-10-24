package com.jraska.github.client.http

import com.jraska.github.client.common.AppBuildConfig
import com.jraska.github.client.logging.VerboseLogger
import dagger.Module
import dagger.Provides
import timber.log.Timber

import java.io.File

@Module
class HttpDependenciesModule(private val config: AppBuildConfig, private val cacheDir: File) {

  @Provides internal fun config(): AppBuildConfig {
    return config
  }

  @Provides internal fun timberLogger(): VerboseLogger {
    return { message: String -> Timber.tag("Network").v(message) } as VerboseLogger
  }

  @Provides internal fun cacheDir(): File {
    return cacheDir
  }
}
