package com.emreakpinar.filmeet.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emreakpinar.filmeet.R
import com.emreakpinar.filmeet.eslesme.Match

class MatchAdapter(private val matchList: List<Match>) :
    RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(match: Match) {
            val matchTextView = itemView.findViewById<TextView>(R.id.matchTextView)
            matchTextView.text = "User: ${match.userId}, Match Rate: ${match.matchRate * 100}%"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.match_item, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(matchList[position])
    }

    override fun getItemCount(): Int = matchList.size
}
