package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.domain.data.MapperCustomer
import com.hiddenodds.trebolv2.model.interfaces.ICustomerRepository
import com.hiddenodds.trebolv2.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebolv2.model.interfaces.IThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject


class SaveListCustomerUseCase @Inject constructor(threadExecutor: IThreadExecutor,
                                                  postExecutionThread: IPostExecutionThread,
                                                  private var customerRepository: ICustomerRepository):
        UseCase<Boolean>(threadExecutor, postExecutionThread){

    var listMapperCustomer: ArrayList<MapperCustomer> = ArrayList()

    override fun buildUseCaseObservable(): Observable<Boolean> {
        return this.customerRepository.saveList(listMapperCustomer)
    }

}