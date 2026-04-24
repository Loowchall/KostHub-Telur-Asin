// Main.kt — KostHub Sistem Manajemen Kos (Menu Interaktif)

data class Kamar(
    val nomor: Int,
    val tipe: String,
    val harga: Double
)

class Penghuni(
    val nama: String,
    val nim: String,
    private var saldo: Double
) {
    fun getSaldo(): Double = saldo

    fun bayarSewa(jumlah: Double): Boolean {
        if (jumlah <= 0) {
            println("Error: Jumlah bayar tidak valid")
            return false
        }
        if (saldo < jumlah) {
            println("Saldo tidak cukup! Saldo: Rp${saldo}, Tagihan: Rp${jumlah}")
            return false
        }
        saldo -= jumlah
        println("Pembayaran berhasil! Sisa saldo: Rp${saldo}")
        return true
    }

    fun topUp(jumlah: Double) {
        if (jumlah > 0) {
            saldo += jumlah
            println("Top up berhasil! Saldo baru: Rp${saldo}")
        }
    }
}

class KostManager {
    private val daftarKamar = mutableListOf<Kamar>()
    private val daftarPenghuni = mutableMapOf<Int, Penghuni>()

    fun tambahKamar(kamar: Kamar) {
        daftarKamar.add(kamar)
        println("Kamar ${kamar.nomor} (${kamar.tipe}) berhasil ditambahkan")
    }

    fun daftarkanPenghuni(nomorKamar: Int, penghuni: Penghuni): Boolean {
        val kamar = daftarKamar.find { it.nomor == nomorKamar }
        if (kamar == null) {
            println("Kamar $nomorKamar tidak ditemukan")
            return false
        }
        if (daftarPenghuni.containsKey(nomorKamar)) {
            println("Kamar $nomorKamar sudah ditempati")
            return false
        }
        daftarPenghuni[nomorKamar] = penghuni
        println("${penghuni.nama} berhasil masuk kamar $nomorKamar")
        return true
    }

    fun keluarkanPenghuni(nomorKamar: Int): Boolean {
        val penghuni = daftarPenghuni.remove(nomorKamar)
        return if (penghuni != null) {
            println("${penghuni.nama} telah keluar dari kamar $nomorKamar")
            true
        } else {
            println("Tidak ada penghuni di kamar $nomorKamar")
            false
        }
    }

    fun prosesPembayaran(nomorKamar: Int) {
        val penghuni = daftarPenghuni[nomorKamar]
        val kamar = daftarKamar.find { it.nomor == nomorKamar }
        if (penghuni != null && kamar != null) {
            print("Tagihan sewa kamar $nomorKamar untuk ${penghuni.nama}: ")
            penghuni.bayarSewa(kamar.harga)
        } else {
            println("Data tidak ditemukan untuk kamar $nomorKamar")
        }
    }

    fun topUpPenghuni(nomorKamar: Int, jumlah: Double) {
        val penghuni = daftarPenghuni[nomorKamar]
        if (penghuni != null) {
            penghuni.topUp(jumlah)
        } else {
            println("Penghuni di kamar $nomorKamar tidak ditemukan")
        }
    }

    fun cekSaldo(nomorKamar: Int) {
        val penghuni = daftarPenghuni[nomorKamar]
        if (penghuni != null) {
            println("Saldo ${penghuni.nama}: Rp${penghuni.getSaldo()}")
        } else {
            println("Penghuni di kamar $nomorKamar tidak ditemukan")
        }
    }

    fun tampilkanStatus() {
        println("\n===== STATUS KOSTHUB TELUR ASIN =====")
        if (daftarKamar.isEmpty()) {
            println("Belum ada kamar terdaftar")
        } else {
            daftarKamar.forEach { kamar ->
                val penghuni = daftarPenghuni[kamar.nomor]
                val status = if (penghuni != null) "Terisi - ${penghuni.nama}" else "Kosong"
                println("Kamar ${kamar.nomor} (${kamar.tipe}) Rp${kamar.harga}/bln -> $status")
            }
        }
        val terisi = daftarPenghuni.size
        val kosong = daftarKamar.size - terisi
        println("Total: ${daftarKamar.size} kamar | Terisi: $terisi | Kosong: $kosong")
        println("=====================================\n")
    }
}

