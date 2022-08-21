package bf.be.android.teste.apis

import bf.be.android.teste.models.French
import bf.be.android.teste.models.RandomWordFrench
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRandomWordFrench {
    @GET("/Word/GetRandomWord")
    fun getRandomWord(@Query("nbrWordsNeeded") nrWords: String): Call<RandomWordFrench>
}