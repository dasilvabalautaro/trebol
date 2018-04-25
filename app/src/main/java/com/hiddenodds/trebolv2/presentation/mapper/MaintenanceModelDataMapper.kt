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
        maintenanceModel.maintenance1 = maintenance.maintenance1
        maintenanceModel.maintenance2 = maintenance.maintenance2
        maintenanceModel.maintenance3 = maintenance.maintenance3
        maintenanceModel.maintenance4 = maintenance.maintenance4
        maintenanceModel.maintenance5 = maintenance.maintenance5
        maintenanceModel.maintenance6 = maintenance.maintenance6
        maintenanceModel.maintenance7 = maintenance.maintenance7
        maintenanceModel.maintenance8 = maintenance.maintenance8
        maintenanceModel.maintenance9 = maintenance.maintenance9
        maintenanceModel.maintenance10 = maintenance.maintenance10
        maintenanceModel.maintenance11 = maintenance.maintenance11
        maintenanceModel.maintenance12 = maintenance.maintenance12
        maintenanceModel.maintenance13 = maintenance.maintenance13
        maintenanceModel.maintenance14 = maintenance.maintenance14
        maintenanceModel.test1 = maintenance.test1
        maintenanceModel.test2 = maintenance.test2
        maintenanceModel.test3 = maintenance.test3
        maintenanceModel.test4 = maintenance.test4
        maintenanceModel.test5 = maintenance.test5
        maintenanceModel.test6 = maintenance.test6
        maintenanceModel.test7 = maintenance.test7
        maintenanceModel.test8 = maintenance.test8
        maintenanceModel.test9 = maintenance.test9
        maintenanceModel.test10 = maintenance.test10
        maintenanceModel.test11 = maintenance.test11
        maintenanceModel.test12 = maintenance.test12
        maintenanceModel.test13 = maintenance.test13
        maintenanceModel.test14 = maintenance.test14
        maintenanceModel.test15 = maintenance.test15
        maintenanceModel.test16 = maintenance.test16
        maintenanceModel.security = maintenance.security
        maintenanceModel.documentation = maintenance.documentation
        maintenanceModel.knowPrint = maintenance.knowPrint
        maintenanceModel.nextHours = maintenance.nextHours
        maintenanceModel.reportTechnical = maintenance.reportTechnical
        return maintenanceModel
    }

    fun transform(maintenanceCollection: Collection<Maintenance>?): Collection<MaintenanceModel>{
        val maintenanceModelCollection: MutableCollection<MaintenanceModel> = ArrayList()

        if (maintenanceCollection != null && !maintenanceCollection.isEmpty())
            maintenanceCollection.mapTo(maintenanceModelCollection) { transform(it) }
        return maintenanceModelCollection
    }
    
}