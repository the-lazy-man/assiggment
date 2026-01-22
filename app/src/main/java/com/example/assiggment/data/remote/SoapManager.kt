package com.example.assiggment.data.remote

import android.util.Log
import com.example.assiggment.domain.model.Promotion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import javax.inject.Inject

class SoapManager @Inject constructor(
    private val client: OkHttpClient
) {
    suspend fun getPromotions(): List<Promotion> = withContext(Dispatchers.IO) {
        val soapXml = """
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                          xmlns:tem="http://tempuri.org/">
           <soapenv:Header/>
           <soapenv:Body>
              <tem:GetCCPromo>
                 <tem:lang>en</tem:lang>
              </tem:GetCCPromo>
           </soapenv:Body>
        </soapenv:Envelope>
    """.trimIndent()

        Log.d("SoapManager", "SOAP Request XML:\n$soapXml")

        val request = Request.Builder()
            .url("https://api-forexcopy.contentdatapro.com/Services/CabinetMicroService.svc")
            .post(soapXml.toRequestBody("text/xml; charset=utf-8".toMediaType()))
            .addHeader("SOAPAction", "http://tempuri.org/ICabinetMicroService/GetCCPromo")
            .addHeader("Content-Type", "text/xml; charset=utf-8")
            .build()

        try {
            Log.d("SoapManager", "Making SOAP request...")
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            Log.d("SoapManager", "Response code: ${response.code}")
            Log.d("SoapManager", "Response body (first 500 chars): ${responseBody.take(500)}...")

            if (response.isSuccessful && responseBody.isNotEmpty()) {
                val parsed = parsePromotions(responseBody)
                Log.d("SoapManager", "Parsed ${parsed.size} promotions")
                parsed
            } else {
                Log.e("SoapManager", "Error: ${response.code} - ${response.message}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("SoapManager", "Exception", e)
            emptyList()
        }
    }
    private fun parsePromotions(xml: String): List<Promotion> {
        val promotions = mutableListOf<Promotion>()
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true  // ✓ You have this
            val xpp = factory.newPullParser()
            xpp.setInput(StringReader(xml))

            var eventType = xpp.eventType
            var currentPromo: MutableMap<String, String>? = null
            var currentTag: String? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        currentTag = xpp.name
                        if (currentTag == "CCPromo") {
                            currentPromo = mutableMapOf()
                        }
                    }
                    XmlPullParser.TEXT -> {
                        val text = xpp.text.trim()
                        if (text.isNotEmpty() && currentTag != null) {
                            when (currentTag) {
                                "ID" -> currentPromo?.put("id", text)
                                "Title" -> currentPromo?.put("title", text)
                                "UrlImage" -> currentPromo?.put("imageUrl", text)
                                "UrlLink" -> currentPromo?.put("link", text)
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (xpp.name == "CCPromo") {
                            currentPromo?.let { promoMap ->
                                val title = promoMap["title"] ?: ""
                                if (title.isNotEmpty()) {
                                    val imageUrl = promoMap["imageUrl"]?.replace(
                                        "forex-images.instaforex.com",
                                        "forex-images.ifxdb.com"
                                    ) ?: ""

                                    promotions.add(Promotion(
                                        id = promoMap["id"] ?: "",
                                        title = title,
                                        imageUrl = imageUrl,
                                        link = promoMap["link"] ?: ""
                                    ))
                                }
                            }
                            currentPromo = null
                        }
                        currentTag = null
                    }
                }
                eventType = xpp.next()
            }
        } catch (e: Exception) {
            Log.e("SoapManager", "Error parsing XML", e)
        }
        return promotions
    }
}
