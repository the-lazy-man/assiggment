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
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
               <soapenv:Header/>
               <soapenv:Body>
                  <tem:GetCCPromo>
                     <tem:lang>en</tem:lang>
                  </tem:GetCCPromo>
               </soapenv:Body>
            </soapenv:Envelope>
        """.trimIndent()

        val request = Request.Builder()
            .url("https://api-forexcopy.contentdatapro.com/Services/CabinetMicroService.svc")
            .post(soapXml.toRequestBody("text/xml; charset=utf-8".toMediaType()))
            .addHeader("SOAPAction", "http://tempuri.org/ICabinetMicroService/GetCCPromo")
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            if (response.isSuccessful && responseBody.isNotEmpty()) {
                parsePromotions(responseBody)
            } else {
                Log.e("SoapManager", "Error fetching promotions: ${response.code}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("SoapManager", "Exception fetching promotions", e)
            emptyList()
        }
    }

    private fun parsePromotions(xml: String): List<Promotion> {
        val promotions = mutableListOf<Promotion>()
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(StringReader(xml))

            var eventType = xpp.eventType
            var currentTag = ""
            var title = ""
            var imageUrl = ""
            var link = ""
            var id = ""

            // Very basic XML parsing logic - can be improved with XmlPullParser util
            // We look for specific tags inside the structure
            // Expecting structure like: <GetCCPromoResult> ... <CCPromo> ... </CCPromo> </GetCCPromoResult>
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    currentTag = xpp.name
                    if (currentTag == "CCPromo") {
                        title = ""
                        imageUrl = ""
                        link = ""
                        id = ""
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    val text = xpp.text
                    when (currentTag) {
                        "ID" -> id = text
                        "Title" -> title = text
                        "UrlImage" -> imageUrl = text // Swap domain if needed
                        "UrlLink" -> link = text
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.name == "CCPromo") {
                        if (title.isNotEmpty()) {
                             // Replace domain as requested
                            val finalImage = imageUrl.replace("forex-images.instaforex.com", "forex-images.ifxdb.com")
                            promotions.add(Promotion(id, title, finalImage, link))
                        }
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
