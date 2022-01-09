package com.example.shoppinglist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
//import com.mongodb.client.MongoClient
//import com.mongodb.client.MongoClients
//import com.mongodb.client.MongoDatabase
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
// Realm Authentication Packages
import io.realm.mongodb.User
import io.realm.mongodb.Credentials
import io.realm.RealmConfiguration



// MongoDB Service Packages

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
//https://drive.google.com/file/d/1N_mxkVWme692WFbkuQlgnsel3qXpbtrm/view?usp=sharing listContent.txt
    var user: User? = null
    var app: App? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        var itemsList = arrayListOf(ShoppingItem("",false))
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        Realm.init(applicationContext)
        val config = RealmConfiguration.Builder() // below line is to allow write
            // data to database on ui thread.
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)
        val appID = "shoppinglist-xkokw" // replace this with your App ID
        app = App(
            AppConfiguration.Builder(appID)
                .build()
        )
        val emailPasswordCredentials: Credentials = Credentials.emailPassword(
            "persimon@inbox.ru",
            "111111"
        )

        app?.loginAsync(emailPasswordCredentials) {
            if (it.isSuccess) {
                println("Successfully authenticated using an email and password.")
                user = app?.currentUser()
            } else {
                println(it.error.toString())
            }
        }

        val shoppingListAdapter = MyAdapter(itemsList, app!!)
//создаем компоновщик
        val layoutManager = LinearLayoutManager(applicationContext)
//и передаем его recyclerView
        recyclerView.layoutManager = layoutManager
//задаем аниматор элементов списка
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = shoppingListAdapter
        val addItemButton: Button = findViewById(R.id.button2)
        addItemButton.setOnClickListener{
            itemsList.add(ShoppingItem("", false))
            recyclerView.smoothScrollToPosition(recyclerView.bottom)
            shoppingListAdapter.notifyDataSetChanged()
            //todo сделать отдельную функцию для добавления элемента
//            shoppingListAdapter.getItemViewType(itemsList.size-1).req
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_shareList -> toMongoAtlas()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toMongoAtlas(): Boolean {
        println("Share to drive")
//        val connectionString =
//            ConnectionString("mongodb://persimon@inbox.ru:111111@realm.mongodb.com:27020/?authMechanism=PLAIN&authSource=%24external&ssl=true&appName=shoppinglist-xkokw:listDB-service:local-userpass")
//        val settings: MongoClientSettings = MongoClientSettings.builder()
//            .applyConnectionString(connectionString)
//            .build()
//        val mongoClient: MongoClient = MongoClients.create(settings)
//        val mongoDatabase: MongoDatabase = mongoClient.getDatabase("listDB")
//        var collection = mongoDatabase.getCollection("items") //выбираем коллекцию
//        val itemsCount = collection.countDocuments()
        println("app = ${app.toString()}")
        val user = app?.currentUser()
        val mongoClient =
            user!!.getMongoClient("listDB-service")
        val mongoDatabase =
            mongoClient.getDatabase("listDB")
        println("listDB = $mongoDatabase")
        val mongoCollection = mongoDatabase.getCollection("items")
        mongoCollection.count().getAsync { task ->
            if (task.isSuccess) {
                val count = task.get()
                println("successfully counted, number of documents in the collection: $count")
            } else {
                println("failed to count documents with: ${task.error}")
            }
        }
        val iter = mongoCollection.find().iterator()
        iter.getAsync { task ->
            if (task.isSuccess) {
                val results = task.get()
                println("successfully found all items:")
                while (results.hasNext()) {
                    println(results.next().toString())
                }
            } else {
                println("failed to find documents with: ${task.error}")
            }
        }
        return true
    }
}