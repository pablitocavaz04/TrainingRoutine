package com.example.trainingroutine_pablocavaz.data.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.adapters.MemoryGameAdapter
import com.example.trainingroutine_pablocavaz.data.local.MemoryCard
import com.example.trainingroutine_pablocavaz.databinding.FragmentAboutMeBinding

class AboutMeFragment : Fragment() {

    private var _binding: FragmentAboutMeBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var adapter: MemoryGameAdapter
    private var selectedIndices = mutableListOf<Int>()
    private val cardImages = listOf(
        R.drawable.kotlin,
        R.drawable.android,
        R.drawable.xml,
        R.drawable.`as`
    )
    private val cards = mutableListOf<MemoryCard>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutMeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón GitHub personal
        binding.githubButton.setOnClickListener {
            openUrl("https://github.com/pablitocavaz04")
        }

        // Botón GitHub del proyecto
        binding.projectButton.setOnClickListener {
            openUrl("https://github.com/pablitocavaz04/TrainingRoutine.git")
        }

        setupMemoryGame()
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun setupMemoryGame() {
        val images = (cardImages + cardImages).shuffled()
        cards.clear()
        cards.addAll(images.mapIndexed { index, imageRes ->
            MemoryCard(id = index, imageResId = imageRes)
        })

        adapter = MemoryGameAdapter(cards) { position ->
            onCardClicked(position)
        }

        binding.memoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.memoryRecyclerView.adapter = adapter
    }

    private fun onCardClicked(position: Int) {
        val clickedCard = cards[position]
        if (clickedCard.isFlipped || clickedCard.isMatched || selectedIndices.size == 2) return

        clickedCard.isFlipped = true
        selectedIndices.add(position)
        adapter.notifyItemChanged(position)

        if (selectedIndices.size == 2) {
            val first = cards[selectedIndices[0]]
            val second = cards[selectedIndices[1]]

            if (first.imageResId == second.imageResId) {
                first.isMatched = true
                second.isMatched = true
                selectedIndices.clear()

                // Mensaje de victoria si están todas emparejadas
                if (cards.all { it.isMatched }) {
                    binding.memoryRecyclerView.postDelayed({
                        showVictoryToast()
                        setupMemoryGame() // Reiniciar
                    }, 800)
                }

            } else {
                handler.postDelayed({
                    first.isFlipped = false
                    second.isFlipped = false
                    adapter.notifyItemChanged(selectedIndices[0])
                    adapter.notifyItemChanged(selectedIndices[1])
                    selectedIndices.clear()
                }, 1000)
            }
        }
    }

    private fun showVictoryToast() {
        android.widget.Toast.makeText(requireContext(), "¡VICTORIAAA! \uD83C\uDFC6", android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
