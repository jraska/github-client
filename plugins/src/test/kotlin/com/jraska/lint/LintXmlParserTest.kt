package com.jraska.lint

import com.jraska.module.ModuleMetadata
import com.jraska.module.ModuleType
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.Test

class LintXmlParserTest {
  @Test
  fun whenEmptyXmlThenNoResults() {
    val lintXmlParser = LintXmlParser(ModuleMetadata("fakeModule", ModuleType.Implementation))

    val issues = lintXmlParser.parse(EMPTY_LINT_RESULT)

    assertThat(issues).isEmpty()
  }

  @Test
  fun whenXmlWithSeveralIssuesFoundThenReportsCorrectly() {
    val moduleMetadata = ModuleMetadata("app-partial-users", ModuleType.Implementation)
    val lintXmlParser = LintXmlParser(moduleMetadata)

    val issues = lintXmlParser.parse(APP_XML_RESULT)

    assertThat(issues).hasSize(2)

    val expectedFirstIssue = LintIssue(
      id = "AllowBackup",
      metadata = moduleMetadata,
      category = "Security",
      severity = Severity.Warning,
      message = "On SDK version 23 and up, your app data will be automatically backed up and restored on app install. Consider adding the attribute `android:fullBackupContent` to specify an `@xml` resource which confi...",
      priority = 3,
      summary = "AllowBackup/FullBackupContent Problems",
      errorLine = """  <application""",
      location = "app-partial-users/src/main/AndroidManifest.xml"
    )

    assertThat(issues[0]).isEqualTo(expectedFirstIssue)

    val expectedSecondIssue = LintIssue(
      id = "KtxExtensionAvailable",
      metadata = moduleMetadata,
      category = "Productivity",
      severity = Severity.Info,
      message = "Add suffix `-ktx` to enable the Kotlin extensions for this library",
      priority = 4,
      summary = "KTX Extension Available",
      errorLine = "  implementation 'androidx.lifecycle:lifecycle-runtime:2.2.0'",
      location = "app-partial-users/build.gradle"
    )

    assertThat(issues[1]).isEqualTo(expectedSecondIssue)
  }

  @Test
  fun whenXmlWithIssuesHavingLocationFoundThenReportsCorrectly() {
    val moduleMetadata = ModuleMetadata("chrome-custom-tabs", ModuleType.Implementation)
    val lintXmlParser = LintXmlParser(moduleMetadata)

    val issues = lintXmlParser.parse(RESULT_WITH_ERROR_LOCATION)

    assertThat(issues).hasSize(2)

    val expectedFirstIssue = LintIssue(
      id = "ObsoleteLintCustomCheck",
      metadata = moduleMetadata,
      category = "Lint",
      severity = Severity.Warning,
      message = "Lint found an issue registry ...",
      priority = 10,
      summary = "Obsolete custom lint check",
      errorLine = null,
      location = "/Users/josefraska/.gradle/caches/transforms-2/files-2.1/dbff84457265c6e0e53cdf08487bce44/appcompat-1.2.0/jars/lint.jar"
    )

    assertThat(issues[0]).isEqualTo(expectedFirstIssue)

    val expectedSecondIssue = LintIssue(
      id = "GradleDependency",
      metadata = moduleMetadata,
      category = "Correctness",
      severity = Severity.Warning,
      message = "A newer version of androidx.browser:browser than 1.2.0 is available: 1.3.0",
      priority = 4,
      summary = "Obsolete Gradle Dependency",
      errorLine = "  implementation 'androidx.browser:browser:1.2.0'",
      location = "chrome-custom-tabs/build.gradle"
    )

    assertThat(issues[1]).isEqualTo(expectedSecondIssue)
  }

