package com.ascri.failtok.data.repositories

import com.ascri.failtok.data.dataSources.RemoteFailDataSource
import com.ascri.failtok.data.models.Fail
import com.ascri.failtok.data.models.Order
import com.ascri.failtok.data.models.TimeFrame
import io.reactivex.Single
import javax.inject.Inject

class FailRepository @Inject constructor(private val remote: RemoteFailDataSource) {

    fun getHotFails(page: Int = 1): Single<List<Fail>> {
        return remote.getFails(page, TimeFrame.MONTH, false, Order.HOT, "", "")
    }

    fun getNewFails(page: Int = 1): Single<List<Fail>> {
        return remote.getFails(page, TimeFrame.MONTH, false, Order.NEW, "", "")
    }

    fun getTopFails(page: Int = 1): Single<List<Fail>> {
        return remote.getFails(page, TimeFrame.MONTH, false, Order.TOP, "", "")
    }

    fun getFailDetails(id: Long): Single<Fail> {
        return remote.getDetails(id)
    }
}