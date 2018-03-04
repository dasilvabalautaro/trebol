package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.domain.data.MapperMaterial
import com.hiddenodds.trebolv2.presentation.model.MaterialModel
import io.reactivex.Observable
import java.util.*


interface IMaterialRepository: IMessagePersistent {
    fun save(material: MapperMaterial): Observable<MaterialModel>
    fun saveList(list: ArrayList<MapperMaterial>): Observable<Boolean>
    fun getList(): Observable<List<MaterialModel>>
    fun deleteList(): Observable<Boolean>
}