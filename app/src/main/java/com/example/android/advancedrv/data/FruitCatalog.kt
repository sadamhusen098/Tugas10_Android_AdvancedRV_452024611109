package com.example.android.advancedrv.data

import com.example.android.advancedrv.R
import com.example.android.advancedrv.model.ListItem
import kotlin.random.Random

/**
 * Sumber data contoh "Katalog Buah & Sayur Segar".
 *
 * Dataset di-generate sekali lalu dimutasi dari MainActivity
 * (tambah / acak harga / hapus) — setiap mutasi di-submit ke
 * `ListAdapter.submitList()` sehingga DiffUtil hanya memperbarui
 * item yang benar-benar berubah.
 */
object FruitCatalog {

    private const val ID_HEADER_BUAH = 100L
    private const val ID_HEADER_SAYUR = 200L
    private const val ID_HEADER_PROMO = 300L

    fun build(): List<ListItem> = listOf(
        ListItem.Header(ID_HEADER_BUAH, "🍎", "Buah-Buahan", "6 jenis buah pilihan"),
        ListItem.FruitItem(1L, "Apel", "🍎", "Buah", R.color.fruit_red, 18000, 4.6f),
        ListItem.FruitItem(2L, "Pisang", "🍌", "Buah", R.color.fruit_yellow, 12000, 4.3f),
        ListItem.FruitItem(3L, "Jeruk", "🍊", "Buah", R.color.fruit_orange, 15000, 4.5f),
        ListItem.FruitItem(4L, "Anggur", "🍇", "Buah", R.color.fruit_purple, 32000, 4.8f),
        ListItem.FruitItem(5L, "Mangga", "🥭", "Buah", R.color.fruit_orange, 22000, 4.4f),
        ListItem.FruitItem(6L, "Semangka", "🍉", "Buah", R.color.fruit_red, 8000, 4.1f),

        ListItem.Header(ID_HEADER_SAYUR, "🥦", "Sayuran", "5 jenis sayur segar"),
        ListItem.FruitItem(11L, "Brokoli", "🥦", "Sayur", R.color.fruit_green, 14000, 4.2f),
        ListItem.FruitItem(12L, "Wortel", "🥕", "Sayur", R.color.fruit_orange, 10000, 4.0f),
        ListItem.FruitItem(13L, "Tomat", "🍅", "Sayur", R.color.fruit_red, 9000, 4.1f),
        ListItem.FruitItem(14L, "Bayam", "🥬", "Sayur", R.color.fruit_green, 7000, 4.2f),
        ListItem.FruitItem(15L, "Jagung", "🌽", "Sayur", R.color.fruit_yellow, 11000, 4.3f),

        ListItem.Header(ID_HEADER_PROMO, "🔥", "Promo Spesial", "Diskon s.d. 30%"),
        ListItem.PromoItem(201L, "Paket Hemat Buah", "Apel + Pisang + Jeruk", 30),
        ListItem.PromoItem(202L, "Paket Sayur Rumahan", "Bayam + Wortel + Tomat", 25),
        ListItem.PromoItem(203L, "Paket Jus Segar", "Mangga + Anggur + Semangka", 20)
    )

    private val extraFruits = listOf(
        ListItem.FruitItem(31L, "Stroberi", "🍓", "Buah", R.color.fruit_red, 28000, 4.9f),
        ListItem.FruitItem(32L, "Kiwi", "🥝", "Buah", R.color.fruit_green, 25000, 4.7f),
        ListItem.FruitItem(33L, "Pepaya", "🍈", "Buah", R.color.fruit_yellow, 9500, 4.0f),
        ListItem.FruitItem(34L, "Terong", "🍆", "Sayur", R.color.fruit_purple, 8500, 3.9f),
        ListItem.FruitItem(35L, "Cabai", "🌶️", "Sayur", R.color.fruit_red, 45000, 4.1f),
        ListItem.FruitItem(36L, "Kubis", "🥬", "Sayur", R.color.fruit_green, 6500, 3.8f)
    )

    private var nextExtraIndex = 0

    /** Menghasilkan item baru untuk fitur "Tambah Item" (disisipkan acak). */
    fun nextExtraItem(): ListItem.FruitItem {
        val item = extraFruits[nextExtraIndex % extraFruits.size]
        nextExtraIndex++
        return item
    }

    /** Mengacak harga beberapa item buah/sayur untuk demo DiffUtil (content change). */
    fun randomizePrices(items: List<ListItem>): List<ListItem> = items.map { item ->
        if (item is ListItem.FruitItem && Random.nextInt(100) < 60) {
            val newPrice = ((item.price * (0.8 + Random.nextDouble() * 0.5)) / 500).toInt() * 500
            item.copy(price = newPrice)
        } else {
            item
        }
    }
}
