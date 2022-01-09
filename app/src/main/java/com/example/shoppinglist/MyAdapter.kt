package com.example.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import io.realm.mongodb.App
import org.bson.Document
import org.bson.types.ObjectId

class MyAdapter(private var itemList: ArrayList<ShoppingItem>, private var app: App):
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemName: EditText = view.findViewById(R.id.itemText)
        var itemSelected: CheckBox = view.findViewById(R.id.itemCheckBox)
        val button: Button = view.findViewById(R.id.itemButton)  //кнопка удаления

//        init{
//            button.setOnClickListener {
//
//            }
//        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//создаем объект, в который записываем ссылку на макет разметки элемента нашего списка
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView) //возвращаем объект класса MyViewHolder с интерфейсом
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemName.setText(item.text)
        holder.itemName.
//        setOnKeyListener { view, i, keyEvent ->
        setOnFocusChangeListener { view, b ->
            println("text = ${holder.itemName.text}")
            if (position>=0 && position<itemList.size) {
                itemList[position].text = holder.itemName.text.toString()

                var newItem = Document("_id", ObjectId()) //создаем документ для БД
                newItem.append("itemName",holder.itemName.text.toString()) //вставляем в него поля с нужными данными из объекта user
                //todo сделать отдельную функцию на update
                val user = app?.currentUser()
                val mongoClient = user!!.getMongoClient("listDB-service")
                val mongoDatabase = mongoClient.getDatabase("listDB")
                println("listDB = $mongoDatabase")
                val mongoCollection = mongoDatabase.getCollection("items")
                mongoCollection.insertOne(newItem)?.getAsync { task -> //заменить на update https://docs.mongodb.com/realm/sdk/android/examples/mongodb-remote-access/#update-a-single-document
                    if (task.isSuccess) {
                        println("successfully inserted a document with id: ${task.get().insertedId}")
                    } else {
                        println("failed to insert documents with: ${task.error}")
                    }
                }
//                notifyItemChanged(position)
            }
//            false
        }
        holder.itemSelected.isChecked = item.checked
        holder.button.setOnClickListener {
            itemList.remove(item)
            //todo сделать отдельную функцию для удаления элемента из БД
            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, itemList.size)
            //notifyDataSetChanged()
        }
    }

//    override fun onViewAttachedToWindow(holder: MyViewHolder) {
//        holder.itemName.requestFocus()
//        super.onViewAttachedToWindow(holder)
//    }

    override fun getItemCount(): Int {//метод для вычисления кол-ва создаваемых элементов списка
        return itemList.size
    }
}