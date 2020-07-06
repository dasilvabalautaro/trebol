package com.hiddenodds.trebol.model.interfaces

import io.reactivex.Scheduler

interface IPostExecutionThread {
    val scheduler: Scheduler
}