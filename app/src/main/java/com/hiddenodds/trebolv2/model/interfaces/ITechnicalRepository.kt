package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.domain.data.MapperTechnical
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import io.reactivex.Observable
import java.util.*

interface ITechnicalRepository: IMessagePersistent {
    fun save(technical: MapperTechnical): Observable<TechnicalModel>
    fun saveList(list: ArrayList<MapperTechnical>): Observable<Boolean>
    fun saveListDependentTechnicians(dependentTechnical:
                                     LinkedHashMap<String,
                                             ArrayList<String>>): Observable<Boolean>
    fun getMasterTechnical(code: String, password: String): Observable<TechnicalModel>
    fun deleteNotifications(code: String): Observable<Boolean>
    fun getTechnical(code: String): Observable<TechnicalModel>
}