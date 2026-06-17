# JavaCRUD — Form Siswa

Aplikasi Swing CRUD sederhana untuk data siswa, dibangun dengan Java + Maven.
Kompatibel dengan **IntelliJ IDEA** (Community maupun Ultimate).

---

## Struktur Project

```
JavaCRUD/
├── pom.xml
└── src/main/java/com/mycompany/javacrud/
    ├── Main.java          ← Entry point (run/debug dari sini)
    ├── FormSiswa.java     ← UI utama (form + tabel)
    └── Koneksi.java       ← Helper koneksi MySQL
```

---

## Cara Buka di IntelliJ

1. Buka IntelliJ → **File → Open**
2. Pilih folder `JavaCRUD` (yang berisi `pom.xml`)
3. Klik **Open as Project**
4. Tunggu Maven import selesai (progress bar di bawah)
5. Klik kanan `Main.java` → **Run 'Main'** atau **Debug 'Main'**

---

## Cara Run & Debug

| Aksi | Shortcut |
|---|---|
| Run | `Shift + F10` |
| Debug | `Shift + F9` |
| Set Breakpoint | Klik di margin kiri (nomor baris) |
| Step Over | `F8` |
| Step Into | `F7` |

---

## Konfigurasi Database (Opsional)

Edit `Koneksi.java`, sesuaikan 3 konstanta ini:

```java
private static final String DB_URL  = "jdbc:mysql://localhost:3306/NAMA_DATABASE";
private static final String DB_USER = "root";
private static final String DB_PASS = "";  // isi password jika ada
```

Buat database-nya dulu di MySQL/XAMPP sebelum run.

---

## Requirement

- **JDK 17** (atau 11+)
- **Maven** (sudah bundled di IntelliJ)
- MySQL (opsional, untuk integrasi database)
