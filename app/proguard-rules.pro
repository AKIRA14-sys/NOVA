# GeckoView ProGuard rules
-keep class org.mozilla.geckoview.** { *; }
-dontwarn org.mozilla.geckoview.**

# Jetpack Compose ProGuard rules
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# SnakeYAML / optional dependencies
-dontwarn java.beans.**
-dontwarn java.awt.**
