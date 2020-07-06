package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.domain.data.MapperCustomer
import com.hiddenodds.trebol.model.interfaces.ICustomerRepository
import com.hiddenodds.trebol.model.interfaces.IPostExecutionThread
import com.hiddenodds.trebol.model.interfaces.IThreadExecutor
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