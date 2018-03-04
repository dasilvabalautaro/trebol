package com.hiddenodds.trebolv2.domain.interfaces

import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import io.reactivex.Observable

interface IHearMessage {
    fun hearMessage(): Observable<String>
    fun hearError(): Observable<DatabaseOperationException>
}