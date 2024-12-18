package com.example.pivco_fragments.Ktor

import android.util.Log

import com.example.pivco_fragments.Models.Character
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.serialization.json.Json
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import kotlin.time.Duration.Companion.seconds
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.parameter
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.builtins.ListSerializer

private const val NETWORK_BASE_URL = "www.anapioficeandfire.com"

class KtorNetwork : KtorNetworkApi {
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    private val client: HttpClient by lazy{
        HttpClient(engine = OkHttp.create()) {
            install(ContentNegotiation) { json(json) }

            install(HttpTimeout) {
                connectTimeoutMillis = 20.seconds.inWholeMilliseconds
                requestTimeoutMillis = 60.seconds.inWholeMilliseconds
                socketTimeoutMillis = 20.seconds.inWholeMilliseconds
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

    override suspend fun getCharacters(page: Int): List<Character> {
        return try {
            client.get {
                url {
                    host = NETWORK_BASE_URL
                    protocol = URLProtocol.HTTPS
                    contentType(ContentType.Application.Json)
                    path("api", "characters")
                    parameter("page", page)
                    parameter("pageSize", "50")
                }
            }.let { response ->
                val jsonString = response.body<String>()
                json.decodeFromString(ListSerializer(Character.serializer()), jsonString)
            }
        } catch (exception: Exception) {
            Log.e("Error", exception.message.toString())
            listOf()
        }
    }

    override fun close() {
        client.close()
    }
}