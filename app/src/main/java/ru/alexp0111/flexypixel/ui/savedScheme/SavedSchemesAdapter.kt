package ru.alexp0111.flexypixel.ui.savedScheme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.alexp0111.flexypixel.databinding.ItemSavedSchemeBinding

class SavedSchemesAdapter(
    private val onItemClicked: (SavedSchemeItem) -> Unit
) : RecyclerView.Adapter<SavedSchemesAdapter.SavedSchemesViewHolder>() {

    private var savedSchemesList: List<SavedSchemeItem> = emptyList()

    inner class SavedSchemesViewHolder(private val binding: ItemSavedSchemeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SavedSchemeItem) {
            binding.schemeName.text = item.name
            binding.itemCard.setOnClickListener {
                onItemClicked.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSchemesViewHolder {
        val itemView =
            ItemSavedSchemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedSchemesViewHolder(itemView)
    }
    override fun getItemCount() = savedSchemesList.size

    override fun onBindViewHolder(holder: SavedSchemesViewHolder, position: Int) {
        val item = savedSchemesList[position]
        holder.bind(item)
    }

    fun updateSavedSchemesList(newList: List<SavedSchemeItem>) {
        savedSchemesList = newList
        notifyDataSetChanged()
    }
}