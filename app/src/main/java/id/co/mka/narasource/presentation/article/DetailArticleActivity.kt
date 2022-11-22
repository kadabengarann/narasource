package id.co.mka.narasource.presentation.article

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.databinding.ActivityDetailArticleBinding

@AndroidEntryPoint
class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding

    private lateinit var articleId: String
    private lateinit var sharedContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get data from navArgs
        articleId = DetailArticleActivityArgs.fromBundle(intent.extras as Bundle).idArticle.toString()

        // set toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        // set data
        setupData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.article_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.menu_share -> {
                // share
                val intent = Intent(Intent.ACTION_SEND, Uri.parse(sharedContent))
                intent.putExtra(Intent.EXTRA_TEXT, sharedContent)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share With:"))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setupData() {
        sharedContent = "Yo, check this article!\n\n\"$articleId\"\nhttps://www.narasource.com/article/$articleId"

        binding.tvTitle.text = articleId
        binding.tvWriter.text = HtmlCompat.fromHtml(
            getString(R.string.written_by, articleId),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        val html = "<h5>Kamu tau nggak ui/ux itu kerjaannya apa aja?</h5><br><i>Yuk simak penjelasannya</i><br><br><p>“User Interface (UI) Designer tidak bisa dipisahkan dengan User Experience (UX) Designer. UI Designer memiliki tugas menentukan tampilan aplikasi dan/atau situs. Nah kalau UX Designer menentukan bagaimana suatu aplikasi dan/atau situs bisa beroperasi dengan mudah. Tapi dalam bekerja, keduanya harus berlandaskan pada hasil riset supaya aplikasi dan/atau situs yang dirancang benar-benar efektif.\n" +
            "<br><br>Jadi, UI Designer merupakan sebutan untuk orang yang mendesain interface untuk perangkat lunak komputer, ponsel pintar, dan lainnya. UI Designer punya tanggung jawab untuk mendesain tampilan secara menarik baik dari sisi bentuk, warna, juga tulisan. Nggak heran kalau UI Designer akan bekerja mengatur tata letak, skema warna, bentuk tombol-tombol yang bisa diklik beserta jenis dan ukuran teks. Nah, semua item ini berinteraksi sama pengguna. Pokoknya semua elemen visual di sebuah aplikasi dan/atau situs menjadi tanggung jawab UI Designer.\n</p>"
        binding.tvContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(html)
        }
    }
}
