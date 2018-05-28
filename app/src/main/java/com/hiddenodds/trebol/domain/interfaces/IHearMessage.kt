package com.hiddenodds.trebol.domain.interfaces

import com.hiddenodds.trebol.model.exception.DatabaseOperationException
import io.reactivex.Observable

interface IHearMessage {
    fun hearMessage(): Observable<String>
    fun hearError(): Observable<DatabaseOperationException>
}