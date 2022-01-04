package com.example.shoppinglist

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
class MyAdapter (private var itemList: ArrayList<ShoppingItem>):
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemName: EditText = view.findViewById(R.id.itemText)
        var itemSelected: CheckBox = view.findViewById(R.id.itemCheckBox)
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
        holder.itemSelected.isChecked = item.checked
    }
    override fun getItemCount(): Int {//метод для вычисления кол-ва создаваемых элементов списка
        return itemList.size
    }
}