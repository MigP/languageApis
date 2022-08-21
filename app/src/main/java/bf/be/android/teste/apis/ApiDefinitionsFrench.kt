package bf.be.android.teste.apis


import bf.be.android.teste.models.French
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiDefinitionsFrench {
    @GET("WordDefinition")
    fun getDefinitions(@Query("idOrName") word: String?): Call<French?>
}