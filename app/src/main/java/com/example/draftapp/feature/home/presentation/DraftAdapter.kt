package com.example.draftapp.feature.home.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.draftapp.R

class DraftAdapter(
    private val onDraftClick: (homeitemstate) -> Unit,
    private val onLockClick: (homeitemstate) -> Unit
) : ListAdapter<homeitemstate, DraftAdapter.DraftViewHolder>(DraftDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_draft, parent, false)
        return DraftViewHolder(view)
    }

    override fun onBindViewHolder(holder: DraftViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DraftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvItemTitle)
        private val tvTag: TextView = itemView.findViewById(R.id.tvItemTag)
        private val tvMeta: TextView = itemView.findViewById(R.id.tvItemMeta)
        private val ivLock: ImageView = itemView.findViewById(R.id.ivLockIcon)
        private val viewAccentBar: View = itemView.findViewById(R.id.viewAccentBar)

        fun bind(item: homeitemstate) {
            tvTitle.text = item.title
            tvMeta.text = item.date
            
            if (item.Locked) {
                tvTag.text = "LOCKED"
                tvTag.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.darker_gray))
                viewAccentBar.setBackgroundColor(ContextCompat.getColor(itemView.context, android.R.color.darker_gray))
                ivLock.setImageResource(R.drawable.bglockicon)
                ivLock.alpha = 1.0f
            } else {
                tvTag.text = "EDITABLE"
                tvTag.setTextColor(ContextCompat.getColor(itemView.context, R.color.black)) // Use a primary color
                viewAccentBar.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.blue))
                ivLock.setImageResource(R.drawable.bglockicon) // Assuming same icon or use unlocked if available
                ivLock.alpha = 0.3f
            }
            
            itemView.setOnClickListener { onDraftClick(item) }
            ivLock.setOnClickListener { onLockClick(item) }
        }
    }

    class DraftDiffCallback : DiffUtil.ItemCallback<homeitemstate>() {
        override fun areItemsTheSame(oldItem: homeitemstate, newItem: homeitemstate): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: homeitemstate, newItem: homeitemstate): Boolean {
            return oldItem == newItem
        }
    }
}
