# NOVA Browser

NOVA Browser is a native Android web browser application built with Kotlin, Jetpack Compose, and Mozilla GeckoView. Designed with a futuristic cyberpunk aesthetic (neon cyan and deep purple theme), NOVA Browser combines Samsung Internet-inspired usability with privacy-focused browser engine controls.

---

## Features (Milestone 1)
- **Browser Engine:** Mozilla GeckoView (`org.mozilla.geckoview:geckoview-omni`).
- **Real Web Browsing:** Navigates to actual websites, handles URLs, domain entries, and search queries.
- **Flexible Search Providers:** Multi-provider architecture distinguishing **General Search Engines** (Google, Bing, Yahoo, DuckDuckGo, Brave) from **Specialized Destinations** (Wikipedia, YouTube, GitHub, Google Scholar). Default provider: **Google**.
- **Full Navigation Controls:** Back, Forward, Refresh, and Home actions.
- **Progress & Error Indicators:** Top loading progress bar and load error alerts.
- **Multi-Tab Support:** Create, switch, and close browser tabs with Compose bottom sheet manager.
- **Cyberpunk UI:** Custom theme with Neon Cyan, Deep Purple, and Neon Pink accents.
- **ARM64 Optimized Build:** Native library filtering for `arm64-v8a` reducing APK size from 575 MB to ~60 MB.

---

## Building Locally

### Prerequisites
- JDK 17
- Android SDK (API 34)
- Gradle 8.8 (using Gradle Wrapper `./gradlew`)

### Build Commands
- **Run Unit Tests:**
  ```bash
  ./gradlew test
  ```
- **Build Debug APK:**
  ```bash
  ./gradlew assembleDebug
  ```
- **Build Optimized ARM64 Release APK:**
  ```bash
  ./gradlew assembleRelease
  ```
  Output APK location: `app/build/outputs/apk/release/app-release-unsigned.apk` (or `app-release.apk`)

---

## Automated GitHub Actions Builds

A GitHub Actions workflow is configured at `.github/workflows/android-apk.yml`.

### How to Trigger a Build
1. **Push to `main` branch:** Every commit pushed to `main` automatically triggers a build and uploads the optimized ARM64 APK as an artifact.
2. **Manual Trigger (`workflow_dispatch`):**
   - Go to your repository on GitHub.
   - Click the **Actions** tab.
   - Select **Build NOVA Browser ARM64 APK**.
   - Click **Run workflow**.
3. **Tagged Release:**
   - Push a version tag e.g. `git tag v0.1.0 && git push origin v0.1.0`.
   - The workflow will build the APK and automatically publish a GitHub Release with the APK attached.

### Downloading the APK from GitHub Actions
1. Navigate to the **Actions** tab on GitHub.
2. Click on the latest successful workflow run.
3. Scroll down to the **Artifacts** section.
4. Click on `nova-browser-arm64-release` to download the zip file containing the APK.

### Downloading published APKs from GitHub Releases
1. Go to the GitHub repository homepage.
2. Click **Releases** on the right sidebar (or navigate to `/releases`).
3. Under the published tag (e.g. `v0.1.0`), download the attached `.apk` file under **Assets**.

---

## Installing on Android

1. Transfer the `.apk` file to your Android phone (via USB, Google Drive, or browser download).
2. Enable **Install Unknown Apps** in your phone's Settings for the File Manager or Browser.
3. Open the `.apk` file on your phone and tap **Install**.
4. Open **NOVA Browser** from your app drawer.
