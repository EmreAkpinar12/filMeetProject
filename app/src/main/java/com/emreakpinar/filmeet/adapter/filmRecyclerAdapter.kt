package com.emreakpinar.filmeet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emreakpinar.filmeet.databinding.FilmRecycleRowBinding
import com.emreakpinar.filmeet.model.Movie
import com.emreakpinar.filmeet.model.MovieItem

class filmRecyclerAdapter(val filmListesi : Movie) : RecyclerView.Adapter<filmRecyclerAdapter.filmViewHolder> (){

    class filmViewHolder(val binding : FilmRecycleRowBinding) : RecyclerView.ViewHolder(binding.root){



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): filmViewHolder {

        val binding =FilmRecycleRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return filmViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return filmListesi.size

    }

    fun filmListesiGuncelle(yeniFilmListesi : List<MovieItem>){

        filmListesi.clear()
        filmListesi.addAll(yeniFilmListesi)
        notifyDataSetChanged()


    }

    override fun onBindViewHolder(holder: filmViewHolder, position: Int) {
        holder.binding.isim.text = filmListesi[position].Title
        holder.binding.basrol.text=filmListesi[position].Casts.toString()
        holder.binding.yonetmen.text=filmListesi[position].Director.toString()
        holder.binding.sure.text=filmListesi[position].RuntimeMinutes.toString()
        holder.binding.tR.text=filmListesi[position].Genres.toString()
        holder.binding.yil.text=filmListesi[position].Year.toString()



      /*  holder.itemView.setOnClickListener{

            val action = anasayfaFragmentDirections.actionAnasayfaFragmentToFilmDetayFragment(filmListesi[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }
*/
    }
}