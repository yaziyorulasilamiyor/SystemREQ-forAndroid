# SystemREQ (Android)

Android cihaz özelliklerine göre oyun/uygulama sistem gereksinimi uygunluk kontrolü yapan basit Android uygulaması.

## Özellikler
- Cihaz bilgilerini gösterir (Android SDK, RAM, boş depolama)
- SQLite üzerinde oyun/uygulama gereksinimlerini saklar (ekle/listele)
- Katalog ekranında sonuç üretir: Uygun / Sınırda / Uygun Değil
- Options Menu ile ek fonksiyonlar (Ekle, Örnek Veri Yükle, Hakkında)

## Kullanılan Teknolojiler
- Kotlin
- Android Studio
- SQLite (SQLiteOpenHelper)
- XML UI
- Activity, Intent, Options Menu
- ListView + ArrayAdapter

## Ekranlar
- MainActivity: Cihaz bilgilerini gösterir
- AppListActivity: SQLite kayıtlarını listeler ve uygunluk sonucunu gösterir
- AddAppActivity: Yeni kayıt ekleme ekranı

## Çalıştırma
1. Android Studio ile projeyi aç
2. Run ▶️ ile emulator veya telefon üzerinde çalıştır
