package com.hiddenodds.trebol.model.interfaces

import com.hiddenodds.trebol.model.exception.DatabaseOperationException
import io.reactivex.Observable

interface IMessagePersistent {
    fun userGetMessage(): Observable<String>
    fun userGetError(): Observable<DatabaseOperationException>
}