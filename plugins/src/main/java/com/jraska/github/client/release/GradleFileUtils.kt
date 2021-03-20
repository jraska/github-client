package com.jraska.github.client.release

import java.util.regex.Pattern

object GradleFileUtils {
  fun versionName(gradleFileText: String): String {
    val versionNamePattern = Pattern.compile("""versionName '([0-9]*\.[0-9]*\.[0-9]*)'""")
    val versionNameMatcher = versionNamePattern.matcher(gradleFileText)
    val found = versionNameMatcher.find()
    if (!found) {
      throw IllegalStateException("No match found for $versionNamePattern")
    }

    return versionNameMatcher.group(1)
  }

  fun incrementVersionCode(gradleFileText: String): String {
    val versionCodePattern = Pattern.compile("""versionCode ([0-9]*)""")
    val versionCodeMatcher = versionCodePattern.matcher(gradleFileText)
    val found = versionCodeMatcher.find()
    if (!found) {
      throw IllegalStateException("No match found for $versionCodePattern")
    }

    val oldVersionCode = versionCodeMatcher.group(1).toLong()
    val newVersionCode = oldVersionCode + 1

    return gradleFileText.replace(versionCodeMatcher.group(0), "versionCode $newVersionCode")
  }

  fun incrementVersionNamePatch(gradleFileText: String): String {
    val versionNamePattern = Pattern.compile("""versionName '([0-9]*)\.([0-9]*)\.([0-9]*)'""")
    val versionNameMatcher = versionNamePattern.matcher(gradleFileText)
    val found = versionNameMatcher.find()
    if (!found) {
      throw IllegalStateException("No match found for $versionNamePattern")
    }

    val oldPatch = versionNameMatcher.group(3).toLong()
    val newPatch = oldPatch + 1

    val newText = "versionName '${versionNameMatcher.group(1)}.${versionNameMatcher.group(2)}.$newPatch'"

    return gradleFileText.replace(versionNameMatcher.group(0), newText)
  }
}
