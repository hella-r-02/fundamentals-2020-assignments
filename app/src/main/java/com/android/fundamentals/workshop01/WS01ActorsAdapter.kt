package com.android.fundamentals.workshop01

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.fundamentals.R
import com.android.fundamentals.data.models.Actor
import com.android.fundamentals.workshop01.solution.EmptyViewHolder

// TODO 08: Extends this class from "RecyclerView.Adapter<>".
//  Parametrize the generic with EmptyViewHolder.
//  Add a constructor invocation to the RecyclerView.Adapter.
//  Place a cursor on the WS01ActorsAdapter name, press "Alt+Enter", implement all three methods.
class WS01ActorsAdapter : RecyclerView.Adapter<EmptyViewHolder>() {

    private var actors = listOf<Actor>()

    @SuppressLint("NotifyDataSetChanged")
    fun bindActors(newActors: List<Actor>) {
        actors = newActors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyViewHolder {
        return EmptyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_actors_empty, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) {
        Toast.makeText(holder.itemView.context, "onBindViewHolder", Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return 1
    }

}

class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {}