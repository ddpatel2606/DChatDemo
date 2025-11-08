package com.dixitpatel.dchatdemo

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * A custom [AndroidJUnitRunner] for Hilt instrumented tests.
 *
 * This runner is essential for Hilt's test support, as it overrides the `newApplication`
 * method to instantiate a [HiltTestApplication] instead of the standard application class.
 * This ensures that Hilt can properly manage dependencies and create test-specific
 * component hierarchies for instrumentation tests.
 *
 * To use this runner, specify it in your app's `build.gradle` file:
 * ```
 * android {
 *     defaultConfig {
 *         testInstrumentationRunner "com.dixitpatel.dchatdemo.HiltTestRunner"
 *     }
 * }
 * ```
 */
class HiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}