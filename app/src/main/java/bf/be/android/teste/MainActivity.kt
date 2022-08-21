package bf.be.android.teste

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import bf.be.android.teste.apis.ApiDefinitionsEnglish
import bf.be.android.teste.apis.ApiDefinitionsFrench
import bf.be.android.teste.apis.ApiRandomWordFrench
import bf.be.android.teste.models.English
import bf.be.android.teste.models.French
import bf.be.android.teste.models.RandomWordFrench
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
        frBtn.setOnClickListener(this::getFrenchDefinition)
        enBtn.setOnClickListener(this::getEnglishDefinition)

        getRandomWordFr()
    }

    fun getRandomWordFr() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://frenchwordsapi.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiRandomWordFrench::class.java)
        val call = api.getRandomWord("1")
        call.enqueue(object : Callback<RandomWordFrench> {
            override fun onResponse(call: Call<RandomWordFrench>, response: Response<RandomWordFrench>) {
                val result = response.body()?.getResultList()
                if (result != null) {
                    println("=====================" + result.get(0).getResult())
                }

                // Url:
                // https://frenchwordsapi.herokuapp.com/api/Word/GetRandomWord?nbrWordsNeeded=1


            }

            override fun onFailure(call: Call<RandomWordFrench>, t: Throwable) {
                println("Error")
            }
        })

    }


    fun getFrenchDefinition(view: View) {
        findViewById<TextView>(R.id.tv_fr).text = ""
        val motChoisi = findViewById<EditText>(R.id.et_fr).text.toString()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://frenchwordsapi.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiDefinitionsFrench::class.java)
        val call = api.getDefinitions(motChoisi)
        call.enqueue(object : Callback<French?> {
            override fun onResponse(call: Call<French?>, response: Response<French?>) {
                val frenchDefinitions = response.body()?.getDefinitionsList() // French definitions here
                val length = frenchDefinitions!!.size
                for (i in 0 until length) {
                    findViewById<TextView>(R.id.tv_fr).append("\n\n" + frenchDefinitions[i])
                }
                findViewById<EditText>(R.id.et_fr).setText("")
            }

            override fun onFailure(call: Call<French?>, t: Throwable) {
                println("Error")
            }
        })
    }

    fun getEnglishDefinition(view: View) {
        findViewById<TextView>(R.id.tv_en).text = ""
        val chosenWord = findViewById<EditText>(R.id.et_en).text.toString()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiDefinitionsEnglish::class.java)
        val call = api.getDefinitions(chosenWord)
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
                for (i in 0 until length) {
                    findViewById<TextView>(R.id.tv_en).append("\n\n" + englishDefinitions[i])
                }
                findViewById<EditText>(R.id.et_en).setText("")
            }

            override fun onFailure(call: Call<List<English>?>, t: Throwable) {
                println("Error")
            }
        })
    }
}