  companion object {
    val EMPTY_LINT_RESULT = """<?xml version="1.0" encoding="UTF-8"?>
<issues format="5" by="lint 4.1.1">

</issues>"""

    val RESULT_WITH_ERROR_LOCATION = """<?xml version="1.0" encoding="UTF-8"?>
<issues format="5" by="lint 4.1.1">

    <issue
        id="ObsoleteLintCustomCheck"
        severity="Warning"
        message="Lint found an issue registry ..."
        category="Lint"
        priority="10"
        summary="Obsolete custom lint check"
        explanation="Lint can be extended with &quot;custom checks&quot;: additional checks implemented by developers and libraries to for example enforce specific API usages required by a library or a company coding style guideline.&#xA;&#xA;The Lint APIs are not yet stable, so these checks may either cause a performance degradation, or stop working, or provide wrong results.&#xA;&#xA;This warning flags custom lint checks that are found to be using obsolete APIs and will need to be updated to run in the current lint environment.&#xA;&#xA;It may also flag issues found to be using a **newer** version of the API, meaning that you need to use a newer version of lint (or Android Studio or Gradle plugin etc) to work with these checks."
        includedVariants="debug"
        excludedVariants="release">
        <location
            file="/Users/josefraska/.gradle/caches/transforms-2/files-2.1/dbff84457265c6e0e53cdf08487bce44/appcompat-1.2.0/jars/lint.jar"/>
    </issue>

    <issue
        id="GradleDependency"
        severity="Warning"
        message="A newer version of androidx.browser:browser than 1.2.0 is available: 1.3.0"
        category="Correctness"
        priority="4"
        summary="Obsolete Gradle Dependency"
        explanation="This detector looks for usages of libraries where the version you are using is not the current stable release. Using older versions is fine, and there are cases where you deliberately want to stick with an older version. However, you may simply not be aware that a more recent version is available, and that is what this lint check helps find."
        errorLine1="  implementation &apos;androidx.browser:browser:1.2.0&apos;"
        errorLine2="  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~">
        <location
            file="/Users/josefraska/projects/GitHub/mygithub-client/feature/chrome-custom-tabs/build.gradle"
            line="23"
            column="3"/>
    </issue>

</issues>
"""

    val APP_XML_RESULT = """<?xml version="1.0" encoding="UTF-8"?>
<issues format="5" by="lint 4.1.1">

    <issue
        id="AllowBackup"
        severity="Warning"
        message="On SDK version 23 and up, your app data will be automatically backed up and restored on app install. Consider adding the attribute `android:fullBackupContent` to specify an `@xml` resource which configures which files to backup. More info: https://developer.android.com/training/backup/autosyncapi.html"
        category="Security"
        priority="3"
        summary="AllowBackup/FullBackupContent Problems"
        explanation="The `allowBackup` attribute determines if an application&apos;s data can be backed up and restored. It is documented at https://developer.android.com/reference/android/R.attr.html#allowBackup&#xA;&#xA;By default, this flag is set to `true` which means application data can be backed up and restored by the OS. Setting `allowBackup=&quot;false&quot;` opts the application out of being backed up and so users can&apos;t restore data related to it when they go through the device setup wizard.&#xA;&#xA;Allowing backups may have security consequences for an application. Currently `adb backup` allows users who have enabled USB debugging to copy application data off of the device. Once backed up, all application data can be read by the user. `adb restore` allows creation of application data from a source specified by the user. Following a restore, applications should not assume that the data, file permissions, and directory permissions were created by the application itself.&#xA;&#xA;To fix this warning, decide whether your application should support backup, and explicitly set `android:allowBackup=(true|false)&quot;`.&#xA;&#xA;If not set to false, and if targeting API 23 or later, lint will also warn that you should set `android:fullBackupContent` to configure auto backup."
        url="https://developer.android.com/training/backup/autosyncapi.html"
        urls="https://developer.android.com/training/backup/autosyncapi.html,https://developer.android.com/reference/android/R.attr.html#allowBackup"
        errorLine1="  &lt;application"
        errorLine2="   ~~~~~~~~~~~">
        <location
            file="/Users/josefraska/projects/GitHub/mygithub-client/app-partial-users/src/main/AndroidManifest.xml"
            line="9"
            column="4"/>
    </issue>

    <issue
        id="KtxExtensionAvailable"
        severity="Information"
        message="Add suffix `-ktx` to enable the Kotlin extensions for this library"
        category="Productivity"
        priority="4"
        summary="KTX Extension Available"
        explanation="Android KTX extensions augment some libraries with support for modern Kotlin language features like extension functions, extension properties, lambdas, named parameters, coroutines, and more.&#xA;&#xA;In Kotlin projects, use the KTX version of a library by replacing the dependency in your `build.gradle` file. For example, you can replace `androidx.fragment:fragment` with `androidx.fragment:fragment-ktx`."
        url="https://developer.android.com/kotlin/ktx"
        urls="https://developer.android.com/kotlin/ktx"
        errorLine1="  implementation &apos;androidx.lifecycle:lifecycle-runtime:2.2.0&apos;"
        errorLine2="  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~">
        <location
            file="/Users/josefraska/projects/GitHub/mygithub-client/app-partial-users/build.gradle"
            line="48"
            column="3"/>
    </issue>

</issues>
"""
  }
}
