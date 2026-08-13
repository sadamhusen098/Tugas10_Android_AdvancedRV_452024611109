package com.example.android.advancedrv.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.advancedrv.databinding.ItemFruitBinding
import com.example.android.advancedrv.databinding.ItemHeaderBinding
import com.example.android.advancedrv.databinding.ItemPromoBinding
import com.example.android.advancedrv.model.ListItem
import com.google.android.material.snackbar.Snackbar

/**
 * Adapter utama (Tugas 10) — berbasis [ListAdapter] + [DiffUtil.ItemCallback].
 *
 * Keunggulan dibanding `RecyclerView.Adapter` standar:
 *  - `submitList()` menjalankan komputasi perbedaan data (diff) di **background thread**
 *    (AsyncListDiffer), menghasilkan kumpulan perubahan minimal (insert/remove/move/change),
 *    lalu RecyclerView hanya me-rebind + menganimasi item yang benar-benar berubah —
 *    tidak membuang & membangun ulang seluruh item seperti `notifyDataSetChanged()`.
 *
 * Adapter ini juga mendukung **Multiple Item View Types** lewat override
 * [getItemViewType] dan [onCreateViewHolder] yang memilih layout berbeda:
 *  - TYPE_HEADER (0) -> item_header.xml, mengambil 3 span (1 baris penuh)
 *  - TYPE_PROMO  (1) -> item_promo.xml,  mengambil 2 span
 *  - TYPE_ITEM   (2) -> item_fruit.xml,  mengambil 1 span
 */
class FruitListAdapter(
    private val onClick: (ListItem) -> Unit
) : ListAdapter<ListItem, RecyclerView.ViewHolder>(DiffCallback) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is ListItem.Header -> TYPE_HEADER
        is ListItem.PromoItem -> TYPE_PROMO
        else -> TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_HEADER -> HeaderViewHolder.from(parent)
            TYPE_PROMO -> PromoViewHolder.from(parent)
            else -> FruitViewHolder.from(parent)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is HeaderViewHolder -> holder.bind(item as ListItem.Header)
            is PromoViewHolder -> holder.bind(item as ListItem.PromoItem)
            is FruitViewHolder -> holder.bind(item as ListItem.FruitItem) {
                Snackbar.make(
                    holder.itemView,
                    "${it.emoji} ${it.name} — ${it.category}, Rp ${it.price}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_PROMO = 1
        const val TYPE_ITEM = 2

        /**
         * DiffUtil.ItemCallback — logika pengecekan perubahan data.
         *
         * - [areItemsTheSame]   : membandingkan **identitas** (id unik). Jika true,
         *   kedua item dianggap "objek yang sama" meskipun isinya berubah.
         * - [areContentsTheSame]: membandingkan **konten** (data class equality).
         *   Hanya dipanggil untuk pasangan item yang areItemsTheSame = true.
         */
        private val DiffCallback = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                oldItem == newItem
        }
    }
}

/**
 * ViewHolder Header — konstruktor **private** + metode factory [from] di
 * dalam companion object (pola "clean binding" yang diminta pada tugas).
 */
class HeaderViewHolder private constructor(
    private val binding: ItemHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ListItem.Header) {
        binding.header = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): HeaderViewHolder {
            val binding = ItemHeaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            return HeaderViewHolder(binding)
        }
    }
}

/**
 * ViewHolder item buah/sayur — konstruktor private + factory [from].
 */
class FruitViewHolder private constructor(
    private val binding: ItemFruitBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ListItem.FruitItem, onItemClick: (ListItem.FruitItem) -> Unit) {
        binding.item = item
        binding.root.setOnClickListener { onItemClick(item) }
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): FruitViewHolder {
            val binding = ItemFruitBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            return FruitViewHolder(binding)
        }
    }
}

/**
 * ViewHolder Promo — konstruktor private + factory [from].
 */
class PromoViewHolder private constructor(
    private val binding: ItemPromoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ListItem.PromoItem) {
        binding.item = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): PromoViewHolder {
            val binding = ItemPromoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            return PromoViewHolder(binding)
        }
    }
}
