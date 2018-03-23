package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.domain.data.MapperMaterial
import com.hiddenodds.trebolv2.model.data.Material
import com.hiddenodds.trebolv2.presentation.model.MaterialModel
import io.reactivex.Observable
import io.realm.RealmResults
import java.util.*


interface IMaterialRepository{
    fun save(material: MapperMaterial): Observable<MaterialModel>
    fun saveList(list: ArrayList<MapperMaterial>): Observable<Boolean>
    fun getList(): Observable<RealmResults<Material>>
    fun deleteList(): Observable<Boolean>
}