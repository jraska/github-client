package com.jraska.github.client.firebase.report

import com.jraska.github.client.firebase.Device
import com.jraska.github.client.firebase.TestOutcome
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class FirebaseResultExtractorTest {

  lateinit var extractor: FirebaseResultExtractor

  @Before
  fun setUp() {
    extractor = FirebaseResultExtractor(Device.Pixel5)
  }

  @Test
  fun whenSuccessfulResultThenParsesCorrectly() {
    val suiteResult = extractor.extract(SUCCESS_RESULT)

    assertThat(suiteResult.failedCount).isZero()
    assertThat(suiteResult.ignoredCount).isZero()
    assertThat(suiteResult.testsCount).isEqualTo(14)
    assertThat(suiteResult.flakyCount).isZero()
    assertThat(suiteResult.suitePassed).isTrue()
    assertThat(suiteResult.time).isEqualTo(15.511)
    assertThat(suiteResult.testResults).hasSize(14)

    val firstTest = suiteResult.testResults[0]
    assertThat(firstTest.time).isEqualTo(0.026)
    assertThat(firstTest.outcome).isEqualTo(TestOutcome.PASSED)
    assertThat(firstTest.className).isEqualTo("com.jraska.github.client.AppSetupTest")
    assertThat(firstTest.methodName).isEqualTo("appCreateEventFired")
    assertThat(firstTest.fullName).isEqualTo("com.jraska.github.client.AppSetupTest#appCreateEventFired")

    val ninthTest = suiteResult.testResults[8]
    assertThat(ninthTest.time).isEqualTo(1.182)
    assertThat(ninthTest.outcome).isEqualTo(TestOutcome.PASSED)
    assertThat(ninthTest.className).isEqualTo("com.jraska.github.client.users.UsersActivityFlowTest")
    assertThat(ninthTest.methodName).isEqualTo("whenStartsThenDisplaysUsers")
    assertThat(ninthTest.fullName).isEqualTo("com.jraska.github.client.users.UsersActivityFlowTest#whenStartsThenDisplaysUsers")
  }

  @Test
  fun whenErrorResultThenParsesCorrectly() {
    val suiteResult = extractor.extract(ERROR_RESULT)

    assertThat(suiteResult.failedCount).isOne()
    assertThat(suiteResult.ignoredCount).isZero()
    assertThat(suiteResult.testsCount).isEqualTo(14)
    assertThat(suiteResult.flakyCount).isZero()
    assertThat(suiteResult.suitePassed).isFalse()
    assertThat(suiteResult.time).isEqualTo(13.846)
    assertThat(suiteResult.testResults).hasSize(14)

    val firstTest = suiteResult.testResults[0]
    assertThat(firstTest.time).isEqualTo(0.0)
    assertThat(firstTest.outcome).isEqualTo(TestOutcome.PASSED)
    assertThat(firstTest.className).isEqualTo("com.jraska.github.client.AppSetupTest")
    assertThat(firstTest.methodName).isEqualTo("appCreateEventFired")
    assertThat(firstTest.failure).isNull()
    assertThat(firstTest.fullName).isEqualTo("com.jraska.github.client.AppSetupTest#appCreateEventFired")


    val failedTest = suiteResult.testResults[7]
    assertThat(failedTest.time).isEqualTo(0.853)
    assertThat(failedTest.outcome).isEqualTo(TestOutcome.FAILED)
    assertThat(failedTest.className).isEqualTo("com.jraska.github.client.settings.SettingsTest")
    assertThat(failedTest.methodName).isEqualTo("whenConsoleClickedThenConsoleOpens")
    assertThat(failedTest.fullName).isEqualTo("com.jraska.github.client.settings.SettingsTest#whenConsoleClickedThenConsoleOpens")
    assertThat(failedTest.failure).startsWith("androidx.test.espresso.NoMatchingViewException: No views in hierarchy found matching: with")
  }

  companion object {
    val SUCCESS_RESULT =
      """
        <testsuite name="" tests="14" failures="0" errors="0" skipped="0" time="15.511" timestamp="2020-11-14T19:39:23" hostname="localhost">
        <properties/>
        <testcase name="appCreateEventFired" classname="com.jraska.github.client.AppSetupTest" time="0.026"/>
        <testcase name="androidSchedulerIsAsync" classname="com.jraska.github.client.AppSetupTest" time="0.0"/>
        <testcase name="timberHasTrees" classname="com.jraska.github.client.AppSetupTest" time="0.0"/>
        <testcase name="whenDetailLinkThenUserDetailActivityDisplayed" classname="com.jraska.github.client.DeepLinkLaunchTest" time="0.88"/>
        <testcase name="whenRepoLinkThenRepoActivityDisplayed" classname="com.jraska.github.client.DeepLinkLaunchTest" time="1.507"/>
        <testcase name="whenUsersLinkThenUsersActivityDisplayed" classname="com.jraska.github.client.DeepLinkLaunchTest" time="0.829"/>
        <testcase name="convertsProperly" classname="com.jraska.github.client.FirebaseEventConverterTest" time="0.025"/>
        <testcase name="whenConsoleClickedThenConsoleOpens" classname="com.jraska.github.client.settings.SettingsTest" time="0.905"/>
        <testcase name="whenStartsThenDisplaysUsers" classname="com.jraska.github.client.users.UsersActivityFlowTest" time="1.182"/>
        <testcase name="whenSettingsThenReportsEvent" classname="com.jraska.github.client.users.UsersActivityFlowTest" time="1.582"/>
        <testcase name="whenAboutThenOpensAbout" classname="com.jraska.github.client.users.UsersActivityFlowTest" time="0.703"/>
        <testcase name="whenRefreshesThenDisplaysOtherUsers" classname="com.jraska.github.client.users.UsersActivityFlowTest" time="1.307"/>
        <testcase name="testPushIntegration_fromSettingsToAbout" classname="com.jraska.github.client.xpush.PushIntegrationTest" time="4.497"/>
        <testcase name="testPushIntegration_fromAboutToSettings" classname="com.jraska.github.client.xpush.PushIntegrationTest" time="0.628"/>
        </testsuite>
      """.trimIndent()

    val ERROR_RESULT = """
      <testsuites>
      <testsuite name="" tests="14" failures="1" errors="0" skipped="0" time="13.846" timestamp="2020-11-14T00:30:59" hostname="localhost">
      <properties/>
      <testcase name="appCreateEventFired" classname="com.jraska.github.client.AppSetupTest" time="0.0"/>
      <testcase name="androidSchedulerIsAsync" classname="com.jraska.github.client.AppSetupTest" time="0.0"/>
      <testcase name="timberHasTrees" classname="com.jraska.github.client.AppSetupTest" time="0.0"/>
      <testcase name="whenDetailLinkThenUserDetailActivityDisplayed" classname="com.jraska.github.client.DeepLinkLaunchTest" time="1.079"/>
      <testcase name="whenRepoLinkThenRepoActivityDisplayed" classname="com.jraska.github.client.DeepLinkLaunchTest" time="1.154"/>
      <testcase name="whenUsersLinkThenUsersActivityDisplayed" classname="com.jraska.github.client.DeepLinkLaunchTest" time="0.63"/>
      <testcase name="convertsProperly" classname="com.jraska.github.client.FirebaseEventConverterTest" time="0.026"/>
      <testcase name="whenConsoleClickedThenConsoleOpens" classname="com.jraska.github.client.settings.SettingsTest" time="0.853">
      <failure>androidx.test.espresso.NoMatchingViewException: No views in hierarchy found matching: with id is.runner.TestExecutor.execute(TestExecutor.java:56) at androidx.test.runner.AndroidJUnitRunner.onStart(AndroidJUnitRunner.java:395) at android.app.Instrumentation      .run(Instrumentation.java:2196) </failure>
      </testcase>
      <testcase name="whenStartsThenDisplaysUsers" classname="com.jraska.github.client.users.UsersActivityFlowTest" time="1.129"/>
      <testcase name="whenSettingsThenReportsEvent" classname="com.jraska.github.client.users.UsersActivityFlowTest" time="1.552"/>
      <testcase name="whenAboutThenOpensAbout" classname="com.jraska.github.client.users.UsersActivityFlowTest" time="0.711"/>
      <testcase name="whenRefreshesThenDisplaysOtherUsers" classname="com.jraska.github.client.users.UsersActivityFlowTest" time="1.003"/>
      <testcase name="testPushIntegration_fromSettingsToAbout" classname="com.jraska.github.client.xpush.PushIntegrationTest" time="3.308"/>
      <testcase name="testPushIntegration_fromAboutToSettings" classname="com.jraska.github.client.xpush.PushIntegrationTest" time="1.104"/>
      </testsuite>
      </testsuites>
    """.trimIndent()
  }
}
