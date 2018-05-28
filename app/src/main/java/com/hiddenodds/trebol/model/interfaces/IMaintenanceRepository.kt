package com.hiddenodds.trebol.model.interfaces

import com.hiddenodds.trebol.presentation.model.MaintenanceModel
import io.reactivex.Observable


interface IMaintenanceRepository {
    fun save(): Observable<MaintenanceModel>
    fun getMaintenance(codeNotify: String): Observable<MaintenanceModel>
    fun update(id: String, nameFieldUpdate: String,
               newValue: String): Observable<Boolean>
    fun delete(codeNotify: String): Observable<Boolean>
}