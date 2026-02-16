# Jawaban Week 4 Lab

**Nama:** Rico Valentino  
**Course:** WEB-202 - Spring Boot Full-Stack Web Development  
**Week:** 4 - MVC Pattern & Controller Layer

---

## Latihan 1: @Controller, Model & Thymeleaf

### Eksperimen 1: @Controller vs @RestController

- **/test/view** → Menampilkan halaman HTML dari template `test.html` dengan teks "Ini dari @Controller"
- **/test/text** → Menampilkan teks langsung di browser: "Ini dari @Controller + @ResponseBody → text langsung"
- **Perbedaannya** → `/test/view` mengembalikan nama template (view) untuk dirender menjadi HTML; `/test/text` mengembalikan string mentah langsung ke response body tanpa melalui template

**Kesimpulan:** `@Controller` tanpa `@ResponseBody` → return **nama template** (view). Dengan `@ResponseBody` → return **data langsung** (text/JSON).

---

### Eksperimen 2: Template Tidak Ada

1. Ubah return di `ProductController.listProducts()` menjadi `"product/halaman-tidak-ada"`
2. Jalankan dan buka `http://localhost:8080/products`

- **Apakah berhasil?** → Tidak
- **HTTP Status code:** → 500 (Internal Server Error)
- **Error message:** → "Could not resolve view with name 'product/halaman-tidak-ada' in servlet with name 'dispatcherServlet'" (atau serupa)

**Kesimpulan:** Jika Controller return nama view yang tidak ada, Spring akan mengembalikan error **500 Internal Server Error** karena **Thymeleaf/ViewResolver tidak dapat menemukan template file**.

---

### Eksperimen 3: @RequestParam vs @PathVariable

| **URL** | **Hasil di Halaman** |
|---------|----------------------|
| `/greet` | Selamat Pagi, Mahasiswa! |
| `/greet?name=Budi` | Selamat Pagi, Budi! |
| `/greet?name=Budi&waktu=Siang` | Selamat Siang, Budi! |
| `/greet/Ani` | Halo, Ani! |
| `/greet/Ani/detail` | Halo, Ani! (lang=ID default) |
| `/greet/Ani/detail?lang=EN` | Hello, Ani! |

**Pertanyaan:**
- URL mana yang pakai `@RequestParam`? → `/greet`, `/greet?name=Budi`, `/greet?name=Budi&waktu=Siang`
- URL mana yang pakai `@PathVariable`? → `/greet/Ani`
- URL mana yang pakai keduanya? → `/greet/Ani/detail`, `/greet/Ani/detail?lang=EN`

---

### Eksperimen 4: Halaman About & Category Summary

- **AboutController** → `GET /about` menampilkan appName, version, author (Rico Valentino), dan daftar technologies
- **ProductService.getAllCategories()** → mengembalikan list kategori unik dari semua produk
- **GET /products/categories** → halaman daftar kategori dengan link ke `/products/category/{nama}` dan jumlah produk per kategori

---

### Pertanyaan Refleksi Latihan 1

**1. Apa perbedaan antara `@Controller` dan `@RestController`? Dalam kasus apa kamu pakai masing-masing?**

- **@Controller:** Mengembalikan nama view (template) untuk dirender sebagai HTML. Dipakai untuk aplikasi web MVC yang menampilkan halaman HTML.
- **@RestController:** Kombinasi `@Controller` + `@ResponseBody`. Mengembalikan data langsung (JSON/XML/text) ke response body. Dipakai untuk REST API yang melayani client (mobile, SPA, dll).
- **Kasus penggunaan:** Pakai `@Controller` untuk web app dengan server-side rendering (Thymeleaf, JSP). Pakai `@RestController` untuk API yang return JSON.

**2. Perhatikan bahwa template `product/list.html` dipakai oleh 3 endpoint berbeda (list all, filter by category, search). Apa keuntungannya membuat template yang reusable seperti ini?**

