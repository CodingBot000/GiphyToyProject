package com.exam.sample.domain.usecase

import com.exam.sample.domain.usecase.base.SingleUseCase
import com.exam.sample.model.data.TrendingData
import com.exam.sample.model.repository.favorite.FavoriteInfoRepository
import io.reactivex.Single
import javax.inject.Inject

class UseCaseGetGIFsByIds @Inject constructor(private val favoriteInfoRepository: FavoriteInfoRepository) :
    SingleUseCase<TrendingData>() {

    private lateinit var idsList: String

    fun setData(idsList: String) {
        this.idsList = idsList
    }

    override fun buildUseCaseSingle(): Single<TrendingData> =
        favoriteInfoRepository.requestGIFsByIds(idsList)
}
