# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html


# This to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable
# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile
#-dontobfuscate

# https://github.com/square/retrofit/issues/3005#issuecomment-1523948650
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
