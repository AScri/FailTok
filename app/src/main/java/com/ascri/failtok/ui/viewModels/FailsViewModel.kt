package com.ascri.failtok.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ascri.failtok.FailTokApp
import com.ascri.failtok.data.models.Fail
import com.ascri.failtok.data.repositories.FailRepository
import com.ascri.failtok.utilities.extensions.BaseViewModel
import com.ascri.failtok.utilities.extensions.addTo
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FailsViewModel @Inject constructor(private val failsRepository: FailRepository) : BaseViewModel() {
    private val fails = mutableListOf<Fail>()
    private val page = MutableLiveData(listOf<Fail>())
    private var type: FailsType = FailsType.HOT_TYPE
    private val isLoading = MutableLiveData(false)

    fun getWalls(): List<Fail> = fails
    fun getPage(): LiveData<List<Fail>> = page
    fun isLoading(): LiveData<Boolean> = isLoading

    fun init(type: FailsType) {
        this.type = type
        if (fails.isEmpty()) {
            loadByType(type)
        } else page.value = fails
    }

    fun loadByType(type: FailsType, page: Int = 1) {
        when (type) {
            FailsType.HOT_TYPE -> loadHotFails(page)
            FailsType.NEW_TYPE -> loadNewFails(page)
            FailsType.TOP_TYPE -> loadTopFails(page)
        }
    }

    private fun loadHotFails(page: Int = 1) {
        isLoading.value = true
        failsRepository.getHotFails(page)
            .defConfig()
            .subscribe({
                fails.addAll(it)
                this.page.value = it
            }, {
                Log.d(FailTokApp.TAG, "loadHotFails: error = $it")
                it.printStackTrace()
            })
            .addTo(compositeDisposable)
    }

    private fun loadNewFails(page: Int = 1) {
        failsRepository.getNewFails(page)
            .defConfig()
            .subscribe({
                fails.addAll(it)
                this.page.value = it
            }, {
                Log.d(FailTokApp.TAG, "loadNewFails: error = $it")
                it.printStackTrace()
            })
            .addTo(compositeDisposable)
    }

    private fun loadTopFails(page: Int = 1) {
        failsRepository.getTopFails(page)
            .defConfig()
            .subscribe({
                fails.addAll(it)
                this.page.value = it
            }, {
                Log.d(FailTokApp.TAG, "loadTopFails: error = $it")
                it.printStackTrace()
            })
            .addTo(compositeDisposable)
    }

    fun fetchDetails(id: Long, callback: (fail: Fail) -> Unit) {
        failsRepository.getFailDetails(id)
            .defConfig()
            .subscribe({
                callback(it)
            }, {
                Log.d(FailTokApp.TAG, "fetchDetails: error = $it")
                it.printStackTrace()
            })
            .addTo(compositeDisposable)
    }

    private fun <T> Single<T>.defConfig(): Single<T> {
        return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { isLoading.value = false }
            .retry(2)
    }

    enum class FailsType {
        HOT_TYPE,
        NEW_TYPE,
        TOP_TYPE
    }
}
