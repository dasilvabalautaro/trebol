package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.domain.data.MapperCustomer
import com.hiddenodds.trebolv2.presentation.model.CustomerModel
import io.reactivex.Observable
import java.util.*

interface ICustomerRepository{
    fun save(customer: MapperCustomer): Observable<CustomerModel>
    fun saveList(list: ArrayList<MapperCustomer>): Observable<Boolean>
    fun deleteCustomerOfTechnical(code: String, fieldName: String):
            Observable<Boolean>
}