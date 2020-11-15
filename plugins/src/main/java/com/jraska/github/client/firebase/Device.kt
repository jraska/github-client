package com.jraska.github.client.firebase

class Device(
  val model: String,
  val apiLevel: Int,
  val locale: String,
  val orientation: String
) {
  fun firebaseCommandString(): String {
    return "model=$model,version=$apiLevel,locale=$locale,orientation=$orientation"
  }

  fun cloudStoragePath(): String {
    return "$model-$apiLevel-$locale-$orientation"
  }

  companion object {
    val Pixel4 = Device("flame", 29, "en", "portrait")
    val LowResNexus = Device("Pixel2", 29, "en", "portrait")
  }
}
