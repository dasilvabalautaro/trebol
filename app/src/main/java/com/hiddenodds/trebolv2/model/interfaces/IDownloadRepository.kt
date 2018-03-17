package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.domain.data.MapperDownload
import com.hiddenodds.trebolv2.presentation.model.DownloadModel
import io.reactivex.Observable

interface IDownloadRepository {
    fun create(list: ArrayList<MapperDownload>): Observable<Boolean>
    fun update(code: String, fieldName: String, value: String): Observable<Boolean>
    fun delete(list: ArrayList<String>):
            Observable<Boolean>
    fun getDownload(code: String): Observable<DownloadModel>
}