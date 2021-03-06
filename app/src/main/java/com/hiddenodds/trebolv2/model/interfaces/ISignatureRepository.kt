package com.hiddenodds.trebolv2.model.interfaces

import io.reactivex.Observable

interface ISignatureRepository {
    fun getSignature(name: String): Observable<String>
}