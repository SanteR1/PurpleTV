package tv.purple.monolith.core.bridge

import android.app.Activity
import io.reactivex.Single
import tv.purple.monolith.core.CoreUtil
import tv.purple.monolith.core.LifecycleCore
import tv.purple.monolith.core.LoggerImpl
import tv.purple.monolith.core.models.flag.Flag
import tv.purple.monolith.core.models.flag.variants.PlayerImpl
import tv.twitch.android.models.channel.ChannelInfo
import tv.twitch.android.models.player.PlayerImplementation
import tv.twitch.android.shared.subscriptions.db.SubscriptionPurchaseEntity
import tv.twitch.android.shared.subscriptions.purchasers.SubscriptionPurchaseResponse
import java.lang.Math.max

class CoreHook {
    companion object {
        @JvmStatic
        val devMode get() = Flag.DEV_MODE.asBoolean()

        @JvmStatic
        fun hookFastBreadArg(arg: String?): String {
            return if (Flag.DISABLE_FAST_BREAD.asBoolean()) {
                "false"
            } else {
                "true"
            }
        }

        @JvmStatic
        fun hookFastBreadArg(): Boolean {
            return !Flag.DISABLE_FAST_BREAD.asBoolean()
        }

        @JvmStatic
        val isOkHttpLoggingEnabled: Boolean = Flag.OKHTTP_LOGGING.asBoolean()

        @JvmStatic
        fun hookPlayerImplementation(default: PlayerImplementation): PlayerImplementation {
            return when (Flag.PLAYER_IMPL.asVariant<PlayerImpl>()) {
                PlayerImpl.Default -> default
                PlayerImpl.Core -> PlayerImplementation.Core
                PlayerImpl.Exo -> PlayerImplementation.Exo2
            }
        }

        @JvmStatic
        fun injectBilling(
            activity: Activity,
            res: Single<SubscriptionPurchaseResponse>,
            purchaseEntity: SubscriptionPurchaseEntity
        ): Single<SubscriptionPurchaseResponse> {
            return res.doOnSuccess {
                if (it is SubscriptionPurchaseResponse.Success) {
                    CoreUtil.openUrl(
                        context = activity,
                        url = "https://www.twitch.tv/subs/${purchaseEntity.channelDisplayName}"
                    )
                }
            }
        }

        @JvmStatic
        fun isBadLeaderboardId(leaderboardId: String?): Boolean {
            if (leaderboardId.isNullOrBlank()) {
                return true;
            }

            leaderboardId.toIntOrNull()?.let { id ->
                if (id == 0 || id == -1) {
                    return true
                }
            }

            return false
        }

        @Suppress("ReplaceJavaStaticMethodWithKotlinAnalog")
        @JvmStatic
        fun getGlideCacheSize(): Long {
            val value = max(32, Flag.GLIDE_CACHE_SIZE.asInt()) * 1024 * 1024
            return value.toLong().also {
                LoggerImpl.debug("Glide cache size: $it")
            }
        }

        @JvmStatic
        fun onConnectingToChannel(i: Int) {
            LifecycleCore.get().onConnectingToChannel(i)
        }

        @JvmStatic
        fun onConnectedToChannel(i: Int) {
            LifecycleCore.get().onConnectedToChannel(i)
        }

        @JvmStatic
        fun onConnectingToChannel(channelInfo: ChannelInfo?) {
            channelInfo?.id?.let { id ->
                LifecycleCore.get().onConnectingToChannel(id)
            }
        }
    }
}