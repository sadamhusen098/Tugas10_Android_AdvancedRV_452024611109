package com.example.android.advancedrv.model

/**
 * Model daftar untuk Advanced RecyclerView (Tugas 10).
 *
 * Menggunakan [sealed class] agar adapter dapat membedakan tipe item
 * lewat `getItemViewType()`: Header (1 layout), Promo (layout lebar),
 * dan item buah/sayur biasa (layout kartu).
 *
 * Setiap item memiliki [id] unik yang menjadi dasar komparasi
 * `areItemsTheSame` pada DiffUtil.
 */
sealed class ListItem(open val id: Long) {

    /**
     * Item Header — mengambil seluruh lebar grid (SPAN_COUNT kolom).
     */
    data class Header(
        override val id: Long,
        val emoji: String,
        val title: String,
        val subtitle: String
    ) : ListItem(id)

    /**
     * Item buah/sayur biasa — mengambil 1 kolom.
     */
    data class FruitItem(
        override val id: Long,
        val name: String,
        val emoji: String,
        val category: String,
        val colorRes: Int,
        val price: Int,
        val rating: Float
    ) : ListItem(id)

    /**
     * Item Promo — mengambil 2 kolom (asimetris, hanya 1 baris per 2 item).
     */
    data class PromoItem(
        override val id: Long,
        val title: String,
        val subtitle: String,
        val discount: Int
    ) : ListItem(id)
}
