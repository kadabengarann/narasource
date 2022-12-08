package id.co.mka.narasource.presentation.article

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.utils.loadImage
import id.co.mka.narasource.databinding.ActivityDetailArticleBinding

@AndroidEntryPoint
class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding
    private val articleViewModel: DetailArticleViewModel by viewModels()

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
        articleViewModel.getDetailArticle(articleId)
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
        articleViewModel.article.observe(this) { article ->
            if (article != null) {
                when (article) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvTitle.text = article.data?.title
                        binding.tvCategory.text = article.data?.category
                        binding.tvWriter.text = HtmlCompat.fromHtml(
                            getString(R.string.written_by, article.data?.author),
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        binding.chipDatePublished.visibility = View.VISIBLE
                        binding.chipDatePublished.text = "23 Januari 2021"
                        binding.ivArticle.visibility = View.VISIBLE
                        binding.ivArticle.loadImage(article.data?.image)
                        binding.tvContent.text =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Html.fromHtml(article.data?.content, Html.FROM_HTML_MODE_COMPACT)
                            } else {
                                HtmlCompat.fromHtml(
                                    article.data?.content.toString(),
                                    HtmlCompat.FROM_HTML_MODE_COMPACT
                                )
                            }
                        sharedContent = "Yo, check this article!\n\n\"${article.data?.title}\"\nhttps://www.narasource.com/article/$articleId"
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
