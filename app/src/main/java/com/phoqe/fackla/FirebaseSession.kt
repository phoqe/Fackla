package com.phoqe.fackla

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.ktx.performance

class FirebaseSession {
    val analytics: FirebaseAnalytics = Firebase.analytics
    val crashlytics: FirebaseCrashlytics = Firebase.crashlytics
    val performance: FirebasePerformance = Firebase.performance

    init {
        analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        performance.isPerformanceCollectionEnabled = !BuildConfig.DEBUG
    }
}