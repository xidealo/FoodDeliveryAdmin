package com.bunbeauty.domain

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class MockLogRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                mockkStatic(Log::class)
                every { Log.e(any(), any()) } returns 0
                every { Log.e(any(), any(), any()) } returns 0
                every { Log.d(any(), any()) } returns 0
                every { Log.i(any(), any()) } returns 0
                every { Log.v(any(), any()) } returns 0
                try {
                    base.evaluate()
                } finally {
                    // Clean up if needed
                }
            }
        }
    }
}
