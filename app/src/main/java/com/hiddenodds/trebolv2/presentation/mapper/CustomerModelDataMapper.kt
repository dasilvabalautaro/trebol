package com.hiddenodds.trebolv2.presentation.mapper

import android.content.Context
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.data.Customer
import com.hiddenodds.trebolv2.presentation.model.CustomerModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerModelDataMapper @Inject constructor(val context: Context) {

    fun transform(customer: Customer?): CustomerModel {
        if (customer == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val customerModel = CustomerModel()
        customerModel.id = customer.id
        customerModel.name = customer.name
        customerModel.email = customer.email
        customerModel.phone = customer.phone
        return customerModel
    }

    fun transform(formCollection: Collection<Customer>?): Collection<CustomerModel>{
        val formModelCollection: MutableCollection<CustomerModel> = ArrayList()

        if (formCollection != null && !formCollection.isEmpty())
            formCollection.mapTo(formModelCollection) { transform(it) }
        return formModelCollection
    }

}