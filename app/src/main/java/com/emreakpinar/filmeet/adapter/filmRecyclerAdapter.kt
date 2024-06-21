package com.emreakpinar.filmeet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emreakpinar.filmeet.databinding.HomeRecyclerViewBinding
import com.emreakpinar.filmeet.model.MovieItem
import com.emreakpinar.filmeet.util.loadCircleImage



interface MovieClickListener{
    fun onMovieClicked(movieId:Int?)


}

class FilmRecyclerAdapter(
    private var filmListesi: List<MovieItem>,
    private val movieClickListener: MovieClickListener
) : RecyclerView.Adapter<FilmRecyclerAdapter.FilmViewHolder>()
{ class FilmViewHolder(val binding: HomeRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding
        = HomeRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return FilmViewHolder(binding)
    }
    override fun getItemCount(): Int {
         return filmListesi.size

    }
    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val movie = filmListesi[position]
        holder.binding.movieIsim.text = movie.title
        holder.binding.filmOzet.text = movie.overview
        holder.binding.rating.text = movie.voteAverage.toString()
        holder.binding.filmImageView.loadCircleImage(movie.posterPath)
        holder.binding.root.setOnClickListener {
            movieClickListener.onMovieClicked(movie.id)

        }
    }
    fun updateList(newList: List<MovieItem>) {
        filmListesi = newList
        notifyDataSetChanged()
    }
}
