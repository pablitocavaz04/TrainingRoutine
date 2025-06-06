package com.example.trainingroutine_pablocavaz.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.local.MemoryCard

class MemoryGameAdapter(
    private val cards: List<MemoryCard>,
    private val onCardClick: (Int) -> Unit
) : RecyclerView.Adapter<MemoryGameAdapter.CardViewHolder>() {

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.card_image)

        init {
            view.setOnClickListener {
                onCardClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memory_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]

        if (card.isMatched) {
            holder.image.alpha = 0.3f
        } else {
            holder.image.alpha = 1f
        }

        if (card.isFlipped || card.isMatched) {
            holder.image.setImageResource(card.imageResId)
        } else {
            holder.image.setImageResource(R.drawable.ic_memory_back) // Imagen de dorso
        }
    }

    override fun getItemCount(): Int = cards.size
}