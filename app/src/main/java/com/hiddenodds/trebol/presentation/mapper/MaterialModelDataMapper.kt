package com.hiddenodds.trebol.presentation.mapper

import android.content.Context
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.data.Material
import com.hiddenodds.trebol.model.data.TypeNotification
import com.hiddenodds.trebol.presentation.interfaces.IModel
import com.hiddenodds.trebol.presentation.interfaces.IModelDataMapper
import com.hiddenodds.trebol.presentation.model.MaterialModel
import io.realm.RealmObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialModelDataMapper @Inject constructor(val context: Context):
        IModelDataMapper {
    override fun <E : RealmObject> transform(objectRealm: E?): IModel {
        if (objectRealm == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val material = objectRealm as Material
        val materialModel = MaterialModel()
        materialModel.id = material.id
        materialModel.code = material.code
        materialModel.detail = material.detail

        return materialModel
    }

    override fun <E : RealmObject> transform(objectRealmCollection:
                                             Collection<E>?): Collection<IModel> {
        val materialCollection = objectRealmCollection!!
                .filterIsInstance<Material>() as Collection<Material>
        val materialModelCollection: MutableCollection<IModel> = ArrayList()

        if (!materialCollection.isEmpty())
            materialCollection.mapTo(materialModelCollection) { transform(it) }
        return materialModelCollection
    }


    /*fun transform(material: Material?): MaterialModel {
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
    }*/
}