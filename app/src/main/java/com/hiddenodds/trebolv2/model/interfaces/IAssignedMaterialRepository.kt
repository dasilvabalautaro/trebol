package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.presentation.model.AssignedMaterialModel
import io.reactivex.Observable


interface IAssignedMaterialRepository {
    fun save(assigned: AssignedMaterialModel): Observable<AssignedMaterialModel>
    fun saveList(list: ArrayList<AssignedMaterialModel>):
            ArrayList<AssignedMaterialModel>
    fun addListAssignedMaterialToNotification(list: ArrayList<AssignedMaterialModel>,
                                              id: String, flagUse: Boolean):
            Observable<Boolean>
    fun updatedAssigned(id: String, quantity: Int): Observable<Boolean>
    fun deleteAssignedMaterial(idNotification: String,
                               idAssigned: String,
                               flagUse: Boolean): Observable<Boolean>
}