package com.emreakpinar.filmeet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreakpinar.filmeet.R
import com.emreakpinar.filmeet.ViewModel.anasayfaViewModel
import com.emreakpinar.filmeet.adapter.FilmRecyclerAdapter
import com.emreakpinar.filmeet.adapter.MovieClickListener
import com.emreakpinar.filmeet.databinding.FragmentAnasayfaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class anasayfaFragment : Fragment(), PopupMenu.OnMenuItemClickListener, MovieClickListener {
    private val viewModel: anasayfaViewModel by viewModels()
    private lateinit var filmRecyclerAdapter: FilmRecyclerAdapter

    private var _binding: FragmentAnasayfaBinding? = null
    private val binding get() = _binding!!

    private lateinit var popup: PopupMenu
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnasayfaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filmRecyclerView.layoutManager = LinearLayoutManager(context)
        filmRecyclerAdapter = FilmRecyclerAdapter(emptyList(), this)
        binding.filmRecyclerView.adapter = filmRecyclerAdapter

        viewModel.getMovieList()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.filmRecyclerView.visibility = View.GONE
            binding.filmlerYukleniyor.visibility = View.VISIBLE
            binding.filmHataMesaj.visibility = View.GONE
            viewModel.getMovieList()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        observeLiveData()

        binding.floatingActionButton.setOnClickListener { floatingButtonTiklandi(it) }


        binding.myFavorites.setOnClickListener {
            val action = anasayfaFragmentDirections.actionAnasayfaFragmentToFavoriteFragment()
            findNavController().navigate(action)
        }

        binding.eslesme.setOnClickListener {
            findNavController().navigate(R.id.action_anasayfaFragment_to_usersFragment)
        }
        binding.friends.setOnClickListener {

            findNavController().navigate(R.id.action_usersFragment_to_friendsFragment)

        }
    }

    private fun observeLiveData() {
        viewModel.filmler.observe(viewLifecycleOwner) { list ->
            list?.let {
                if (it.isNotEmpty()) {
                    filmRecyclerAdapter.updateList(it.filterNotNull())
                    binding.filmRecyclerView.visibility = View.VISIBLE
                    binding.filmHataMesaj.visibility = View.GONE
                } else {
                    binding.filmHataMesaj.visibility = View.VISIBLE
                }
            }
        }

        viewModel.filmHataMesaji.observe(viewLifecycleOwner) { isError ->
            binding.filmHataMesaj.visibility = if (isError) View.VISIBLE else View.GONE
        }

        viewModel.filmYukleniyor.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.filmHataMesaj.visibility = View.GONE
                binding.filmRecyclerView.visibility = View.GONE
                binding.filmlerYukleniyor.visibility = View.VISIBLE
            } else {
                binding.filmlerYukleniyor.visibility = View.GONE
            }
        }
    }

    private fun floatingButtonTiklandi(view: View) {
        popup = PopupMenu(requireContext(), binding.floatingActionButton)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu, popup.menu)
        popup.setOnMenuItemClickListener(this)
        popup.show()
    }

    override fun onMovieClicked(movieId: Int?) {
        movieId?.let {
            val action = anasayfaFragmentDirections.actionAnasayfaFragmentToFilmDetayFragment(it)
            findNavController().navigate(action)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.exitItem -> {
                auth.signOut()
                val action = anasayfaFragmentDirections.actionAnasayfaFragmentToKullaniciFragment()
                findNavController().navigate(action)
                return true
            }

        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
