package com.emreakpinar.filmeet.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emreakpinar.filmeet.adapter.MovieClickListener
import com.emreakpinar.filmeet.databinding.FavoriteRecyclerViewBinding
import com.emreakpinar.filmeet.model.FavoriteMovie
import com.emreakpinar.filmeet.util.loadCircleImage


interface FavoriteClickListener {
    fun onDeleteClicked(movieId: Int)
}
class FavoriteAdapter(
    private var favoriteList: List<FavoriteMovie>,
    private val favoriteClickListener: FavoriteClickListener
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    inner class FavoriteViewHolder(val binding: FavoriteRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            FavoriteRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return favoriteList.size
    }
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val film = favoriteList[position]
        holder.binding.favoriteMovieTitle.text = film.title
        holder.binding.textViewFavoriteOverview.text = film.overview
        holder.binding.imageViewFavoriteMovie.loadCircleImage(film.posterPath)

        // Silme butonunun tıklama işlemleri
        holder.binding.deleteFavorites.setOnClickListener {
            favoriteClickListener.onDeleteClicked(film.filmId)
        }
    }
    fun updateFavoriteList(newFavoriteList: List<FavoriteMovie>) {
        favoriteList = newFavoriteList
        notifyDataSetChanged()
    }
}
