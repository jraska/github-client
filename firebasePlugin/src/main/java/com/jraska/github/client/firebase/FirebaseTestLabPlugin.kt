package com.jraska.github.client.firebase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FirebaseTestLabPlugin : Plugin<Project> {
  override fun apply(theProject: Project) {
    theProject.afterEvaluate { project ->
      val setupGCloudProject = project.tasks.register("setupGCloudProject", Exec::class.java) {
        it.commandLine = "gcloud config set project github-client-25b47".split(' ')
        it.dependsOn(project.tasks.named("assembleDebugAndroidTest"))
      }

      val setupGCloudAccount = project.tasks.register("setupGCloudAccount", Exec::class.java) {
        val credentialsPath = project.createCredentialsFile()
        it.commandLine = "gcloud auth activate-service-account --key-file $credentialsPath".split(' ')

        it.dependsOn(setupGCloudProject)
      }

      var resultsFileToPull: String? = null

      val executeTestsInTestLab = project.tasks.register("executeInstrumentedTestsOnFirebase", Exec::class.java) {
        val appApk = "${project.buildDir}/outputs/apk/debug/app-debug.apk"
        val testApk = "${project.buildDir}/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
        val deviceName = "flame"
        val androidVersion = "29"
        val device = "model=$deviceName,version=$androidVersion,locale=en,orientation=portrait"
        val resultDir = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())

        val fcmKey = System.getenv("FCM_API_KEY")

        resultsFileToPull = "gs://test-lab-twsawhz0hy5am-h35y3vymzadax/$resultDir/$deviceName-$androidVersion-en-portrait/test_result_1.xml"

        it.commandLine =
          ("gcloud " +
            "firebase test android run " +
            "--app $appApk " +
            "--test $testApk " +
            "--device $device " +
            "--results-dir $resultDir " +
            "--no-performance-metrics " +
            "--environment-variables FCM_API_KEY=$fcmKey")
            .split(' ')

        it.dependsOn(project.tasks.named("assembleDebugAndroidTest"))
        it.dependsOn(project.tasks.named("assembleDebug"))
        it.dependsOn(setupGCloudAccount)
      }

      val pullResults = project.tasks.register("pullFirebaseXmlResults", Exec::class.java) { task ->
        task.dependsOn(executeTestsInTestLab)

        task.doFirst {
          task.commandLine = "gsutil cp $resultsFileToPull ${project.buildDir}/test-results/firebase-tests-results.xml".split(' ')
        }
      }

      project.tasks.register("runInstrumentedTestsOnFirebase") {
        it.dependsOn(executeTestsInTestLab)
        it.dependsOn(pullResults)
      }
    }
  }

  fun Project.createCredentialsFile(): String {
    val credentialsPath = "$buildDir/credentials.json"
    val credentials = System.getenv("GCLOUD_CREDENTIALS")
    if (credentials != null) {
      File(credentialsPath).writeText(credentials)
    }
    return credentialsPath
  }
}
