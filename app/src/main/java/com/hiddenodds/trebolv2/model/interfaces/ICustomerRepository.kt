package com.hiddenodds.trebolv2.model.interfaces

import com.hiddenodds.trebolv2.domain.data.MapperCustomer
import com.hiddenodds.trebolv2.presentation.model.CustomerModel
import io.reactivex.Observable

interface ICustomerRepository: IMessagePersistent {
    fun save(customer: MapperCustomer): Observable<CustomerModel>
}