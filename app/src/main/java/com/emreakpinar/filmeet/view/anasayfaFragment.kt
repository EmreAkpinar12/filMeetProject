package com.emreakpinar.filmeet.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreakpinar.filmeet.R
import com.emreakpinar.filmeet.ViewModel.anasayfaViewModel
import com.emreakpinar.filmeet.adapter.filmRecyclerAdapter
import com.emreakpinar.filmeet.databinding.FragmentAnasayfaBinding
import com.emreakpinar.filmeet.model.Film
import com.emreakpinar.filmeet.model.Movie
import com.emreakpinar.filmeet.service.filmAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class anasayfaFragment : Fragment(),PopupMenu.OnMenuItemClickListener {

    private var _binding: FragmentAnasayfaBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: anasayfaViewModel
    private val filmRecyclerAdapter = filmRecyclerAdapter(Movie())

    private lateinit var popup:PopupMenu
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= Firebase.auth

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnasayfaBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProvider(this)[anasayfaViewModel::class.java]
        viewModel.refreshData()

binding.filmRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.filmRecyclerView.adapter = filmRecyclerAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.filmRecyclerView.visibility = View.GONE
            binding.filmlerYukleniyor.visibility = View.VISIBLE
            binding.filmHataMesaj.visibility = View.GONE
            viewModel.refreshDataFromInternet()
            binding.swipeRefreshLayout.isRefreshing = false


            binding.floatingActionButton.setOnClickListener{floatingButtonTiklandi(it)}


        }
        observeLiveData()

    }



    private fun observeLiveData (){

        viewModel.filmler.observe(viewLifecycleOwner){

           filmRecyclerAdapter.filmListesiGuncelle(it)
Log.e("anasayfa",it.toString())
            binding.filmRecyclerView.visibility  = View.VISIBLE
        }


        viewModel.filmHataMesaji.observe(viewLifecycleOwner){

            if (it) {

                binding.filmHataMesaj.visibility=View.VISIBLE
                binding.filmRecyclerView.visibility = View.GONE
            }

            else{

                binding.filmHataMesaj.visibility = View.GONE



            }
            }


        viewModel.filmYukleniyor.observe(viewLifecycleOwner){


            if (it){

                binding.filmHataMesaj.visibility = View.GONE
                binding.filmRecyclerView.visibility = View.GONE
                binding.filmlerYukleniyor.visibility = View.VISIBLE

            }else{

                binding.filmlerYukleniyor.visibility = View.GONE
            }

        }

        }










    fun floatingButtonTiklandi(view: View){
val popup =PopupMenu(requireContext(),binding.floatingActionButton)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu,popup.menu)
        popup.setOnMenuItemClickListener(this)
        popup.show()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        //çıkış yapma
        if(item?.itemId== R.id.exitItem){

            auth.signOut()
            val action= anasayfaFragmentDirections.actionAnasayfaFragmentToKullaniciFragment()
            Navigation.findNavController(requireView()).navigate(action)



        }
        return true

    }


}