package com.example.vinciandroidv2.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

interface LifecycleObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener()
}