package com.emreakpinar.filmeet.view

import androidx.fragment.app.Fragment

class MatchFragment : Fragment() {
   /* private var _binding: FragmentMatchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UserAdapter
    private val matchedUsers = arrayListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter(matchedUsers)
        binding.matchRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.matchRecyclerView.adapter = adapter

        findMatches()
    }

    private fun findMatches() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val firestore = Firebase.firestore

        firestore.collection("users")
            .document(currentUser.uid)
            .collection("favorites")
            .get()
            .addOnSuccessListener { currentUserFavorites ->
                val currentUserFavoriteMovies = currentUserFavorites.documents.mapNotNull { it.getString("title") }

                firestore.collection("users")
                    .get()
                    .addOnSuccessListener { users ->
                        matchedUsers.clear()
                        for (user in users) {
                            if (user.id != currentUser.uid) {
                                firestore.collection("users")
                                    .document(user.id)
                                    .collection("favorites")
                                    .get()
                                    .addOnSuccessListener { otherUserFavorites ->
                                        val otherUserFavoriteMovies = otherUserFavorites.documents.mapNotNull { it.getString("title") }
                                        val commonMovies = currentUserFavoriteMovies.intersect(otherUserFavoriteMovies).size
                                        val matchRate = commonMovies.toDouble() / currentUserFavoriteMovies.size.toDouble()
                                        if (matchRate > 0.5) { // Eşleşme oranını %50'nin üzerinde tutun
                                            val matchedUser = user.toObject(User::class.java)
                                            matchedUser?.let { matchedUsers.add(it) }
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                            }
                        }
                    }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    */
}
