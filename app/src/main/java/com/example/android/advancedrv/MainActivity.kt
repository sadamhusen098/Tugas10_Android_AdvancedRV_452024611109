package com.example.android.advancedrv

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.advancedrv.data.FruitCatalog
import com.example.android.advancedrv.databinding.ActivityMainBinding
import com.example.android.advancedrv.list.FruitListAdapter
import com.example.android.advancedrv.model.ListItem
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FruitListAdapter
    private val items = mutableListOf<ListItem>()

    companion object {
        /** Jumlah kolom grid (item biasa = 1 span, promo = 2 span, header = 3 span). */
        const val SPAN_COUNT = 3
        private const val TAG = "AdvancedRV"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = FruitListAdapter { /* Snackbar detail item ditangani di adapter */ }

        // ===== GridLayoutManager + SpanSizeLookup (ukuran span dinamis) =====
        val layoutManager = GridLayoutManager(this, SPAN_COUNT)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = when (adapter.getItemViewType(position)) {
                FruitListAdapter.TYPE_HEADER -> SPAN_COUNT // 3 span = 1 baris penuh
                FruitListAdapter.TYPE_PROMO -> 2           // 2 span = lebar 2 kolom
                else -> 1                                  // item biasa = 1 kolom
            }
        }

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        // Data awal
        items.addAll(FruitCatalog.build())
        adapter.submitList(items.toList())

        // ===== Tombol demo — setiap aksi memicu DiffUtil di background thread =====
        binding.btnAdd.setOnClickListener {
            Log.d(TAG, "btnAdd CLICKED")
            val name = FruitCatalog.nextExtraItem()
            val insertAt = items.indexOfFirst { it is ListItem.Header && it.id == 200L }
            items.add(insertAt, name)
            adapter.submitList(items.toList())
            Toast.makeText(this, "➕ ${name.name} ditambahkan ke daftar", Toast.LENGTH_SHORT).show()
        }

        binding.btnShuffle.setOnClickListener {
            Log.d(TAG, "btnShuffle CLICKED")
            val updated = FruitCatalog.randomizePrices(items)
            items.clear(); items.addAll(updated)
            adapter.submitList(items.toList())
            Toast.makeText(this, "🔀 Harga diacak — hanya item berubah yang di-rebind", Toast.LENGTH_SHORT).show()
        }

        binding.btnDelete.setOnClickListener {
            Log.d(TAG, "btnDelete CLICKED")
            val candidates = items.filterIsInstance<ListItem.FruitItem>()
            if (candidates.isEmpty()) return@setOnClickListener
            val victim = candidates[Random.nextInt(candidates.size)]
            items.remove(victim)
            adapter.submitList(items.toList())
            Toast.makeText(this, "🗑️ ${victim.name} dihapus", Toast.LENGTH_SHORT).show()
        }

        binding.btnReset.setOnClickListener {
            Log.d(TAG, "btnReset CLICKED")
            items.clear()
            items.addAll(FruitCatalog.build())
            adapter.submitList(items.toList())
            Toast.makeText(this, "🔄 Daftar di-reset", Toast.LENGTH_SHORT).show()
        }
    }
}
