package com.hiddenodds.trebol.model.interfaces

import io.reactivex.Observable

interface ISignatureRepository {
    fun getSignature(name: String): Observable<String>
}