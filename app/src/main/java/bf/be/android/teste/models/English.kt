package bf.be.android.teste.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class English {
    @SerializedName("meanings")
    @Expose
    private val meanings: List<Meaning?>? = null

    fun getMeanings() = this.meanings
}