package com.unsa.smartflu.components

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unsa.smartflu.models.Device
import com.unsa.smartflu.R

class DeviceAdapter(private val devlist: ArrayList<Device>) : RecyclerView.Adapter<DeviceAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return devlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentDev = devlist[position]
        holder.name.text = currentDev.name
        holder.lastValue.text = currentDev.last_value.toString()
        holder.tagDescription.text = currentDev.tag_description

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, OptionsDevice::class.java)
            intent.putExtra("nombre", currentDev.name)
            intent.putExtra("id", currentDev.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.div_name)
        val tagDescription: TextView = itemView.findViewById(R.id.dev_tag)
        val lastValue: TextView = itemView.findViewById(R.id.dev_statistics)
    }
}