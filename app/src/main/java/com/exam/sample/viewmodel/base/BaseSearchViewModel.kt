package com.exam.sample.viewmodel.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exam.sample.domain.usecase.UseCaseGetSearchData
import com.exam.sample.livedata.Event
import com.exam.sample.model.data.TrendingData
import com.exam.sample.utils.Resource
import com.exam.sample.utils.isNetworkConnected

/**
 * Artists, Clips, Search  ViewModel들이 모두 상속받아 getSearch 메소드의 중복을 없애기위해 만들었으나,
 * ViewModel들의 일관된 상속관계와 일관된 관심사 분리 등 원칙에 해를 끼친다 생각되어
 * 각 ViewModel의 구현을 중복이 발생해도 각각 하는 방식으로 변경하였음.
 * 어차피 Artists, Clips는 임의로 탭을 늘리기위한 의미없는 더미에 불과하기 때문에.
 */
abstract class BaseSearchViewModel() : BaseViewModel() {
    private val _itemLiveData = MutableLiveData<Event<Resource<TrendingData>>>()
    val itemLiveData: LiveData<Event<Resource<TrendingData>>> get() = _itemLiveData

    fun getSearch(useCaseGetSearchData: UseCaseGetSearchData, keyword: String, offset: Int, isMore: Boolean = false) {
        if (!isNetworkConnected())
            return

        showProgress()
        with(useCaseGetSearchData) {
            setData(keyword, offset)
            execute(
                onSuccess = {
                    if (isMore)
                        it.isAddData = true
                    val res = Event(Resource.success(it))
                    _itemLiveData.postValue(res)
                },
                onError = {
                    val res = Event(Resource.error(it.message.toString(), null))
                    _itemLiveData.postValue(res)
                },
                onFinished = {
                    hideProgress()
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
