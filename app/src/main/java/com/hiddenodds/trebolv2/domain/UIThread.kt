package com.hiddenodds.trebolv2.domain

import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UIThread @Inject constructor() : IPostExecutionThread {
    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()

}