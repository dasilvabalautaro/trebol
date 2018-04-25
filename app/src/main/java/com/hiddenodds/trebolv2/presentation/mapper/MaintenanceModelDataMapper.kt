package com.hiddenodds.trebolv2.presentation.mapper

import android.content.Context
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.data.Maintenance
import com.hiddenodds.trebolv2.presentation.model.MaintenanceModel
import javax.inject.Inject


class MaintenanceModelDataMapper @Inject constructor(val context: Context){
    fun transform(maintenance: Maintenance?): MaintenanceModel {
        if (maintenance == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val maintenanceModel = MaintenanceModel()
        maintenanceModel.id = maintenance.id
        maintenanceModel.codeNotify = maintenance.codeNotify
        maintenanceModel.verification1 = maintenance.verification1
        maintenanceModel.verification2 = maintenance.verification2
        maintenanceModel.verification3 = maintenance.verification3
        maintenanceModel.verification4 = maintenance.verification4
        maintenanceModel.verification5 = maintenance.verification5
        maintenanceModel.verification6 = maintenance.verification6
        maintenanceModel.verification7 = maintenance.verification7
        maintenanceModel.verification8 = maintenance.verification8
        maintenanceModel.verification9 = maintenance.verification9
        maintenanceModel.verification10 = maintenance.verification10
        maintenanceModel.verification11 = maintenance.verification11
        maintenanceModel.verification12 = maintenance.verification12
        maintenanceModel.verification13 = maintenance.verification13
        maintenanceModel.verification14 = maintenance.verification14

        return maintenanceModel
    }

    fun transform(maintenanceCollection: Collection<Maintenance>?): Collection<MaintenanceModel>{
        val maintenanceModelCollection: MutableCollection<MaintenanceModel> = ArrayList()

        if (maintenanceCollection != null && !maintenanceCollection.isEmpty())
            maintenanceCollection.mapTo(maintenanceModelCollection) { transform(it) }
        return maintenanceModelCollection
    }
    
}