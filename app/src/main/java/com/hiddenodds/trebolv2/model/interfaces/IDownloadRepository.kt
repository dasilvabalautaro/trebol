package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.domain.data.MapperDownload
import io.reactivex.Observable
import java.util.*

interface IDownloadRepository {
    fun create(list: ArrayList<MapperDownload>): Observable<Boolean>
    fun update(code: String, fieldName: String, value: String): Observable<Boolean>
    fun delete(code: String):
            Observable<Boolean>

}