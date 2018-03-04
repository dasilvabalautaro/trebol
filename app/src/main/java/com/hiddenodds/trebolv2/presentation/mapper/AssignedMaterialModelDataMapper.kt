package com.hiddenodds.trebolv2.presentation.mapper

import android.content.Context
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.data.AssignedMaterial
import com.hiddenodds.trebolv2.presentation.model.AssignedMaterialModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignedMaterialModelDataMapper @Inject 
        constructor(val context: Context, private val 
        materialModelDataMapper: MaterialModelDataMapper) {
    
    fun transform(assignedMaterial: AssignedMaterial?): AssignedMaterialModel {
        if (assignedMaterial == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val assignedMaterialModel = AssignedMaterialModel()
        assignedMaterialModel.id = assignedMaterial.id
        assignedMaterialModel.quantity = assignedMaterial.quantity
        assignedMaterialModel.material = materialModelDataMapper
                .transform(assignedMaterial.material)

        return assignedMaterialModel
    }

    fun transform(assignedMaterialCollection: Collection<AssignedMaterial>?):
            Collection<AssignedMaterialModel>{
        val assignedMaterialModelCollection: MutableCollection<AssignedMaterialModel> = ArrayList()

        if (assignedMaterialCollection != null && !assignedMaterialCollection.isEmpty())
            assignedMaterialCollection.mapTo(assignedMaterialModelCollection) { transform(it) }
        return assignedMaterialModelCollection
    }
    
}