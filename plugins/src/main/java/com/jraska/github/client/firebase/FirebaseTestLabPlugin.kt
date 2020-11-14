package com.jraska.github.client.firebase

import com.jraska.github.client.firebase.report.ConsoleTestResultReporter
import com.jraska.github.client.firebase.report.FirebaseResultExtractor
import com.jraska.github.client.firebase.report.MixpanelTestResultsReporter
import com.jraska.gradle.git.GitInfoProvider
import com.mixpanel.mixpanelapi.MixpanelAPI
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.process.ExecResult
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FirebaseTestLabPlugin : Plugin<Project> {
  override fun apply(theProject: Project) {
    theProject.afterEvaluate { project ->

      var resultsFileToPull: String? = null
      lateinit var deviceExecuted: String

      val executeTestsInTestLab = project.tasks.register("executeInstrumentedTestsOnFirebase", Exec::class.java) { firebaseTask ->
        firebaseTask.doFirst {
          project.exec("gcloud config set project github-client-25b47")
          val credentialsPath = project.createCredentialsFile()
          project.exec("gcloud auth activate-service-account --key-file $credentialsPath")
        }

        val appApk = "${project.buildDir}/outputs/apk/debug/app-debug.apk"
        val testApk = "${project.buildDir}/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
        val deviceName = "flame"
        val androidVersion = "29"
        val device = "model=$deviceName,version=$androidVersion,locale=en,orientation=portrait"
        val resultDir = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())
        deviceExecuted = device

        val fcmKey = System.getenv("FCM_API_KEY")

        resultsFileToPull = "gs://test-lab-twsawhz0hy5am-h35y3vymzadax/$resultDir/$deviceName-$androidVersion-en-portrait/test_result_1.xml"

        firebaseTask.commandLine =
          ("gcloud " +
            "firebase test android run " +
            "--app $appApk " +
            "--test $testApk " +
            "--device $device " +
            "--results-dir $resultDir " +
            "--no-performance-metrics " +
            "--environment-variables FCM_API_KEY=$fcmKey")
            .split(' ')
        firebaseTask.isIgnoreExitValue = true

        firebaseTask.dependsOn(project.tasks.named("assembleDebugAndroidTest"))
        firebaseTask.dependsOn(project.tasks.named("assembleDebug"))
      }

      val reportResults = project.tasks.register("reportFirebaseXmlResults", Exec::class.java) { pullTask ->
        pullTask.dependsOn(executeTestsInTestLab)

        val outputFile = "${project.buildDir}/test-results/firebase-tests-results.xml"
        pullTask.doFirst {
          pullTask.commandLine = "gsutil cp $resultsFileToPull $outputFile".split(' ')
        }

        pullTask.doLast {
          val result = FirebaseResultExtractor("noUrlYet", GitInfoProvider.gitInfo(project), deviceExecuted).extract(File(outputFile).readText())
          val mixpanelToken: String? = System.getenv("GITHUB_CLIENT_MIXPANEL_API_KEY")
          val reporter = if (mixpanelToken == null) {
            println("'GITHUB_CLIENT_MIXPANEL_API_KEY' not set, data will be reported to console only")
            ConsoleTestResultReporter()
          } else {
            MixpanelTestResultsReporter(mixpanelToken, MixpanelAPI())
          }

          reporter.report(result)
        }
      }

      project.tasks.register("runInstrumentedTestsOnFirebase") {
        it.dependsOn(executeTestsInTestLab)
        it.dependsOn(reportResults)

        it.doLast {
          if (executeTestsInTestLab.isPresent) {
            val execResult = executeTestsInTestLab.get().execResult!!
            if (execResult.exitValue != 0) {
              execResult.rethrowFailure()
            }
          }
        }
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
