package com.exam.sample.domain.usecase

import com.exam.sample.domain.usecase.base.SingleUseCase
import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.model.repository.favorite.FavoriteInfoRepository
import io.reactivex.Single
import javax.inject.Inject

class UseCaseDbRemoveAll @Inject constructor(private val favoriteInfoRepository: FavoriteInfoRepository) :
    SingleUseCase<Int>() {

    override fun buildUseCaseSingle(): Single<Int> =
         favoriteInfoRepository.removeAllFavoriteDB()
}

