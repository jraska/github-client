package com.jraska.github.client.http

import android.Manifest
import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.jraska.github.client.HasRetrofit
import com.jraska.github.client.users.ui.UsersActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okreplay.AndroidTapeRoot
import okreplay.OkReplayConfig
import okreplay.OkReplayRuleChain
import okreplay.TapeMode
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ReplayHttpComponent private constructor(private val retrofit: Retrofit) : HasRetrofit {

  override fun retrofit(): Retrofit {
    return retrofit
  }

  companion object {
    private val REPLAY_INTERCEPTOR = CustomOkReplayInterceptor()
    private const val NETWORK_ERROR_MESSAGE = "You are trying to do network requests in tests you naughty developer!"

    fun create(): ReplayHttpComponent {
      val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okReplayClient())
        .build()

      return ReplayHttpComponent(retrofit)
    }

    fun okReplayRule(): TestRule {
      return okReplayRule(TapeMode.READ_ONLY)
    }

    fun okReplayRule(tapeMode: TapeMode): TestRule {
      return okReplayRule(UsersActivity::class.java, tapeMode)
    }

    fun <T : Activity> okReplayRule(activityClass: Class<T>, tapeMode: TapeMode): TestRule {
      val testClass = findTestClassInStack()

      val configuration = OkReplayConfig.Builder()
        .tapeRoot(AndroidTapeRoot(InstrumentationRegistry.getInstrumentation().context, testClass))
        .defaultMode(tapeMode)
        .sslEnabled(true)
        .interceptor(REPLAY_INTERCEPTOR)
        .build()

      return RuleChain.outerRule(GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        .around(OkReplayRuleChain(configuration, ActivityTestRule(activityClass)).get())
    }

    private fun findTestClassInStack(): Class<*> {
      for (element in Throwable().stackTrace) {
        if (element.className.endsWith("Test")) {
          return Class.forName(element.className)
        }
      }

      throw IllegalStateException("No test class with suffix 'Test' found")
    }

    private fun okReplayClient(): OkHttpClient {
      val httpLoggingInterceptor = HttpLoggingInterceptor()
      val builder = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor.apply { httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC })

      REPLAY_INTERCEPTOR.interceptorsToRegister().forEach { builder.addInterceptor(it) }

      val noNetworkInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
          throw UnsupportedOperationException(NETWORK_ERROR_MESSAGE)
        }
      }
      builder.addNetworkInterceptor(noNetworkInterceptor)

      return builder.build()
    }
  }
}
