package bf.be.android.teste.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WordName {
    @SerializedName("WordName")
    @Expose
    private val wordName: String = ""

    fun getResult(): String {
        return this.wordName
    }
}