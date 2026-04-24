// ============================================================
// KostHub - Manajemen Kos Mahasiswa
// UTS Pemrograman Berorientasi Objek - Tema No. 8
// ============================================================

// ============================================================
// CLASS: Kamar
// ============================================================
class Kamar(
    val nomorKamar: Int,
    val tipeKamar: String,
    val hargaPerBulan: Double
) {
    // DATA HIDING: status tidak bisa diubah sembarangan dari luar
    private var status: String = "Kosong"
    private var penghuniSaatIni: String? = null

    fun getStatus(): String = status
    fun getPenghuni(): String? = penghuniSaatIni

    // CUSTOM SETTER: satu-satunya jalur resmi untuk mengisi kamar
    fun isiKamar(namaPenyewa: String): Boolean {
        if (status == "Terisi") {
            println("[ERROR] Kamar $nomorKamar sudah berstatus 'Terisi' oleh $penghuniSaatIni. Pemesanan GAGAL.")
            return false
        }
        status = "Terisi"
        penghuniSaatIni = namaPenyewa
        println("[SUKSES] Kamar $nomorKamar berhasil dipesan oleh $namaPenyewa.")
        return true
    }

    // CUSTOM SETTER: satu-satunya jalur resmi untuk mengosongkan kamar
    fun kosongkanKamar(): Boolean {
        if (status == "Kosong") {
            println("[ERROR] Kamar $nomorKamar sudah kosong.")
            return false
        }
        println("[INFO] $penghuniSaatIni telah keluar dari kamar $nomorKamar.")
        status = "Kosong"
        penghuniSaatIni = null
        return true
    }

    fun tampilkanInfo() {
        println("  Kamar $nomorKamar | Tipe: $tipeKamar | Harga: Rp${hargaPerBulan}/bln | Status: $status" +
                if (penghuniSaatIni != null) " ($penghuniSaatIni)" else "")
    }
}

// ============================================================
// CLASS: Penyewa
// ============================================================
class Penyewa(
    val nama: String,
    val nim: String,
    saldoAwal: Double
) {
    // DATA HIDING: saldo TIDAK BOLEH diakses/diubah langsung dari luar
    private var saldo: Double = saldoAwal

    fun getSaldo(): Double = saldo

    // CUSTOM SETTER + VALIDASI: jalur resmi bayar sewa
    fun bayarSewa(kamar: Kamar): Boolean {
        // Validasi 1: kamar harus berstatus terisi oleh penyewa ini
        if (kamar.getPenghuni() != this.nama) {
            println("[ERROR] $nama bukan penghuni kamar ${kamar.nomorKamar}. Pembayaran GAGAL.")
            return false
        }
        // Validasi 2: saldo harus mencukupi
        val tagihan = kamar.hargaPerBulan
        if (saldo < tagihan) {
            println("[ERROR] Saldo $nama tidak cukup! Saldo: Rp$saldo, Tagihan: Rp$tagihan. Pembayaran GAGAL.")
            return false
        }
        // Proses pembayaran
        saldo -= tagihan
        println("[SUKSES] $nama berhasil membayar sewa kamar ${kamar.nomorKamar}. " +
                "Tagihan: Rp$tagihan | Sisa Saldo: Rp$saldo")
        return true
    }

    // CUSTOM SETTER + VALIDASI: jalur resmi top up saldo
    fun topUpSaldo(jumlah: Double): Boolean {
        if (jumlah <= 0) {
            println("[ERROR] Jumlah top up tidak valid (harus > 0). Top Up GAGAL.")
            return false
        }
        saldo += jumlah
        println("[SUKSES] Top up Rp$jumlah berhasil. Saldo $nama sekarang: Rp$saldo")
        return true
    }

    fun tampilkanInfo() {
        println("  Penyewa: $nama | NIM: $nim | Saldo: Rp$saldo")
    }
}

// ============================================================
// CLASS: BapakKos
// ============================================================
class BapakKos(
    val nama: String
) {
    // DATA HIDING: pendapatan tidak bisa diubah sembarangan dari luar
    private var totalPendapatan: Double = 0.0
    private val daftarKamar = mutableListOf<Kamar>()

    fun getTotalPendapatan(): Double = totalPendapatan

    // CUSTOM SETTER: tambah kamar lewat jalur resmi
    fun tambahKamar(kamar: Kamar) {
        daftarKamar.add(kamar)
        println("[INFO] Kamar ${kamar.nomorKamar} (${kamar.tipeKamar}) berhasil ditambahkan.")
    }

    // CUSTOM SETTER + VALIDASI: proses pembayaran sewa
    fun prosesPembayaranSewa(penyewa: Penyewa, kamar: Kamar): Boolean {
        val berhasil = penyewa.bayarSewa(kamar)
        if (berhasil) {
            totalPendapatan += kamar.hargaPerBulan
            println("[INFO] Pendapatan $nama bertambah Rp${kamar.hargaPerBulan}. Total: Rp$totalPendapatan")
        }
        return berhasil
    }

    // CUSTOM SETTER + VALIDASI: proses pemesanan kamar
    fun prosesPemesanan(penyewa: Penyewa, nomorKamar: Int): Boolean {
        val kamar = daftarKamar.find { it.nomorKamar == nomorKamar }
        if (kamar == null) {
            println("[ERROR] Kamar $nomorKamar tidak ditemukan. Pemesanan GAGAL.")
            return false
        }
        return kamar.isiKamar(penyewa.nama)
    }

    fun tampilkanSemuaKamar() {
        println("\n====== DAFTAR KAMAR KOS ${nama.uppercase()} ======")
        if (daftarKamar.isEmpty()) {
            println("  Belum ada kamar terdaftar.")
        } else {
            daftarKamar.forEach { it.tampilkanInfo() }
        }
        val terisi = daftarKamar.count { it.getStatus() == "Terisi" }
        println("  Total: ${daftarKamar.size} kamar | Terisi: $terisi | Kosong: ${daftarKamar.size - terisi}")
        println("  Total Pendapatan $nama: Rp$totalPendapatan")
        println("==========================================\n")
    }

    fun getKamar(nomor: Int): Kamar? = daftarKamar.find { it.nomorKamar == nomor }
}

