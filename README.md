## Modul 1 - Coding Standards

### Reflection 1

Prinsip **Clean Code** yang sudah saya terapkan pada modul 1 adalah, pertama, **Meaningful Names**, saya menamai variabel-variabel seperti `productData`, `productRepository`, dan `allProducts` yang secara jelas mendeskripsikan guna dan isinya. Kedua, **Single Responsibility Principle** atau **SRP**, yang dimana setiap *class* memiliki *role* yang jelas, `Product` meng-*handle* data, `ProductRepository` mengatur logika, dan `ProductController` meng-*handle* *web requests*. Ketiga, **Seperation of Concerns**, saya buat proyek ini dengan memisahkan antara logika bisnisnya, akses data, dan *UI* agar terstruktur dengan rapi.

Keempat, **Don't Repeat Youself** atau **DRY**, dengan menggunakan `@Getter` dan `@Setter` di *model*, kita tidak perlu membuatnya secara manual lagi. Terakhir, kode distruktur agar mudah untuk dilakukan **unit testing** dan **functional testing**.

Praktik **Secure Coding** yang telah diterapkan pada modul 1 ini, pertama, ada **Encapsulation**, dimana *field-field* pada `Product` dan `ProductRepository` dibuat *private* agar tidak dapat diakses dari luar. Kedua, penggunaan `List<Product>` memastikan hanya `Product` yang valid yang di-*handle*, sehingga mengurangi *RTE*. Terakhir, penggunaan `@Autowired` untuk **dependency injections** membuat *inject mock objects* lebih mudah untuk **security testing**.

Area yang dimana bisa di-**improve** adalah saya temui *method* `create` tidak menjamin `productId` yang dibuat unik, sehingga dengan menggunakan `UUID.randomUUID().toString()` dapat menjamin setiap produk memiliki *ID* yang unik.

### Reflection 2

Menulis **unit tests** terasa *time-consuming* tetapi dapat berguna sebagai ***safety net*** yang mendukung kita saat me-*refactor* kode. Walaupun menurut saya tidak ada batasan yang *fix* berapa jumlah tes per *class*, tetapi jumlahnya dapat disesuaikan dengan kompleksitas dan alur *logic*-nya. Setiap *public method* perlu setidaknya satu *test case* dan *test case* tambahan untuk tiap *branch*, *loop*, dan *edge cases*.

Untuk menjamin tes-tesnya cukup, kita perlu memverifikasi **positive scenarios** (input yang valid) dan **negative scenarios** (input yang tidak valid, *list* kosong/ *null*). Jika **code coverage** mencapai 100%, belum tentu berarti kodenya tidak ada *error* sama sekali. *Coverage* hanya mengukur *line-line* mana saja yang tereksekusi dan tidak menilai apakah *logic*-nya benar atau semua kemungkinan input sudah dites.

Jika kita membuat **functional test** baru dengan menduplikasi `CreateProductFunctionalTest`, maka akan timbul beberapa isu. Implementasi ulang semua *setup* dan variabel *instance* di setiap *class* baru akan terlihat redundan dan sulit untuk di-*maintain*. Jika *logic*-nya perlu diubah, maka perlu di-*update* satu-satu. *Test classes*-nya akan terlihat numpuk dengan *setup* daripada fokus tentang tes-tes yang unik. Demi mengatasi masalah ini, ada baiknya dibuat **abstract base class** yang mengandung semua prosedur *setup* yang sama, lalu **functional test classes** yang lain bisa *extend* dari *base class* ini.

---

## Modul 2 - Continuous Integration, DevOps

1. PMD nge-*flag* *class* `EshopApplication` karena di dalamnya ada *single static method* `main`, hal ini karena Java menambahkan *public constructor* ke *classes* dan PMD mengasumsi *class*-nya adalah Utility Class yang harusnya ada *private constructor*. Jadi, saya *fix* ini dengan menambah *private constructor* kosong agar PMD "*happy*" dan nge-*block* *instantiation* secara eksplisit.

2. Iya, *setup* saat ini memenuhi CI karena dengan secara otomatis nge-*run tests* dan *code security scans* setiap kali *code* di-*push* atau ada *pull request* yang dibuat. *Setup* juga memenuhi CD dengan nge-*trigger* secara otomatis *update* terbaru ke Render setelah ada *kode* baru yang di-*merge* ke *branch* `main` sehingga tidak perlu manual *deployment* lagi.

## Modul 3 - Maintanability, OO Principles

1. Prinsip SOLID yang saya terapkan pada modul 3 ini adalah, pertama, SRP, dimana saya pisahin `CarController` jadi file sendiri karena sebuah *class* harus punya satu alasan untuk berubah jika ada yang harus diubah. Kedua, adalah OCP, saya hapus *inheritance* '*extends ProductController*' agar *entity software* terbuka untuk pengembangan dan tertutup untuk modifikasi. Ketiga, LSP, dengan menghapus *inheritance* tersebut maka dapat dipastikan tidak ada pelanggaran *subclass* harus bisa menggantikan *base class* tanpa mengubah properti program. Keempat, ISP, saya tetap pertahankan *interface* `CarService` agar tetap spesifik atas *methods* yang relevan bagi klien. Terakhir, DIP, saya mengubah *injeksi* pada *controller* dan *service* agar modul tingkat tinggi tidak *dependent* dengan modul tingkat rendah tapi keduanya bergantung pada abstraksi.

2. *Benefit* dari menerapkan SOLID adalah agar *kode* menjadi jauh lebih terstuktur, mudah di-*maintain*, dan fleksibel untuk dikembangkan. Contohnya, penerapan SRP dan OCP, dengan memisahkan `CarController` dari `ProductController`, kalau suatu saat ada perubahan *logic* untuk produk, *controller* mobil tidak ikut terdampak.

3. Kerugian jika tidak menerapkan SOLID adalah arsitektur *kode* akan kaku, rentan terhadap *bug*, dan sulit di-*maintain*. Contohnya, kalau tidak menerapkan LSP, ketika `CarController` meng-*extend* `ProductController`, maka secara paksa akan digabungkan dua *entity* berbeda ini yang tidak bisa saling menggantikan secara logis. Jika diubah fungsi *base* dari `ProductController`, alur `Car` bisa saja tidak sengaja jadi rusak.