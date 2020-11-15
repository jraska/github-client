package com.jraska.github.client.firebase

import com.jraska.github.client.firebase.report.ConsoleTestResultReporter
import com.jraska.github.client.firebase.report.FirebaseResultExtractor
import com.jraska.github.client.firebase.report.FirebaseUrlParser
import com.jraska.github.client.firebase.report.MixpanelTestResultsReporter
import com.jraska.gradle.git.GitInfoProvider
import com.mixpanel.mixpanelapi.MixpanelAPI
import org.apache.tools.ant.util.TeeOutputStream
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.process.ExecResult
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FirebaseTestLabPlugin : Plugin<Project> {
  override fun apply(theProject: Project) {
    theProject.afterEvaluate { project ->

      project.tasks.register("runInstrumentedTestsOnFirebase", Exec::class.java) { firebaseTask ->
        firebaseTask.doFirst {
          project.exec("gcloud config set project github-client-25b47")
          val credentialsPath = project.createCredentialsFile()
          project.exec("gcloud auth activate-service-account --key-file $credentialsPath")
        }

        val appApk = "${project.buildDir}/outputs/apk/debug/app-debug.apk"
        val testApk = "${project.buildDir}/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
        val firstDevice = Device.Pixel4
        val secondDevice = Device.Pixel2
        val resultDir = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())

        val fcmKey = System.getenv("FCM_API_KEY")

        firebaseTask.commandLine =
          ("gcloud " +
            "firebase test android run " +
            "--app $appApk " +
            "--test $testApk " +
            "--device ${firstDevice.firebaseCommandString()} " +
            "--device ${secondDevice.firebaseCommandString()} " +
            "--results-dir $resultDir " +
            "--no-performance-metrics " +
            "--timeout 3m " +
            "--environment-variables FCM_API_KEY=$fcmKey")
            .split(' ')
        firebaseTask.isIgnoreExitValue = true

        val decorativeStream = ByteArrayOutputStream()
        firebaseTask.errorOutput = TeeOutputStream(decorativeStream, System.err)

        firebaseTask.doLast {
          val firstOutputFile = "${project.buildDir}/test-results/${firstDevice.cloudStoragePath()}/firebase-tests-results.xml"
          val firstResultsFileToPull = "gs://test-lab-twsawhz0hy5am-h35y3vymzadax/$resultDir/${firstDevice.cloudStoragePath()}/test_result_1.xml"
          project.exec("gsutil cp $firstResultsFileToPull $firstOutputFile")

          val firebaseUrl = FirebaseUrlParser.parse(decorativeStream.toString())
          val firstResult = FirebaseResultExtractor(firebaseUrl, GitInfoProvider.gitInfo(project), firstDevice).extract(File(firstOutputFile).readText())

          val secondOutputFile = "${project.buildDir}/test-results/${secondDevice.cloudStoragePath()}/firebase-tests-results.xml"
          val secondResultsFileToPull = "gs://test-lab-twsawhz0hy5am-h35y3vymzadax/$resultDir/${secondDevice.cloudStoragePath()}/test_result_1.xml"
          project.exec("gsutil cp $secondResultsFileToPull $secondOutputFile")

          val secondResult = FirebaseResultExtractor(firebaseUrl, GitInfoProvider.gitInfo(project), secondDevice).extract(File(secondOutputFile).readText())

          val mixpanelToken: String? = System.getenv("GITHUB_CLIENT_MIXPANEL_API_KEY")
          val reporter = if (mixpanelToken == null) {
            println("'GITHUB_CLIENT_MIXPANEL_API_KEY' not set, data will be reported to console only")
            ConsoleTestResultReporter()
          } else {
            MixpanelTestResultsReporter(mixpanelToken, MixpanelAPI())
          }

          reporter.report(firstResult)
//          reporter.report(secondResult)
          firebaseTask.execResult!!.assertNormalExitValue()
        }

        firebaseTask.dependsOn(project.tasks.named("assembleDebugAndroidTest"))
        firebaseTask.dependsOn(project.tasks.named("assembleDebug"))
      }
    }
  }

  private fun Project.exec(command: String): ExecResult {
    return exec {
      it.commandLine(command.split(" "))
    }
  }

  private fun Project.createCredentialsFile(): String {
    val credentialsPath = "$buildDir/credentials.json"
    val credentials = System.getenv("GCLOUD_CREDENTIALS")
    if (credentials != null) {
      File(credentialsPath).writeText(credentials)
    }
    return credentialsPath
  }
}
