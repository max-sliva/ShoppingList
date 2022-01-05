package com.example.shoppinglist

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.core.view.get
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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
        val shoppingListAdapter = MyAdapter(itemsList)
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
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}