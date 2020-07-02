package com.sayantanbanerjee.codechefscrap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch(Dispatchers.Main) {
            Info()
        }
    }

    private suspend fun Info() {
        val job=GlobalScope.launch(Dispatchers.Default) {
            Log.i("######","STARTED")
            val url = "https://www.codechef.com/users/tonystark_3000"
            val doc: Document = Jsoup.connect(url).get()

            val div: Elements = doc.select("div.rating-number")
            val count : String = div.text()  // count = 1668 current_rating

            val div2: Elements = doc.select("div.rating-header.text-center").select("small")
            val countHighest : String = div2.text() // (Highest count = 1668)

            val div3: Elements = doc.select("div.rating-ranks").select("ul")
            for(i in 0..1)
            {
                val ranks : Elements = div3.select("li").eq(i)
                val rank : String = ranks.text()
                Log.i("######",rank) // Global rank and country rank found
            }


        }

        job.join()
    }
}