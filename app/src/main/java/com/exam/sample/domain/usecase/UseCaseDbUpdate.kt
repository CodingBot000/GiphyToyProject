package com.exam.sample.domain.usecase

import com.exam.sample.domain.usecase.base.SingleUseCase
import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.model.repository.favorite.FavoriteInfoRepository
import io.reactivex.Single
import javax.inject.Inject

class UseCaseDbUpdate @Inject constructor(private val favoriteInfoRepository: FavoriteInfoRepository) :
    SingleUseCase<Int>() {

    private lateinit var favoriteInfo: FavoriteInfo

    fun setData(favoriteInfo: FavoriteInfo) {
        this.favoriteInfo = favoriteInfo
    }

    override fun buildUseCaseSingle(): Single<Int> =
        favoriteInfoRepository.updateFavoriteDB(favoriteInfo)
}
