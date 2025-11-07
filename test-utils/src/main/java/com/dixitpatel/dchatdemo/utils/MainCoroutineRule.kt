package com.dixitpatel.dchatdemo.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Sets the main coroutines dispatcher to a [TestDispatcher] for unit testing.
 *
 * Declare it as a JUnit Rule:
 *
 * ```
 * @get:Rule
 * val mainCoroutineRule = MainCoroutineRule()
 * ```
 *
 * Then, use `runTest` to execute your tests.
 */
@ExperimentalCoroutinesApi
open class MainCoroutineRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
