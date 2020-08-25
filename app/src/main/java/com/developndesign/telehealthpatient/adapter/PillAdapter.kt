package com.developndesign.telehealthpatient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.developndesign.telehealthpatient.R

class PillAdapter(val context: Context, val data: MutableList<String>) : RecyclerView.Adapter<PillAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.pill_text)
        val delete: ImageView = view.findViewById(R.id.delete_pill)
        val pill: RelativeLayout = view.findViewById(R.id.pill)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.pill_adapter_item, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = data[position]
        holder.delete.setOnClickListener {
            data.removeAt(position)
            notifyDataSetChanged()
        }
        holder.pill.setOnClickListener {
            showAlertDialog("", position)
        }
    }

    private fun showAlertDialog(enter: String, position: Int) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.add_member_view, null)
        builder.setView(view)
        var alertDialog = builder.create()
        alertDialog.show()
        val textViewName = view.findViewById<EditText>(R.id.name)
        val textViewEnter = view.findViewById<TextView>(R.id.enter)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        val done = view.findViewById<TextView>(R.id.done)
        textViewEnter.text = "Enter"
        textViewName.setText(data[position])
        cancel.setOnClickListener { alertDialog.cancel() }
        done.setOnClickListener {
            val str = textViewName.text.toString()
            if (!str.replace(" ".toRegex(), "").isEmpty()) {
                data[position] = str
                notifyDataSetChanged()
                alertDialog.cancel()
            } else Toast.makeText(context, "Please enter text", Toast.LENGTH_SHORT).show()
        }
    }
}