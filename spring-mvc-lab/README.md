# Spring MVC Lab - Week 4

Project Spring Boot untuk Latihan 1 & 2 (WEB-202)

## Kebutuhan

- **JDK 23**
- IntelliJ IDEA (atau IDE lain)

## Cara Buka di IntelliJ IDEA

1. **File** → **Open**
2. Pilih folder **spring-mvc-lab** (tempat file `build.gradle` berada)
3. Pilih **Open as Project**
4. IntelliJ akan mendeteksi Gradle dan melakukan sync otomatis
5. Pastikan Project SDK = **JDK 23**  
   → **File** → **Project Structure** (Ctrl+Alt+Shift+S) → **Project** → **SDK** → pilih JDK 23

> Jika JDK 23 belum ada di IntelliJ: **File** → **Project Structure** → **Platform Settings** → **SDKs** → **+** → **Download JDK** → pilih Version 23.

## Cara Menjalankan

### Via IntelliJ
- Buka `SpringMvcLabApplication.java`
- Klik tombol **Run** (ikon hijau) atau tekan **Shift+F10**

### Via Gradle (Command Line)
Pastikan **JAVA_HOME** mengarah ke JDK 23 terlebih dahulu:

```powershell
# Windows - set JAVA_HOME sementara
$env:JAVA_HOME = "C:\Program Files\Java\jdk-23"  # sesuaikan path JDK 23 Anda
.\gradlew.bat bootRun
```

```bash
# Linux/Mac
export JAVA_HOME=/path/to/jdk-23
./gradlew bootRun
```

## URL Aplikasi

- Home: http://localhost:8080
- Produk: http://localhost:8080/products
- Detail: http://localhost:8080/products/1
- About: http://localhost:8080/about
- Statistik: http://localhost:8080/statistics
