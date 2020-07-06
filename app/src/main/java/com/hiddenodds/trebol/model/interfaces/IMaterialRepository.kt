package com.hiddenodds.trebol.model.interfaces

import com.hiddenodds.trebol.domain.data.MapperMaterial
import com.hiddenodds.trebol.presentation.model.MaterialModel
import io.reactivex.Observable
import java.util.*


interface IMaterialRepository{
    fun save(material: MapperMaterial): Observable<MaterialModel>
    fun saveList(list: ArrayList<MapperMaterial>): Observable<Boolean>
    fun getList(): Observable<List<MaterialModel>>
    fun deleteList(): Observable<Boolean>
}