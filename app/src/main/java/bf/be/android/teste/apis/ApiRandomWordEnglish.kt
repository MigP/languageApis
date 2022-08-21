package bf.be.android.teste.apis

import retrofit2.Call
import retrofit2.http.GET

interface ApiRandomWordEnglish {
    @GET("word")
    fun getRandomWord(): Call<List<String>>
}