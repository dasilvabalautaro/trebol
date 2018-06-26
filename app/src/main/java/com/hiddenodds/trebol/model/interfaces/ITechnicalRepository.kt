package com.hiddenodds.trebol.model.interfaces

import com.hiddenodds.trebol.domain.data.MapperTechnical
import com.hiddenodds.trebol.presentation.model.TechnicalModel
import io.reactivex.Observable
import java.util.*

interface ITechnicalRepository{
    fun save(technical: MapperTechnical): Observable<TechnicalModel>
    fun saveList(listTech: ArrayList<MapperTechnical>): Observable<Boolean>
    fun saveListDependentTechnicians(dependentTechnical:
                                     LinkedHashMap<String,
                                             ArrayList<String>>): Observable<Boolean>
    fun getMasterTechnical(code: String, password: String): Observable<TechnicalModel>
    fun deleteNotifications(code: String): Observable<Boolean>
    fun getTechnical(code: String): Observable<TechnicalModel>
}