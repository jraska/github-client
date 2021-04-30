package com.jraska.github.client.http

import android.Manifest
import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.jraska.github.client.users.ui.UsersActivity
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okreplay.AndroidTapeRoot
import okreplay.OkReplayConfig
import okreplay.OkReplayRuleChain
import okreplay.TapeMode
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
object ReplayHttpModule {

  private val retrofit by lazy { createRetrofit() }

  @Provides
  fun retrofit(): Retrofit {
    return retrofit
  }

  private val REPLAY_MEDIATOR = OkReplayMediator()

  private fun createRetrofit(): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://api.github.com")
      .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
      .addConverterFactory(GsonConverterFactory.create())
      .client(okReplayClient())
      .build()
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
      .interceptor(REPLAY_MEDIATOR.okReplayInterceptor)
      .build()

    return RuleChain.outerRule(GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE))
      .around(MediatorRule(REPLAY_MEDIATOR))
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

    REPLAY_MEDIATOR.configure(builder)

    builder.addInterceptor(MockWebServerInterceptor)

    return builder.build()
  }
}

class MediatorRule(private val okReplayMediator: OkReplayMediator) : ExternalResource() {
  override fun before() {
    okReplayMediator.onTestStart()
  }

  override fun after() {
    okReplayMediator.onTestStop()
  }
}
