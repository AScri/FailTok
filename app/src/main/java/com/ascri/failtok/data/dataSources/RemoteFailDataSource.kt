package com.ascri.failtok.data.dataSources

import android.util.Log
import com.ascri.failtok.FailTokApp
import com.ascri.failtok.data.models.Fail
import com.ascri.failtok.data.models.Order
import com.ascri.failtok.data.models.TimeFrame
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import javax.inject.Inject

open class RemoteFailDataSource @Inject constructor(private val okHttpClient: OkHttpClient) {
    companion object {
        internal const val MAIN_ENDPOINT = "https://livestreamfails.com/load/loadPosts.php"
        internal const val DETAILS_ENDPOINT = "https://livestreamfails.com/post/%d"
    }

    fun getFails(
        page: Int, timeFrame: TimeFrame, nsfw: Boolean, order: Order, game: String, streamer: String
    ): Single<List<Fail>> {
        return Single.fromCallable {
            val models = mutableListOf<Fail>()

            val url = getRequestUrl(page, timeFrame, order, nsfw, game, streamer)

            val request = Request.Builder().url(url).get().build()
            val html = okHttpClient.newCall(request).execute().body()?.string()
            val doc = Jsoup.parse(html)

            doc.select("div.post-card")?.let {
                it.forEach { card ->
                    val title = card.selectFirst("p.title").text()

                    val postId = card.selectFirst("a[href]").attr("href")
                        .split('/', ignoreCase = true).last().toLong()

                    val thumbnailUrl = card.selectFirst("img.card-img-top").attr("src")

                    val streamerName = card.selectFirst("div.stream-info > small.text-muted")
                        ?.select("a[href]")?.get(0)?.text() ?: ""
                    val gameName = card.selectFirst("div.stream-info > small.text-muted")
                        ?.select("a[href]")?.get(1)?.text() ?: ""

                    val isNsfw = card.selectFirst("span.oi-warning") != null

                    val pointsElement = card.selectFirst("small.text-muted > span.oi-arrow-circle-top").parent()

                    val points = pointsElement
                        .ownText().replace(Regex("[^\\d.]"), "").toInt()

                    models.add(Fail(postId, title, streamerName, gameName, points, isNsfw, thumbnailUrl))
                }
            }
            models.toList()
        }
    }

    fun getDetails(postId: Long): Single<Fail> {
        return Single.fromCallable {
            val request = Request.Builder().url(String.format(DETAILS_ENDPOINT, postId)).get().build()
            val response = okHttpClient.newCall(request).execute()
            val responseUrl = response.networkResponse()?.request()?.url().toString()

            if (postId == 0L || responseUrl.contains("post_not_found")) {
                Log.w(FailTokApp.TAG, "getDetails: Fail ID not found")
            }

            val doc = Jsoup.parse(response.body()?.string())
            doc?.let {
                val title = it.selectFirst("h4.post-title").text()

                val streamer = it.selectFirst("div.post-streamer-info")
                    ?.select("a[href]")?.get(0)?.text() ?: ""
                val game = it.selectFirst("div.post-streamer-info")
                    ?.select("a[href]")?.get(1)?.text() ?: ""

                val nsfw = it.selectFirst("div.post-stats-info > span.oi-warning") != null

                val pointsElement = it.selectFirst("small.text-muted > span.oi-arrow-circle-top").parent()

                val points = pointsElement
                    .ownText().replace(Regex("[^\\d.]"), "").toInt()

                val videoUrl = it.selectFirst("video > source").attr("src")

                val sourceUrl = it.selectFirst("div.post-stats-info > a").attr("href")
                val thumbnailUrl = it.selectFirst("video").attr("poster")

                Fail(postId, title, streamer, game, points, nsfw, thumbnailUrl, videoUrl, sourceUrl)
            }
        }
    }

    private fun getRequestUrl(
        page: Int, timeFrame: TimeFrame, order: Order,
        nsfw: Boolean, game: String, streamer: String
    ): HttpUrl? {
        val postMode = getPostMode(streamer, game)
        val baseUrl = HttpUrl.parse(MAIN_ENDPOINT)

        var urlBuilder = baseUrl?.newBuilder()
            ?.addQueryParameter("loadPostMode", postMode)
            ?.addQueryParameter("loadPostNSFW", (if (nsfw) 1 else 0).toString())
            ?.addQueryParameter("loadPostOrder", order.value)
            ?.addQueryParameter("loadPostPage", page.toString())
            ?.addQueryParameter("loadPostTimeFrame", timeFrame.value)

        if (postMode == "streamer") {
            urlBuilder = urlBuilder?.addQueryParameter("loadPostModeStreamer", streamer)
        }
        if (postMode == "game") {
            urlBuilder = urlBuilder?.addQueryParameter("loadPostModeGame", game)
        }

        return urlBuilder?.build()
    }

    private fun getPostMode(streamer: String, game: String): String {
        if (streamer.isNotEmpty()) return "streamer"
        if (game.isNotEmpty()) return "game"
        return "standard"
    }
}