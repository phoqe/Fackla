package com.phoqe.fackla

import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.ktx.performance

class FirebaseSession {
    private val analytics: FirebaseAnalytics = Firebase.analytics
    private val crashlytics: FirebaseCrashlytics = Firebase.crashlytics
    private val performance: FirebasePerformance = Firebase.performance
    private val appCheck: FirebaseAppCheck = FirebaseAppCheck.getInstance()

    init {
        analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        performance.isPerformanceCollectionEnabled = !BuildConfig.DEBUG
        appCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())
    }
}