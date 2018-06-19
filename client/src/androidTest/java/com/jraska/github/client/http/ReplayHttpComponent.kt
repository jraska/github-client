package com.jraska.github.client.http

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.IdlingRegistry
import android.support.test.rule.ActivityTestRule
import com.jraska.github.client.MakeTestsPassRule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okreplay.*
import org.junit.rules.TestRule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReplayHttpComponent private constructor(private val retrofit: Retrofit) : HttpComponent {

  override fun retrofit(): Retrofit {
    return retrofit
  }

  companion object {
    private val REPLAY_INTERCEPTOR = OkReplayInterceptor()
    private const val NETWORK_ERROR_MESSAGE = "You are trying to do network requests in tests you naughty developer!"

    fun create(): ReplayHttpComponent {
      val idlingResourceFactory = RxHttpIdlingResourceFactory.create()
      IdlingRegistry.getInstance().register(idlingResourceFactory.idlingResource())

      val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addCallAdapterFactory(idlingResourceFactory)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okReplayClient())
        .build()

      return ReplayHttpComponent(retrofit)
    }

    fun okReplayRule(): TestRule {
      return okReplayRule(MakeTestsPassRule())
    }

    fun <T : Activity> okReplayRule(activityClass: Class<T>): TestRule {
      return okReplayRule(ActivityTestRule(activityClass))
    }

    fun <T : Activity> okReplayRule(rule: ActivityTestRule<T>): TestRule {
      val testClass = findTestClassInStack()

      val configuration = OkReplayConfig.Builder()
        .tapeRoot(AndroidTapeRoot(InstrumentationRegistry.getContext(), testClass))
        .defaultMode(TapeMode.READ_ONLY)
        .sslEnabled(true)
        .interceptor(REPLAY_INTERCEPTOR)
        .build()

      return OkReplayRuleChain(configuration, rule).get()
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
      return OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .addInterceptor(REPLAY_INTERCEPTOR)
        .addNetworkInterceptor { _ -> throw UnsupportedOperationException(NETWORK_ERROR_MESSAGE) }
        .build()
    }


  }
}