- **DRY (Don't Repeat Yourself):** Tidak perlu duplikasi kode HTML untuk tampilan yang sama.
- **Konsistensi:** Semua halaman daftar produk tampil seragam.
- **Pemeliharaan:** Perubahan tampilan cukup di satu tempat, langsung berlaku di semua endpoint.
- **Efisiensi:** Mengurangi ukuran kode dan waktu pengembangan.

**3. Kenapa Controller inject `ProductService` (bukan langsung akses data di ArrayList)? Apa yang terjadi kalau Controller langsung manage data?**

- **Separation of Concerns:** Controller fokus ke HTTP/request handling; Service fokus ke logika bisnis dan akses data.
- **Testability:** Service bisa di-mock untuk unit test Controller.
- **Reusability:** Service bisa dipakai banyak Controller lain.
- **Single Source of Truth:** Data dikelola di satu tempat (Service).
- **Kalau Controller langsung manage data:** Logika bisnis tercampur dengan layer presentasi, sulit diuji, dan melanggar prinsip MVC.

**4. Apa perbedaan `model.addAttribute("products", products)` dengan return `products` langsung seperti di `@RestController`?**

- **model.addAttribute():** Data disimpan di Model object yang dikirim ke View (template Thymeleaf). Template akan merender HTML dengan data tersebut. Hasil akhir = halaman HTML.
- **return products di @RestController:** Data dikonversi ke JSON dan dikirim langsung sebagai response body. Tidak ada proses rendering template. Hasil akhir = JSON string.
- **Intinya:** Yang pertama untuk server-side rendering (HTML); yang kedua untuk API (data mentah).

**5. Jika kamu buka `http://localhost:8080/products/abc` (ID bukan angka), apa yang terjadi? Kenapa?**

- **Apa yang terjadi:** Spring akan menampilkan error **400 Bad Request** (atau 500) karena type mismatch.
- **Kenapa:** Parameter `@PathVariable Long id` mengharapkan tipe `Long`. String "abc" tidak bisa dikonversi ke Long, sehingga Spring memunculkan `MethodArgumentTypeMismatchException` atau `NumberFormatException`.

**6. Apa keuntungan pakai `@RequestMapping("/products")` di level class dibanding menulis full path di setiap `@GetMapping`?**

- **DRY:** Path dasar `/products` ditulis sekali saja.
- **Kemudahan refactor:** Jika path berubah, cukup ubah di satu tempat.
- **Kejelasan struktur:** Semua endpoint produk terkumpul di satu controller dengan base path yang jelas.
- **Konsistensi:** Memastikan semua endpoint produk punya prefix yang sama.

**7. Dalam lab ini, kata "Model" muncul dalam beberapa konteks berbeda. Sebutkan minimal 2 arti yang berbeda dan jelaskan perbedaannya.**

- **Model object (Spring Model):** Interface/objek container key-value yang digunakan Controller untuk mengirim data ke View. Misalnya `model.addAttribute("products", products)`. Ini seperti "baki pengantar" data dari Controller ke View.
- **Model Layer (MVC):** Lapisan yang berisi logika bisnis dan data. Di lab ini diwakili oleh `ProductService` dan `Product` (Data Class). Ini "dapur" yang memproses data.
- **Data Class / POJO (Product.java):** Class Java biasa yang merepresentasikan entitas data. Bukan Spring Bean, bukan Spring Model. Ini objek data yang di-pass dari Service ke Controller ke View.

---

## Latihan 2: Navigasi Multi-Halaman & Layout Sederhana

### Eksperimen 1: Fragment yang Tidak Ada

1. Ubah referensi fragment di template menjadi `navbar-yang-salah` (misalnya di `home.html`)
2. Jalankan dan buka halaman tersebut

- **Apakah error?** → Ya
- **Error message:** → "Error resolving template" atau "Fragment 'navbar-yang-salah' tidak ditemukan" (bisa bervariasi tergantung versi Thymeleaf)

**Kesimpulan:** Jika nama fragment salah, Thymeleaf akan **menampilkan error** (biasanya 500) karena **tidak dapat menemukan fragment yang diminta**.

---

### Eksperimen 2: Static Resource Path

1. Ganti `th:href="@{/css/style.css}"` menjadi `href="/css/style.css"`
   - **CSS masih bekerja?** → Ya (path absolut valid untuk static resources)

2. Ganti path ke file yang tidak ada: `th:href="@{/css/tidak-ada.css}"`
   - **Apakah halaman error?** → Tidak (halaman tetap load)
   - **Apakah CSS diterapkan?** → Tidak (file tidak ada, styling dari file itu tidak diterapkan)

**Kesimpulan:** `th:href="@{}"` lebih baik karena **mendukung context path** jika aplikasi di-deploy di subpath, dan **memastikan URL di-generate dengan benar**. Jika file CSS tidak ada, **halaman tidak error** tetapi **style dari file itu tidak diterapkan** (browser akan 404 untuk resource tersebut).

---

### Eksperimen 3: Halaman Statistik (Tantangan)

- **StatisticsController** → `GET /statistics`
- Data yang ditampilkan: total produk, produk per kategori, termahal, termurah, rata-rata harga, jumlah produk stok rendah
- Template **statistics.html** dengan card dan tabel
- Link "Statistik" di navbar

---

### Pertanyaan Refleksi Latihan 2

**1. Apa keuntungan menggunakan Thymeleaf Fragment untuk navbar dan footer?**

- **Reusability:** Satu fragment dipakai di banyak halaman.
- **Konsistensi:** Navbar dan footer tampil sama di seluruh aplikasi.
- **Maintainability:** Perubahan navbar/footer cukup di satu file, langsung berlaku di semua halaman.
- **DRY:** Menghindari copy-paste kode HTML yang sama berulang kali.

**2. Apa bedanya file di `static/` dan `templates/`? Kenapa CSS ada di `static/` bukan `templates/`?**

- **static/:** Berisi file statis yang disajikan langsung ke client tanpa diproses (CSS, JS, gambar, font). Dapat diakses via URL langsung (misal `/css/style.css`).
- **templates/:** Berisi file template (misalnya HTML + Thymeleaf) yang diproses server dulu, di-render dengan data dari Model, lalu dikirim sebagai HTML final.
- **Kenapa CSS di static/:** CSS tidak perlu diproses oleh Thymeleaf. CSS adalah file statis yang browser butuhkan apa adanya. Template Thymeleaf hanya mereferensikan URL ke file tersebut.

**3. Apa yang dimaksud dengan `th:replace` dan bagaimana bedanya dengan `th:insert`?**

- **th:replace:** Mengganti element host sepenuhnya dengan konten fragment. Element `<div th:replace="~{fragments/layout :: navbar}">` akan diganti total oleh konten navbar. Di DOM hasil, tidak ada lagi tag `<div>` pembungkus.
- **th:insert:** Menyisipkan konten fragment ke dalam element host. Element host tetap ada, fragment dimasukkan sebagai child. Di DOM hasil, ada tag `<div>` yang membungkus konten fragment.
- **Dampak praktis:** `th:replace` cocok bila fragment menggantikan wadah sepenuhnya (navbar langsung jadi `<nav>`). `th:insert` cocok bila fragment menjadi child dari element tertentu.

**4. Kenapa kita pakai `@{}` untuk URL di Thymeleaf, bukan langsung tulis path?**

- **Context path:** Jika aplikasi di-deploy dengan context path (misal `/app`), `@{}` akan otomatis menambahkan prefix. Path `/products` jadi `/app/products`.
- **Session/CSRF:** Beberapa konfigurasi Spring Security memerlukan token; `@{}` bisa membantu generate URL yang benar.
- **Portabilitas:** URL yang di-generate konsisten dengan konfigurasi server.
- **Escape dan encoding:** `@{}` memastikan parameter di-encode dengan benar.

**5. Perhatikan bahwa `ProductController` inject `ProductService` melalui Constructor Injection. Apa jadinya kalau Controller tidak pakai DI dan langsung `new ProductService()` di dalam Controller?**

- **Tight coupling:** Controller tergantung langsung ke implementasi konkret `ProductService`. Sulit ganti implementasi (misalnya untuk testing dengan mock).
- **Sulit di-test:** Dalam unit test, kita tidak bisa inject mock Service. Controller akan selalu memakai Service asli.
- **Lingkup bean:** `ProductService` yang di-inject adalah Singleton Spring. Kalau `new ProductService()` di dalam method, setiap request bisa membuat instance baru, yang bisa menyebabkan data tidak konsisten atau performa buruk.
- **Pelanggaran Dependency Inversion:** Seharusnya Controller bergantung pada abstraction (interface), bukan implementasi konkret.

---

## Ringkasan

- **Author:** Rico Valentino (diset di `AboutController`)
- Semua latihan dan eksperimen telah diimplementasikan
- Untuk Eksperimen 2 L1 dan Eksperimen 1 L2: ubah kode sementara → jalankan → ambil screenshot → kembalikan kode ke semula