// ============================================================
// MAIN FUNCTION - SIMULASI
// ============================================================
fun main() {
    println("============================================================")
    println("   KOSTHUB - SISTEM MANAJEMEN KOS MAHASISWA")
    println("   UTS Pemrograman Berorientasi Objek - Tema No. 8")
    println("============================================================\n")

    // --- SETUP DATA ---
    val bapakKos = BapakKos("Pak Budi")
    bapakKos.tambahKamar(Kamar(101, "Standard", 800000.0))
    bapakKos.tambahKamar(Kamar(102, "AC", 1200000.0))
    bapakKos.tambahKamar(Kamar(103, "VIP", 1800000.0))

    val penyewa1 = Penyewa("Andi Pratama", "04221001", 2000000.0)
    val penyewa2 = Penyewa("Sari Dewi", "04221002", 500000.0)
    val penyewa3 = Penyewa("Budi Santoso", "04221003", 1500000.0)

    bapakKos.tampilkanSemuaKamar()

    // ============================================================
    // SIMULASI GAGAL 1: Pesan kamar yang sudah terisi
    // ============================================================
    println(">>> SIMULASI GAGAL 1: Pesan kamar yang sudah terisi <<<")
    bapakKos.prosesPemesanan(penyewa1, 101) // Sukses pertama
    bapakKos.prosesPemesanan(penyewa3, 101) // GAGAL - kamar sudah terisi
    println()

    // ============================================================
    // SIMULASI GAGAL 2: Bayar sewa dengan saldo tidak cukup
    // ============================================================
    println(">>> SIMULASI GAGAL 2: Bayar sewa dengan saldo tidak cukup <<<")
    bapakKos.prosesPemesanan(penyewa2, 102) // Pesan kamar 102
    val kamar102 = bapakKos.getKamar(102)!!
    bapakKos.prosesPembayaranSewa(penyewa2, kamar102) // GAGAL - saldo Rp500.000 < tagihan Rp1.200.000
    println()

    // ============================================================
    // SIMULASI GAGAL 3: Bayar sewa kamar orang lain
    // ============================================================
    println(">>> SIMULASI GAGAL 3: Bayar sewa kamar yang bukan miliknya <<<")
    val kamar101 = bapakKos.getKamar(101)!!
    bapakKos.prosesPembayaranSewa(penyewa2, kamar101) // GAGAL - penyewa2 bukan penghuni kamar 101
    println()

    // ============================================================
    // SIMULASI SUKSES 1: Bayar sewa kamar berhasil
    // ============================================================
    println(">>> SIMULASI SUKSES 1: Bayar sewa kamar berhasil <<<")
    bapakKos.prosesPembayaranSewa(penyewa1, kamar101) // SUKSES - saldo Rp2.000.000 cukup
    println()

    // ============================================================
    // SIMULASI SUKSES 2: Top up lalu bayar sewa
    // ============================================================
    println(">>> SIMULASI SUKSES 2: Top up saldo lalu bayar sewa <<<")
    penyewa2.topUpSaldo(1000000.0) // Top up Rp1.000.000 → saldo jadi Rp1.500.000
    bapakKos.prosesPembayaranSewa(penyewa2, kamar102) // SUKSES - saldo sudah cukup
    println()

    // ============================================================
    // SIMULASI GAGAL 4: Top up dengan nominal tidak valid
    // ============================================================
    println(">>> SIMULASI GAGAL 4: Top up dengan nominal tidak valid <<<")
    penyewa3.topUpSaldo(-500000.0) // GAGAL - nominal negatif
    penyewa3.topUpSaldo(0.0)       // GAGAL - nominal nol
    println()

    // ============================================================
    // STATUS AKHIR
    // ============================================================
    println(">>> STATUS AKHIR <<<")
    bapakKos.tampilkanSemuaKamar()
    println("Detail Saldo Penyewa:")
    penyewa1.tampilkanInfo()
    penyewa2.tampilkanInfo()
    penyewa3.tampilkanInfo()
}