package com.example.android.advancedrv.binding

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import java.text.NumberFormat
import java.util.Locale

/**
 * Custom Binding Adapter (Tugas 10).
 *
 * Fungsi-fungsi [BindingAdapter] ini dipanggil **langsung dari atribut XML**
 * pada layout item (lihat `app:cardTint`, `app:priceFormat`, `app:ratingText`),
 * sehingga logika presentasi data tidak lagi ditulis manual di ViewHolder —
 * data binding yang mengeksekusinya otomatis saat nilai LiveData/data berubah.
 */

/**
 * Mewarnai kartu (MaterialCardView) berdasarkan resource warna.
 * Dipanggil dari XML: `app:cardTint="@{item.colorRes}"`
 */
@BindingAdapter("app:cardTint")
fun setCardTint(view: MaterialCardView, colorRes: Int) {
    view.setCardBackgroundColor(ContextCompat.getColor(view.context, colorRes))
}

/**
 * Memformat harga angka menjadi mata uang Rupiah (tanpa desimal).
 * Dipanggil dari XML: `app:priceFormat="@{item.price}"`
 *
 * Contoh: 18000 -> "Rp 18.000"
 */
@BindingAdapter("app:priceFormat")
fun setPriceFormat(textView: TextView, price: Int) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.minimumFractionDigits = 0
    formatter.maximumFractionDigits = 0
    textView.text = formatter.format(price.toLong())
}

/**
 * Menampilkan rating dengan satu angka desimal + ikon bintang.
 * Dipanggil dari XML: `app:ratingText="@{item.rating}"`
 */
@BindingAdapter("app:ratingText")
fun setRatingText(textView: TextView, rating: Float) {
    textView.text = String.format(Locale.getDefault(), "★ %.1f", rating)
}
