package tv.purple.monolith.component.api.data.api

import io.reactivex.Single
import retrofit2.http.GET
import tv.purple.monolith.models.retrofit.ffz.FfzBadgesData

interface FfzApi {
    @GET("/v1/badges/ids")
    fun globalBadges(): Single<FfzBadgesData>
}