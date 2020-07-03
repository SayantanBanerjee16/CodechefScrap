package com.sayantanbanerjee.codechefscrap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class MainActivity : AppCompatActivity() {

    private lateinit var disp : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disp = findViewById(R.id.disp)

        GlobalScope.launch(Dispatchers.Main) {
            Info()
        }
    }

    private suspend fun Info() {
        val job = GlobalScope.launch(Dispatchers.Default) {
            var desc: String = ""

            // URL and Document connection
            val url = "https://www.codechef.com/users/arjan_bal"
            val doc: Document = Jsoup.connect(url).get()

            // base info like name and rating
            val div: Elements = doc.select("div.user-details-container.plr10")
            val name: Elements = div.select("header").select("h2")
            desc += "Name : " + name.text() + "\n"
            val group: Elements = div.select("ul").select("li").eq(0)
            desc += "Rating : " + group.select("span.rating").text() + "\n"

            // current_rating
            val div2: Elements = doc.select("div.rating-number")
            val count: String = div2.text()
            desc += "\nCurrent Rating : $count\n"

            // (Highest rating)
            val div3: Elements = doc.select("div.rating-header.text-center").select("small")
            val countHighest: String = div3.text()
            desc += "$countHighest\n"

            // Global rank and country rank found
            val div4: Elements = doc.select("div.rating-ranks").select("ul")
            for (i in 0..1) {
                val ranks: Elements = div4.select("li").eq(i)
                val rank: String = ranks.text()
                desc += "$rank\n"
            }

            //finally displaying after switching context to Main Thread
            withContext(Dispatchers.Main){
                disp.text = desc
            }
        }
        job.join()
    }
}