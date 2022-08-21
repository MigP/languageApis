package bf.be.android.teste.apis

import bf.be.android.teste.models.RandomFrenchWord
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRandomWordFrench {
    @GET("Word/GetRandomWord?nbrWordsNeeded=1")
    fun getRandomWord(): Call<List<RandomFrenchWord>>
}