package bf.be.android.teste.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RandomFrenchWord {
    @SerializedName("WordName")
    private val wordName: String = ""

    fun getResult(): String {
        return this.wordName
    }
}