fun tampilkanMenu() {
    println("╔════════════════════════════════╗")
    println("║    KOSTHUB TELUR ASIN MENU     ║")
    println("╠════════════════════════════════╣")
    println("║ 1. Tambah Kamar                ║")
    println("║ 2. Daftarkan Penghuni          ║")
    println("║ 3. Keluarkan Penghuni          ║")
    println("║ 4. Proses Pembayaran           ║")
    println("║ 5. Top Up Saldo                ║")
    println("║ 6. Cek Saldo                   ║")
    println("║ 7. Lihat Status Kamar          ║")
    println("║ 0. Keluar                      ║")
    println("╚════════════════════════════════╝")
    print("Pilih menu: ")
}

fun main() {
    val kost = KostManager()
    val scanner = java.util.Scanner(System.`in`)

    // Data awal
    kost.tambahKamar(Kamar(101, "Standard", 800000.0))
    kost.tambahKamar(Kamar(102, "AC", 1200000.0))
    kost.tambahKamar(Kamar(103, "VIP", 1800000.0))

    println("\nSelamat datang di KostHub Telur Asin!")

    var pilihan: Int
    do {
        tampilkanMenu()
        pilihan = scanner.nextInt()
        scanner.nextLine()

        when (pilihan) {
            1 -> {
                println("\n--- Tambah Kamar ---")
                print("Nomor kamar: ")
                val nomor = scanner.nextInt()
                scanner.nextLine()
                print("Tipe kamar (Standard/AC/VIP): ")
                val tipe = scanner.nextLine()
                print("Harga per bulan: Rp")
                val harga = scanner.nextDouble()
                scanner.nextLine()
                kost.tambahKamar(Kamar(nomor, tipe, harga))
            }
            2 -> {
                println("\n--- Daftarkan Penghuni ---")
                print("Nomor kamar: ")
                val nomorKamar = scanner.nextInt()
                scanner.nextLine()
                print("Nama penghuni: ")
                val nama = scanner.nextLine()
                print("NIM: ")
                val nim = scanner.nextLine()
                print("Saldo awal: Rp")
                val saldo = scanner.nextDouble()
                scanner.nextLine()
                kost.daftarkanPenghuni(nomorKamar, Penghuni(nama, nim, saldo))
            }
            3 -> {
                println("\n--- Keluarkan Penghuni ---")
                print("Nomor kamar: ")
                val nomorKamar = scanner.nextInt()
                scanner.nextLine()
                kost.keluarkanPenghuni(nomorKamar)
            }
            4 -> {
                println("\n--- Proses Pembayaran ---")
                print("Nomor kamar: ")
                val nomorKamar = scanner.nextInt()
                scanner.nextLine()
                kost.prosesPembayaran(nomorKamar)
            }
            5 -> {
                println("\n--- Top Up Saldo ---")
                print("Nomor kamar: ")
                val nomorKamar = scanner.nextInt()
                scanner.nextLine()
                print("Jumlah top up: Rp")
                val jumlah = scanner.nextDouble()
                scanner.nextLine()
                kost.topUpPenghuni(nomorKamar, jumlah)
            }
            6 -> {
                println("\n--- Cek Saldo ---")
                print("Nomor kamar: ")
                val nomorKamar = scanner.nextInt()
                scanner.nextLine()
                kost.cekSaldo(nomorKamar)
            }
            7 -> {
                kost.tampilkanStatus()
            }
            0 -> {
                println("\nTerima kasih telah menggunakan KostHub Telur Asin!")
            }
            else -> {
                println("\nMenu tidak valid! Pilih 0-7")
            }
        }
        println()
    } while (pilihan != 0)
}