package com.example.android.advancedrv

import android.graphics.Bitmap
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

/**
 * Test driver demo untuk Tugas 10.
 * Memanggil performClick() langsung pada tombol — verifikasi logika ListAdapter + DiffUtil
 * tanpa bergantung pada input touch emulator. Setiap langkah menyimpan screenshot ke
 * /sdcard/demo/ sehingga rekaman visual deterministik.
 *
 * Urutan: Tambah -> Acak -> Hapus -> scroll ke Promo -> klik item (Snackbar) -> Reset.
 */
@RunWith(AndroidJUnit4::class)
class DemoDriverTest {

    private val outDir: File by lazy {
        File(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir, "demo")
    }

    /** Screenshot via UiAutomation (butuh izin shell dari instrumentation). */
    private fun shot(tag: String) {
        try {
            outDir.mkdirs()
            val bmp: Bitmap = InstrumentationRegistry.getInstrumentation()
                .uiAutomation.takeScreenshot()
            FileOutputStream(File(outDir, "$tag.png")).use { out ->
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (t: Throwable) {
            android.util.Log.w("DemoShot", "gagal screenshot $tag: $t")
        }
    }

    /** Ambil frame beruntun tiap 250ms selama durasi ms (untuk GIF). */
    private fun frames(ms: Long, tag: String) {
        val end = System.currentTimeMillis() + ms
        var i = 0
        while (System.currentTimeMillis() < end) {
            shot("${tag}_%03d".format(i))
            i++
            Thread.sleep(250)
        }
    }

    @Test
    fun demoSequence() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            var v: DemoViews? = null
            scenario.onActivity { activity ->
                v = DemoViews(
                    add = activity.findViewById(R.id.btn_add),
                    shuffle = activity.findViewById(R.id.btn_shuffle),
                    delete = activity.findViewById(R.id.btn_delete),
                    reset = activity.findViewById(R.id.btn_reset),
                    recycler = activity.findViewById(R.id.recyclerView)
                )
            }
            val views = v ?: error("views null")

            fun click(block: () -> Unit) {
                views.add.post { block() }
                Thread.sleep(700) // tunggu UI thread eksekusi + submitList
            }

            // 1. State awal: grid 3 kolom, header 3-span, promo 2-span, item 1-span
            frames(2500, "01_initial")
            // 2. Tambah item baru (Stroberi) — DiffUtil animasi insert
            click { views.add.performClick() }
            frames(2000, "02_after_add")
            // 3. Acak harga — hanya item berubah yang di-rebind
            click { views.shuffle.performClick() }
            frames(2000, "03_after_shuffle")
            // 4. Hapus satu item acak
            click { views.delete.performClick() }
            frames(2000, "04_after_delete")
            // 5. Scroll ke bawah — Promo (2 span) terlihat
            views.recycler.post { views.recycler.smoothScrollToPosition(views.recycler.adapter!!.itemCount - 1) }
            frames(2500, "05_promo")
            // 6. Scroll balik ke atas, lalu klik item buah — Snackbar detail
            views.recycler.post { views.recycler.smoothScrollToPosition(0) }
            Thread.sleep(1200)
            click {
                val holder = views.recycler.findViewHolderForAdapterPosition(1)
                holder?.itemView?.performClick()
            }
            frames(2200, "06_snackbar")
            // 7. Reset daftar
            click { views.reset.performClick() }
            frames(2000, "07_reset")
        }
    }

    private class DemoViews(
        val add: View,
        val shuffle: View,
        val delete: View,
        val reset: View,
        val recycler: RecyclerView
    )
}
