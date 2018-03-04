package com.hiddenodds.trebolv2.model.interfaces

import io.reactivex.Scheduler

interface IPostExecutionThread {
    val scheduler: Scheduler
}