# Spring MVC Lab - Week 4

Latihan Week 4: Spring MVC — Controller, Model & Thymeleaf (WEB-202)

## Struktur Project

```
handson 5/
├── spring-mvc-lab/          ← Project Spring Boot (Latihan 1 & 2 digabung)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/springmvclab/
│   │   │   │   ├── controller/    ← HomeController, ProductController, dll
│   │   │   │   ├── model/         ← Product
│   │   │   │   └── service/       ← ProductService
│   │   │   └── resources/
│   │   │       ├── static/css/    ← style.css
│   │   │       └── templates/     ← Thymeleaf templates
│   │   └── test/
│   └── build.gradle
├── JAWABAN.md               ← Jawaban refleksi & eksperimen
├── screenshots/             ← Simpan screenshot di sini
└── README.md                ← File ini
```

## Cara Menjalankan

1. **Generate Gradle Wrapper** (jika belum ada `gradle-wrapper.jar`):
   ```bash
   cd spring-mvc-lab
   gradle wrapper
   ```

2. **Jalankan aplikasi**:
   ```bash
   cd spring-mvc-lab
   ./gradlew bootRun    # Linux/Mac
   gradlew.bat bootRun  # Windows
   ```

3. **Buka browser**: http://localhost:8080

## URL yang Tersedia

| URL | Keterangan |
|-----|------------|
| `/` | Home |
| `/products` | Daftar semua produk |
| `/products/{id}` | Detail produk |
| `/products/category/{category}` | Filter by kategori |
| `/products/search?keyword=` | Pencarian |
| `/products/categories` | Daftar kategori (Eksperimen 4) |
| `/about` | Halaman About (Eksperimen 4) |
| `/statistics` | Statistik produk (Eksperimen 3 Latihan 2) |
| `/test/view` | Eksperimen 1: @Controller view |
| `/test/text` | Eksperimen 1: @ResponseBody |
| `/greet` | Eksperimen 3: @RequestParam |
| `/greet/{name}` | Eksperimen 3: @PathVariable |

## Catatan

- Project ini dikonfigurasi sesuai lab: **Spring Boot 4.0.2** dan **Java 25**
- Jika Java 25 belum tersedia, ubah di `build.gradle`: gunakan `sourceCompatibility = '21'` dan Spring Boot `3.3.5`
- Ganti `author` di `AboutController` dengan nama Anda sebelum submit
