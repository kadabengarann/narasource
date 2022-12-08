package id.co.mka.narasource.presentation.terms

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import id.co.mka.narasource.R
import id.co.mka.narasource.databinding.FragmentTermsBinding

class TermsFragment : Fragment() {

    private var _binding: FragmentTermsBinding? = null
    private val binding get() = _binding

    private var termsType = "user"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTermsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.title = ""
        }
        setHasOptionsMenu(true)

        termsType = TermsFragmentArgs.fromBundle(arguments as Bundle).type
        setupContent()
        setupAction()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.close_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_close -> {
                requireActivity().onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAction() {
        binding?.apply {
            btnAccept.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setupContent() {
        if (termsType == "user") {
            binding?.apply {
                tvContentTitle.text = "Syarat & Ketentuan"
                tvContent.text = "Selamat datang di Narasource\n" +
                    "Dengan mengunduh, memasang, dan/atau menggunakan Platform Narasource (sesuai yang didefinisikan di bawah ini), Anda menyetujui bahwa Anda telah membaca, memahami, dan menyetujui Syarat dan Ketentuan (“Syarat dan Ketentuan”) ini.\n" +
                    "Syarat dan Ketentuan ini menandakan perjanjian hukum antara Anda dan NaraSource dan berlaku untuk kunjungan Anda ke dan penggunaan Anda atas Platform dan Layanan kami (sesuai yang didefinisikan di bawah ini). Namun, harap diperhatikan bahwa syarat, batasan, dan kebijakan privasi tambahan dapat berlaku.\n" +
                    "Jika Anda tidak menyetujui Syarat dan Ketentuan ini, jangan mengakses dan/atau menggunakan Platform dan/atau Layanan ini.\n" +
                    "Peran platform ini adalah memfasilitasi komunikasi antara Anda dengan para Narasumber/pakar yang terpercaya dalam bentuk layanan wawancara. Anda memiliki hak untuk mengkonsiderasi dan memilih apabila layanan ini sesuai atau tidak untuk Anda.\n" +
                    "· Narasumber tidak diperuntukan dan berhak menolak untuk pertanyaan di luar topik dari bidang Narasumber.\n" +
                    "· Narasumber dari Narasource dapat menolak memberikan jawaban atau melakukan pertemuan bilamana terdapat potensi penyalahgunaan layanan.\n" +
                    "· Pengguna mengetahui bahwa Narasumber akan memberikan wawancara sesuai dengan data Pengguna yang didaftarkan pada saat registrasi dan informasi yang telah disampaikan.\n" +
                    "· Narasource bukan merupakan penyedia layanan , maka Narasource tidak mempekerjakan pakar yang tergabung dalam platform sebagai penyedia layanan, dan kami tidak bertanggung jawab atas tindakan, kecerobohan, kelalaian, dan/atau kelengahan penyedia layanan. Platform merupakan layanan informasi dan komunikasi online yang disediakan oleh Narasource.\n" +
                    "A. DEFINISI\n" +
                    "“Kami” atau “Narasource“, berarti pemilik, pengembang, dan pengelola situs www.NaraSource.com, dan berbagai Platform lainya.\n" +
                    "“Anda“, berarti setiap orang yang mengakses dan menggunakan layanan dan jasa yang disediakan oleh Kami.\n" +
                    "“Penyedia Layanan” merupakan: i) Narasumber profesional yang memiliki portofolio yang akurat serta mengikuti peraturan berkaitan Peraturan Menteri Dalam Negeri (Permendagri) yaitu dalam Pasal 1 Angka 13 Permendagri No. 33 Tahun 2007 Berdasarkan pedoman penyelenggaraan penelitian dan pengembangan di lingkungan Departemen Dalam Negeri dan Pemerintahan Daerah, tenaga ahli atau narasumber adalah orang yang memiliki kompetensi di bidang ilmu atau keahlian tertentu.\n" +
                    "Kami melakukan verifikasi dan seleksi terhadap Narasumber kami untuk memastikan kualifikasinya; dan/atau ii) individu (pakar) yang memiliki latar belakang pendidikan sesuai dan portofolio yang akurat sesuai bidangnya.\n" +
                    "“Pengguna“, merupakan individu yang memiliki hak untuk mengadakan perjanjian terikat berdasarkan hukum Republik Indonesia untuk menggunakan layanan wawancara yang disediakan oleh Kami.\n" +
                    "“Platform” merupakan segala produk di bidang teknologi informasi yang dikembangkan dan dikelola oleh Narasource, termasuk namun tidak terbatas pada segala bentuk fitur, layanan, konten, ataupun informasi yang terdapat dalam aplikasi dan situs Narasource, seperti layanan wawancara dalam bentuk pertemuan Daring, layanan, konten, ataupun informasi yang terdapat dalam aplikasi dan situs NaraSource sebagaimana disebutkan di dalam Perjanjian ini dapat berubah dari waktu ke waktu, berdasarkan keputusan internal Narasource.\n" +
                    "“Informasi Pribadi“, berarti tiap dan seluruh data pribadi yang diberikan oleh Pengguna Platform Narasource, termasuk namun tidak terbatas pada nama, nomor identifikasi, data konsultasi, lokasi pengguna, kontak pengguna, serta dokumen dan data lainnya sebagaimana diminta pada formulir pendaftaran akun atau formulir lainnya pada saat menggunakan Platform Narasource.\n" +
                    "B. LAYANAN\n" +
                    "Jenis-jenis layanan yang dapat diakses melalui Platform adalah sebagai berikut:\n" +
                    "Konten Narasource. Konten Narasource yang ditawarkan di Platform Narasource hanya bersifat umum dan digunakan untuk memberikan informasi kepada Customer. Bagian mana pun dalam konten sebaiknya tidak dianggap atau digunakan sebagai pengganti untuk saran Narasources.\n" +
                    "Wawancara dalam bentuk pertemuan Daring, dengan pilihan topik sesuai dengan bidang dari Narasumber.\n" +
                    "Layanan lain apa pun yang dapat kami tambahkan dari waktu ke waktu.\n" +
                    "Informasi yang terdapat dalam Platform Narasource ditampilkan sesuai keadaan kenyataan untuk tujuan informasi umum. Kami berusaha untuk selalu menyediakan dan menampilkan informasi yang terbaru dan akurat, namun Kami tidak menjamin bahwa segala informasi sesuai dengan ketepatan waktu atau relevansi dengan kebutuhan Anda.\n" +
                    "Layanan yang Kami berikan tidak bersifat memaksa atau mengikat, serta tidak mengharuskan Anda untuk mengikuti informasi yang tersedia. Tidak ada situasi atau keadaan apapun yang dapat membuat Kami dikenakan tanggung jawab atas kemungkinan kerugian yang Anda alami karena pengambilan keputusan yang dilakukan oleh Anda sendiri terkait tindakan atas informasi produk yang kami sampaikan di Platform Narasource.\n" +
                    "C. PENGGUNAAN LAYANAN\n" +
                    "Dengan Anda melanjutkan penggunaan atau pengaksesan Platform Narasource, berarti Anda telah menyatakan serta menjamin kepada Kami bahwa:\n" +
                    "Anda hanya diperkenankan untuk mengakses dan/atau menggunakan Platform NaraSource untuk keperluan pribadi dan non-komersil, yang berarti bahwa Platform NaraSource hanya boleh diakses dan digunakan secara langsung oleh Pengguna. Akses dan penggunaan Platform NaraSource diluar dari keperluan pribadi atau non-komersil dilarang keras, dan melanggar Syarat dan Ketentuan ini.\n" +
                    "Anda menyatakan dan menjamin bahwa Anda adalah individu yang secara hukum berhak untuk mengadakan perjanjian yang mengikat berdasarkan hukum Negara Republik Indonesia. Jika tidak, Kami atau Penyedia Layanan berhak berdasarkan hukum untuk membatalkan perjanjian yang dibuat dengan Anda.\n" +
                    "Anda tidak diperkenankan untuk menanyakan hal-hal yang bersifat pribadi kepada Penyedia Layanan (termasuk di dalamnya adalah nomor telepon, selular, dan e-mail dari Penyedia Layanan).\n" +
                    "Anda tidak diperkenankan untuk, serta berjanji untuk tidak melakukan hal-hal berikut ini:\n" +
                    "Mengalihkan akun Pengguna di Platform Narasource kepada pihak ketiga lainnya tanpa persetujuan tertulis Narasource.\n" +
                    "Menyebarkan virus, spam atau teknologi sejenis lainnya yang dapat merusak dan/atau merugikan Narasource dan/atau Pengguna lainnya.\n" +
                    "Menyakiti, menyiksa, mempermalukan, memfitnah, mencemarkan nama baik, mengancam, mengintimidasi atau mengganggu orang atau bisnis lain, atau apapun yang melanggar privasi atau yang Kami anggap cabul, menghina, penuh kebencian, tidak senonoh, tidak patut, tidak pantas, tidak dapat diterima, mendiskriminasikan atau merusak.\n" +
                    "Menggunakan Platform Narasource untuk tujuan pelanggaran hukum, tindakan penipuan atau tindakan komersil.\n" +
                    "Menggunakan Platform Narasource untuk mendistribusikan iklan atau materi lainnya yang tidak diinginkan atau diizinkan oleh Narasource.\n" +
                    "Melanggar atau menyalahi hak orang lain, termasuk tanpa kecuali: hak paten, merek dagang, hak cipta, rahasia dagang, publisitas, dan hak milik lainnya.\n" +
                    "Membuat, memeriksa, memperbarui, mengubah atau memperbaiki database, rekaman atau direktori Anda ataupun orang lain.\n" +
                    "Mengambil atau mengumpulkan data pribadi dari Pengguna lain, termasuk namun tidak terbatas pada alamat e-mail, tanpa persetujuan Pengguna tersebut.\n" +
                    "Memasukkan atau memindahkan fitur pada Platform Narasource tanpa persetujuan tertulis dari Narasource.\n" +
                    "Menempatkan informasi atau aplikasi lain yang melanggar hak kekayaan intelektual pihak ketiga lainnya di dalam Platform Narasource.\n" +
                    "Mengubah atau mengatur ulang bagian apapun dalam Platform Narasource yang akan mengganggu atau menaruh beban berlebihan pada sistem komunikasi dan teknis kami.\n" +
                    "Menggunakan kode komputer otomatis, proses, program, robot, net crawler, spider, pemrosesan data, trawling atau kode computer, proses, program atau sistem ‘screen scraping’ alternatif.\n" +
                    "Melanggar Syarat dan Ketentuan, atau petunjuk lainnya yang ada pada Platform Narasource.\n" +
                    "Kami akan menjamin keamanan dan kerahasiaan Informasi Pribadi Anda, termasuk di dalamnya segala bentuk pertemuan secara daring.\n" +
                    "Anda memberikan Kami lisensi non-eksklusif untuk menggunakan materi atau informasi yang Anda kirimkan di Platform dan/atau berikan kepada Kami, termasuk tetapi tidak terbatas pada pertanyaan, ulasan, komentar, dan saran.\n" +
                    "Kami tidak bertanggung jawab atas cedera, kematian atau kerugian langsung maupun tidak langsung yang disebabkan oleh interaksi atau Penyedia Layanan dengan Pengguna. Narasource juga tidak bertanggung jawab atas kesalahan atau tindakan kriminal yang dilakukan oleh para Penyedia Layanan selama pemberian jasa atau layanan melalui Platform Narasource. Dalam hal ini, Penyedia Layanan hanyalah merupakan mitra kerja Narasource.\n" +
                    "Kami tidak bertanggung jawab atas kehilangan atau kerusakan yang di luar perkiraan saat Pengguna mengakses atau menggunakan Platform Narasource. Ini termasuk kehilangan penghematan yang diharapkan, kerugian finansial atau fisik karena adanya kesalahan dalam pengambilan tindakan oleh Penyedia Layanan atau kehilangan atau kerusakan dalam bentuk apapun yang Anda harus alami akibat penggunaan Platform Narasource.\n" +
                    "D. HAK KEKAYAAN INTELEKTUAL\n" +
                    "Semua Hak Kekayaan Intelektual yang ada di dalam Platform Narasource adalah kepunyaan dari Kami.\n" +
                    "Tiap atau keseluruhan informasi dan materi dan konten, termasuk tetapi tidak terbatas pada, tulisan, perangkat lunak, teks, data, grafik, gambar, audio, video, logo, ikon atau kode-kode html dan kode-kode lain yang ada di Platform Narasource dilarang dipublikasikan, dimodifikasi, disalin, digandakan atau diubah dengan cara apapun di luar area Platform Narasource tanpa persetujuan tertulis dari Kami.\n" +
                    "Pelanggaran terhadap hak-hak dalam Platform Narasource dapat ditindak sesuai dengan peraturan yang berlaku.\n" +
                    "Anda dapat menggunakan informasi atau isi dalam Platform Narasource hanya untuk penggunaan pribadi non-komersil. Kecuali ditentukan sebaliknya dan/atau diperbolehkan secara tegas oleh undang-undang hak cipta, maka Pengguna dilarang untuk menyalin, membagikan ulang, mentransmisi ulang, mempublikasi atau melakukan tindakan eksploitasi komersial dari pengunduhan yang dilakukan tanpa persetujuan tertutlis pemilik Hak Kekayaan Intelektual tersebut. Dalam hal Pengguna telah mendapatkan izin yang diperlukan maka Pengguna dilarang melakukan perubahan atau penghapusan. Pengguna dengan ini menyatakan menerima dan mengetahui bahwa dengan mengunduh materi Hak Kekayaan Intelektual bukan berarti mendapatkan hak kepemilikan atas pengunduhan materi Hak Kekayaan Intelektual tersebut.\n" +
                    "E. PENUTUP\n" +
                    "Penggunaan dan akses ke Platform Narasource diatur oleh Syarat dan Ketentuan serta Kebijakan Privasi Kami. Dengan mengakses atau menggunakan Platform Narasource, berarti Anda telah memahami dan menyetujui serta terikat dan tunduk dengan segala syarat dan ketentuan yang berlaku.\n" +
                    "Hubungan antara Narasource dan Pengguna merupakan suatu hubungan independen dan tidak memiliki hubungan keagenan, kemitraan, usaha patungan, karyawan-perusahaan atau pemilik waralaba-pewaralaba yang akan dibuat atau dibuat dengan adanya Perjanjian ini.\n" +
                    "Judul di dalam Perjanjian ini dibuat hanya sebagai acuan, dan sama sekali tidak menetapkan, membatasi, menjelaskan atau menjabarkan apa yang termaktub dalam pasal atau ketentuan tersebut.\n" +
                    "Kami berhak untuk menutup atau mengubah atau memperbaharui Syarat dan Ketentuan ini setiap saat tanpa pemberitahuan, dan berhak untuk membuat keputusan akhir jika tidak ada ketidakcocokan. Kami tidak bertanggung jawab atas kerugian dalam bentuk apa pun yang timbul akibat perubahan pada Syarat dan Ketentuan.\n" +
                    "Kami memiliki hak untuk menginvestigasi dan menuntut hak untuk setiap pelanggaran atas Perjanjian ini sepanjang yang dimungkinkan dan diperkenankan oleh hukum.\n" +
                    "Pengguna dengan ini mengakui bahwa NaraSource memiliki hak untuk memonitor akses penggunaan Platform NaraSource untuk memastikan kepatuhan para pihak atas Perjanjian ini, atau untuk mematuhi peraturan yang berlaku atau perintah atau persyaratan dari pengadilan, lembaga administratif atau badan pemerintahan lainnya di Republik Indonesia.\n" +
                    "Apabila terdapat 1 (satu) atau lebih ketentuan di dalam Perjanjian ini yang dinyatakan tidak berlaku, melawan hukum atau tidak dapat dilaksanakan berdasarkan ketentuan perundang-undangan yang berlaku di Republik Indonesia maka keadaan tersebut tidak akan mempengaruhi keberlakuan ketentuan-ketentuan lain dalam Perjanjian ini. Dalam hal terjadi keadaan tersebut, para pihak berjanji untuk tetap berusaha memenuhi segala ketentuan yang terdapat di dalam Perjanjian ini.\n" +
                    "Segala sengketa yang berkaitan dengan Perjanjian ini, akan pertama-tama diselesaikan secara musyawarah untuk mufakat oleh para pihak. Dalam hal penyelesaian secara musyawarah untuk mufakat tidak dapat dilakukan, para pihak memutuskan untuk menyelesaikan konflik yang terjadi melalui Badan Arbitrase Nasional Indonesia (BANI) sesuai dengan prosedur yang berlaku di BANI.\n" +
                    "Perjanjian ini diatur, ditafsirkan, serta dilaksanakan berdasarkan hukum yang berlaku di Republik Indonesia dan para pihak diwajibkan untuk tunduk kepada semua peraturan yang berlaku di Indonesia.\n"
            }
        } else {
            binding?.apply {
                tvContentTitle.text = "Syarat Calon Narasumber"
                tvContent.text = "Selamat datang di NaraSource!\n" +
                    "Dengan mendaftarkan diri sebagai Narasumber menggunakan Platform NaraSource (sesuai yang didefinisikan di bawah ini), Anda menyetujui bahwa Anda telah membaca, memahami, dan menyetujui Syarat dan Ketentuan (“Syarat dan Ketentuan”) ini.\n" +
                    "Syarat Menjadi Narasumber Untuk menjadi seorang narasumber dalam platform ini, Anda Perlu memperhatikan syarat-syarat yang harus dipenuhi oleh seorang narasumber di bawah ini:\n" +
                    "SYARAT DAN KETENTUAN:\n" +
                    "· Calon Narasumber Memiliki portofolio yang valid sesuai di bidang yang ditekuni\n" +
                    "· Calon Narasumber memiliki sertifikat yang valid sesuai bidang yang ditekuni\n" +
                    "· Calon Narasumber harus berpengalaman di bidangnya\n" +
                    "· Calon Narasumber harus mempunyai wawasan yang luas\n" +
                    "· Calon Narasumber mampu berkomunikasi dengan baik\n" +
                    "· Calon Narasumber tidak menuntut untuk selalu mendapatkan Customer\n" +
                    "· Calon Narasumber harus bertanggung jawab\n" +
                    "· Calon Narasumber harus bersedia dikenakan biaya pajak\n" +
                    "· Calon Narasumber Memiliki pemahaman yang ada di bidangnya\n" +
                    "· Calon narasumber harus dapat menyampaikan informasi dengan metode yang menarik.\n" +
                    "· Calon Narasumber harus Bertutur kata dengan sopan kepada customer\n" +
                    "· Calon Narasumber harus Bersikap objektif dan jujur\n" +
                    "· Calon Narasumber harus Mampu memanfaatkan kemajuan teknologi.\n" +
                    "· Calon Narasumber harus Disiplin waktu\n" +
                    "· Calon Narasumber harus Responsif\n" +
                    "· Calon harus Komunikatif dan interaktif\n" +
                    "· Calon Narasumber harus memiliki device yang mendukung penggunaan aplikasi Narasource dengan baik\n" +
                    "· Calon Narasumber bersedia untuk melakukan wawancara sesuai janji dengan Customer\n" +
                    "Dalam platform NaraSource ini Anda sebagai Narasumber akan diberikan kesempatan untuk menjadi Narasumber ketika Customer mencari sesuai kriteria yang diinginkan Customer, maka sistem dari Aplikasi yang menentukan Narasumber. Semakin sering Anda menerima permintaan Customer maka semakin sering Anda disarankan untuk menjadi Narasumber di dalam platform NaraSource. Pembayaran untuk Narasumber ini akan di transferkan setelah wawancara telah selesai. Pembiayaannya permenit sebagai berikut ;\n" +
                    "- 10 menit = Rp.10.000.\n" +
                    "- 30 menit = Rp.30.000\n" +
                    "- 1 jam = Rp.50.000\n" +
                    "Aplikasi NaraSource ini akan menentukan Narasumber yang mana akan melakukan wawancara terhadap Customer sesuai penilaian dari Portofolio, sertifikat dan syarat yang sudah dipenuhi oleh Calon Narasumber. NaraSource akan memaksimalkan untuk menentukan Narasumber yang cocok sesuai yang dibutuhkan oleh Customer.\n"
            }
        }
    }
}
