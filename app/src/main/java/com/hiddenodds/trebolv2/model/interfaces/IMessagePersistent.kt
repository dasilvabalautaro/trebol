package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.model.exception.DatabaseOperationException
import io.reactivex.Observable

interface IMessagePersistent {
    fun userGetMessage(): Observable<String>
    fun userGetError(): Observable<DatabaseOperationException>
}