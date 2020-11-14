package com.jraska.github.client.firebase.report

object FirebaseUrlParser {
  private val urlPattern = """Test results will be streamed to \[(\S*)\]""".toPattern()

  fun parse(output: String): String {
    val matcher = urlPattern.matcher(output)

    matcher.find()
    return matcher.group(1)
  }
}
