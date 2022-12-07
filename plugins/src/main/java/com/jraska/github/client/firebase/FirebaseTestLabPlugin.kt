package com.jraska.github.client.firebase

import com.jraska.analytics.AnalyticsReporter
import com.jraska.github.client.firebase.report.FirebaseResultExtractor
import com.jraska.github.client.firebase.report.FirebaseUrlParser
import com.jraska.github.client.firebase.report.TestResultsReporter
import com.jraska.gradle.CiInfo
import com.jraska.gradle.git.GitInfoProvider
import org.apache.tools.ant.util.TeeOutputStream
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.process.ExecResult
import java.io.ByteArrayOutputStream
import java.io.File

class FirebaseTestLabPlugin : Plugin<Project> {
  override fun apply(theProject: Project) {
    theProject.afterEvaluate { project ->

      project.tasks.register("runInstrumentedTestsOnFirebase", Exec::class.java) { firebaseTask ->
        firebaseTask.doFirst {
          project.exec("gcloud config set project github-client-25b47")
          val credentialsPath = project.createCredentialsFile()
          project.exec("gcloud auth activate-service-account --key-file $credentialsPath")
        }

        val testConfiguration = TestConfiguration.create(project)

        val envVars = mapOf("FCM_API_KEY" to System.getenv("FCM_API_KEY"))
        firebaseTask.commandLine = GCloudCommands.firebaseRunCommand(testConfiguration, envVars).split(' ')
        firebaseTask.isIgnoreExitValue = true

        val decorativeStream = ByteArrayOutputStream()
        firebaseTask.errorOutput = TeeOutputStream(decorativeStream, System.err)

        firebaseTask.doLast {
          val firebaseUrl = FirebaseUrlParser.parse(decorativeStream.toString())

          val firstResult = testSuiteResult(project, testConfiguration.firstDevice, firebaseUrl, testConfiguration.resultDir)
          val secondResult = testSuiteResult(project, testConfiguration.secondDevice, firebaseUrl, testConfiguration.resultDir)

          val reporter = TestResultsReporter(AnalyticsReporter.create("Test Reporter"))

          reporter.report(firstResult)
          reporter.report(secondResult)
          firebaseTask.executionResult.get().assertNormalExitValue()
        }

        firebaseTask.dependsOn(project.tasks.named("assembleDebugAndroidTest"))
        firebaseTask.dependsOn(project.tasks.named("assembleDebug"))
      }
    }
  }

  private fun testSuiteResult(
    project: Project,
    device: Device,
    firebaseUrl: String,
    resultDir: String
  ): TestSuiteResult {
    val firstOutputFile =
      "${project.buildDir}/test-results/${device.cloudStoragePath()}/firebase-tests-results.xml"
    val firstResultsFileToPull =
      "gs://test-lab-twsawhz0hy5am-h35y3vymzadax/$resultDir/${device.cloudStoragePath()}/test_result_1.xml"
    project.exec("gsutil cp $firstResultsFileToPull $firstOutputFile")

    return FirebaseResultExtractor(
      firebaseUrl,
      GitInfoProvider.gitInfo(project),
      CiInfo.collectGitHubActions(),
      device,
    ).extract(File(firstOutputFile).readText())
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
