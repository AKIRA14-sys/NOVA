# GeckoView Native JNI Keep Rules
-keep class org.mozilla.geckoview.** { *; }
-keep interface org.mozilla.geckoview.** { *; }
-keepclassmembers class org.mozilla.geckoview.** { *; }
-keepclassmembernames class org.mozilla.geckoview.** { *; }

-keep class org.mozilla.gecko.** { *; }
-keep interface org.mozilla.gecko.** { *; }
-keepclassmembers class org.mozilla.gecko.** { *; }
-keepclassmembernames class org.mozilla.gecko.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable

# Jetpack Compose & Material3 Keep Rules
-keep class androidx.compose.** { *; }
-keep interface androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.material.** { *; }
-keep class androidx.compose.ui.text.input.** { *; }
-keep class androidx.compose.foundation.text.** { *; }

# Optional dependency warnings
-dontwarn java.beans.**
-dontwarn java.awt.**
-dontwarn org.mozilla.geckoview.**
