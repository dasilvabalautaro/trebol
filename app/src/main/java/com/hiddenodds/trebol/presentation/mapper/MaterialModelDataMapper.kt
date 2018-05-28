package com.hiddenodds.trebol.presentation.mapper

import android.content.Context
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.data.Material
import com.hiddenodds.trebol.presentation.model.MaterialModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialModelDataMapper @Inject constructor(val context: Context) {
    fun transform(material: Material?): MaterialModel {
        if (material == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val materialModel = MaterialModel()
        materialModel.id = material.id
        materialModel.code = material.code
        materialModel.detail = material.detail
        
        return materialModel
    }

    fun transform(materialCollection: Collection<Material>?): Collection<MaterialModel>{
        val materialModelCollection: MutableCollection<MaterialModel> = ArrayList()

        if (materialCollection != null && !materialCollection.isEmpty())
            materialCollection.mapTo(materialModelCollection) { transform(it) }
        return materialModelCollection
    }
}