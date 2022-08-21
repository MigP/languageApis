package bf.be.android.teste

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import bf.be.android.teste.apis.ApiDefinitionsEnglish
import bf.be.android.teste.apis.ApiDefinitionsFrench
import bf.be.android.teste.apis.ApiRandomWordEnglish
import bf.be.android.teste.apis.ApiRandomWordFrench
import bf.be.android.teste.models.English
import bf.be.android.teste.models.French
import bf.be.android.teste.models.RandomFrenchWord
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val frBtn = findViewById<Button>(R.id.frBtn)
        val enBtn = findViewById<Button>(R.id.enBtn)
        frBtn.setOnClickListener(this::getRandomWordFr)
        enBtn.setOnClickListener(this::getRandomWordEn)

    }

    fun getRandomWordFr(view: View) {
        findViewById<TextView>(R.id.language).setText("Fr")

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://frenchwordsapi.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiRandomWordFrench::class.java)
        val call = api.getRandomWord()
        call.enqueue(object : Callback<List<RandomFrenchWord>> {
            override fun onResponse(call: Call<List<RandomFrenchWord>>, response: Response<List<RandomFrenchWord>>) {
                val randomFrenchWord = response.body()?.get(0)?.getResult() // Random French word here

                findViewById<TextView>(R.id.word).setText(randomFrenchWord)
                getFrenchDefinition(randomFrenchWord!!)
            }

            override fun onFailure(call: Call<List<RandomFrenchWord>>, t: Throwable) {
                println("Error")
            }
        })
    }

    fun getRandomWordEn(view: View) {
        findViewById<TextView>(R.id.language).setText("En")
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://random-word-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiRandomWordEnglish::class.java)
        val call = api.getRandomWord()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                val randomEnglishWord = response.body()?.get(0) // Random English word here

                findViewById<TextView>(R.id.word).setText(randomEnglishWord)
                getEnglishDefinition(randomEnglishWord!!)
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                println("Error")
            }
        })
    }

    fun getFrenchDefinition(word: String) {

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://frenchwordsapi.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiDefinitionsFrench::class.java)
        val call = api.getDefinitions(word)
        call.enqueue(object : Callback<French?> {
            override fun onResponse(call: Call<French?>, response: Response<French?>) {
                val frenchDefinitions = response.body()?.getDefinitionsList() // French definitions here
                if (frenchDefinitions != null) {
                    val length = frenchDefinitions.size
                    findViewById<TextView>(R.id.definitions).setText("")
                    for (i in 0 until length) {
                        findViewById<TextView>(R.id.definitions).append("\n\n" + frenchDefinitions[i])
                    }
                } else {
                    findViewById<TextView>(R.id.definitions).setText("-- Aucune définition trouvée --")
                }
                findViewById<TextView>(R.id.definitions).setMovementMethod(ScrollingMovementMethod())
            }

            override fun onFailure(call: Call<French?>, t: Throwable) {
                println("Error")
            }
        })
    }

    fun getEnglishDefinition(word: String) {

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiDefinitionsEnglish::class.java)
        val call = api.getDefinitions(word)
        call.enqueue(object : Callback<List<English>?> {
            override fun onResponse(call: Call<List<English>?>, response: Response<List<English>?>) {
                var error = false
                var englishDefinitions = ArrayList<String>()
                val dictionaryResponse = response.body()

                if (dictionaryResponse != null) {
                    val meanings = dictionaryResponse[0].getMeanings()
                    if (meanings != null) {
                        for (i in 0 until meanings.size) {
                            val definitions = meanings[i]?.getDefinitions()
                            if (definitions != null) {
                                for (j in 0 until definitions.size) {
                                    englishDefinitions.add(definitions[j]?.getDefinition().toString()) // English definitions here
                                }
                            } else {
                                error = true // Alert the user: no definitions found
                            }
                        }
                    } else {
                        error = true // Alert the user: no definitions found
                    }
                } else {
                    error = true // Alert the user: no definitions found
                }
                val length = englishDefinitions!!.size
                findViewById<TextView>(R.id.definitions).setText("")
                for (i in 0 until length) {
                    findViewById<TextView>(R.id.definitions).append("\n\n" + englishDefinitions[i])
                }

                if (error) findViewById<TextView>(R.id.definitions).setText("-- No definitions found --")
            }

            override fun onFailure(call: Call<List<English>?>, t: Throwable) {
                println("Error")
            }
        })
    }
}