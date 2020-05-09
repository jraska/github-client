project.afterEvaluate {
  val gcloud = "gcloud"
  val setupGCloudProject = tasks.register("setupGCloudProject", Exec::class) {
    commandLine = "$gcloud config set project github-client-25b47".split(' ')
  }

  val setupGCloudAccount = tasks.register("setupGCloudAccount", Exec::class) {
    val credentialsPath = createCredentialsFile()
    commandLine = "$gcloud auth activate-service-account --key-file $credentialsPath".split(' ')

    dependsOn(setupGCloudProject)
  }

  val firebaseTestsTask = tasks.register("runInstrumentedTestsOnFirebase", Exec::class) {
    val appApk = "${project.buildDir}/outputs/apk/debug/app-debug.apk"
    val testApk = "${project.buildDir}/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
    val device = "model=Pixel2,version=29,locale=en,orientation=portrait"

    commandLine =
      ("$gcloud " +
        "firebase test android run " +
        "--app $appApk " +
        "--test $testApk " +
        "--device $device " +
        "--no-performance-metrics")
        .split(' ')
    dependsOn(project.tasks.named("assembleDebugAndroidTest"))
    dependsOn(setupGCloudAccount)
  }

  project.tasks.named("check").configure { dependsOn(firebaseTestsTask) }
}

fun Project.createCredentialsFile(): String {
  val credentialsPath = "$projectDir/credentials.json"
  val credentials: String = System.getenv("GCLOUD_CREDENTIALS")
  File(credentialsPath).writeText(credentials)
  return credentialsPath
}

