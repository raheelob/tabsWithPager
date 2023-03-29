package com.cookpad.hiring.android.ui.network

import com.cookpad.hiring.android.data.api.CookpadHiringService
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class FetchCollectionListTest {
    private lateinit var service: CookpadHiringService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CookpadHiringService::class.java)
    }

    private fun enqueueMockResponse(fileName: String) {
        javaClass.classLoader?.let {
            val inputStream = it.getResourceAsStream(fileName)
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()
            mockResponse.setBody(source.readString(Charsets.UTF_8))
            server.enqueue(mockResponse)
        }
    }

    private fun enqueueMockResponseError(fileName: String) {
        javaClass.classLoader?.let {
            val inputStream = it.getResourceAsStream(fileName)
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()
            mockResponse.setBody(source.readString(Charsets.UTF_8))
            mockResponse.setResponseCode(404)
            server.enqueue(mockResponse)
        }
    }

    @Test
    fun `Collection List Should Not Be Empty`() {
        runBlocking {
            enqueueMockResponse("CollectionResponse.json")
            val responseBody = service.getCollections()
            assertTrue(responseBody.toString().isNotEmpty())
            val request = server.takeRequest()
            assertTrue(request.path.equals("/collections"))
        }
    }

    @Test
    fun `Collection List Should Throw Error`() {
        runBlocking {
            enqueueMockResponseError("CollectionResponse.json")
            try {
                val responseBody = service.getCollections()
                assert(false)
            } catch (e: Exception) {
                assert(true)
            }
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}