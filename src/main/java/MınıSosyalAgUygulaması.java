import java.util.*;

public class MınıSosyalAgUygulaması {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        SosyalAg sosyalAg = new SosyalAg();

        while (true) {
            System.out.println("\n----- Mini Sosyal Ağ -----");
            System.out.println("1. Yeni kullanıcı oluştur");
            System.out.println("2. Arkadaş ekle");
            System.out.println("3. Gönderi paylaş");
            System.out.println("4. Gönderi beğen");
            System.out.println("5. Tüm kullanıcıları listele");
            System.out.println("6. En popüler kullanıcıyı göster");
            System.out.println("7. 18 yaşından küçük kullanıcıları listele");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");
            int secim = input.nextInt();
            input.nextLine(); // boş satırı yakala

            switch (secim) {
                case 1 -> {
                    System.out.print("Kullanıcı adı: ");
                    String ad = input.nextLine();
                    System.out.print("E-posta: ");
                    String mail = input.nextLine();
                    System.out.print("Yaş: ");
                    int yas = input.nextInt();
                    input.nextLine();
                    sosyalAg.kullaniciEkle(ad, mail, yas);
                }
                case 2 -> {
                    System.out.print("Kullanıcı adı: ");
                    String ad1 = input.nextLine();
                    System.out.print("Arkadaşın adı: ");
                    String ad2 = input.nextLine();
                    sosyalAg.arkadasEkle(ad1, ad2);
                }
                case 3 -> {
                    System.out.print("Kullanıcı adı: ");
                    String ad = input.nextLine();
                    System.out.print("Gönderi içeriği: ");
                    String yazi = input.nextLine();
                    sosyalAg.gonderiEkle(ad, yazi);
                }
                case 4 -> {
                    System.out.print("Beğenecek kişi adı: ");
                    String begenen = input.nextLine();
                    System.out.print("Gönderi sahibi: ");
                    String yazar = input.nextLine();
                    sosyalAg.gonderiBegen(begenen, yazar);
                }
                case 5 -> sosyalAg.kullanicilariListele();
                case 6 -> sosyalAg.enPopulerKullanici();
                case 7 -> sosyalAg.yaşi18denKucukleriListele();
                case 0 -> {
                    System.out.println("Çıkılıyor...");
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }
}

// Ana sınıf
class SosyalAg {
    private final ArrayList<Kullanici> kullanicilar = new ArrayList<>();
    private final HashMap<String, LinkedList<Gonderi>> gonderiMap = new HashMap<>();

    public void kullaniciEkle(String ad, String mail, int yas) {
        Kullanici yeni = new Kullanici(ad, mail, yas);
        kullanicilar.add(yeni);
        gonderiMap.put(ad, yeni.getGonderiler());
        System.out.println("Kullanıcı eklendi.");
    }

    public void arkadasEkle(String kim, String kime) {
        Kullanici k1 = kullaniciBul(kim);
        Kullanici k2 = kullaniciBul(kime);
        if (k1 != null && k2 != null) {
            k1.getArkadaslar().add(k2);
            System.out.println(kime + " adlı kullanıcı " + kim + " adlı kullanıcıya arkadaş olarak eklendi.");
        } else System.out.println("Kullanıcı(lar) bulunamadı.");
    }

    public void gonderiEkle(String ad, String yazi) {
        Kullanici k = kullaniciBul(ad);
        if (k != null) {
            Gonderi g = new Gonderi(yazi);
            k.getGonderiler().add(g);
            System.out.println("Gönderi eklendi.");
        } else System.out.println("Kullanıcı bulunamadı.");
    }

    public void gonderiBegen(String begenen, String yazar) {
        LinkedList<Gonderi> gList = gonderiMap.get(yazar);
        if (gList != null && !gList.isEmpty()) {
            Gonderi g = gList.getLast();
            g.getBegeniler().add(begenen);
            System.out.println("Beğenildi.");
        } else System.out.println("Gönderi bulunamadı.");
    }

    public void kullanicilariListele() {
        Iterator<Kullanici> it = kullanicilar.iterator();
        while (it.hasNext()) {
            Kullanici k = it.next();
            System.out.println("Adı: " + k.getKullaniciAdi() + ", Yaş: " + k.getYas());
            for (Gonderi g : k.getGonderiler()) {
                System.out.println("  > " + g.getYazi() + " [" + g.getBegeniler().size() + " beğeni]");
            }
        }
    }

    public void enPopulerKullanici() {
        TreeMap<Integer, String> siralama = new TreeMap<>(Collections.reverseOrder());
        for (Kullanici k : kullanicilar) {
            int toplamBegeni = 0;
            for (Gonderi g : k.getGonderiler()) toplamBegeni += g.getBegeniler().size();
            siralama.put(toplamBegeni, k.getKullaniciAdi());
        }
        if (!siralama.isEmpty()) {
            Map.Entry<Integer, String> enPopuler = siralama.firstEntry();
            System.out.println("En popüler kullanıcı: " + enPopuler.getValue() + " (" + enPopuler.getKey() + " beğeni)");
        } else {
            System.out.println("Henüz gönderi yok.");
        }
    }

    public void yaşi18denKucukleriListele() {
        boolean bulundu = false;
        for (Kullanici k : kullanicilar) {
            if (k.getYas() < 18) {
                System.out.println("Adı: " + k.getKullaniciAdi() + ", Yaş: " + k.getYas());
                bulundu = true;
            }
        }
        if (!bulundu) {
            System.out.println("18 yaşından küçük kullanıcı bulunmamaktadır.");
        }
    }

    private Kullanici kullaniciBul(String ad) {
        for (Kullanici k : kullanicilar) {
            if (k.getKullaniciAdi().equals(ad)) return k;
        }
        return null;
    }
}

// Kullanıcı sınıfı
class Kullanici {
    private final String kullaniciAdi;
    private final String eposta;
    private final int yas;
    private final LinkedList<Gonderi> gonderiler = new LinkedList<>();
    private final HashSet<Kullanici> arkadaslar = new HashSet<>();
    private final Profil profil;

    public Kullanici(String ad, String mail, int yas) {
        this.kullaniciAdi = ad;
        this.eposta = mail;
        this.yas = yas;
        this.profil = new Profil("profil.jpg", "Merhaba!");
    }

    public String getKullaniciAdi() { return kullaniciAdi; }
    public int getYas() { return yas; }
    public LinkedList<Gonderi> getGonderiler() { return gonderiler; }
    public HashSet<Kullanici> getArkadaslar() { return arkadaslar; }

    class Profil {
        String resim;
        String bio;

        public Profil(String r, String b) {
            this.resim = r;
            this.bio = b;
        }
    }
}

// Gönderi sınıfı
class Gonderi {
    private final String yazi;
    private final HashSet<String> begeniler = new HashSet<>();

    public Gonderi(String yazi) {
        this.yazi = yazi;
    }

    public String getYazi() { return yazi; }
    public HashSet<String> getBegeniler() { return begeniler; }
}


/*
BURASIDA İŞ GÖRÜYOR ANCAK KULLANICIDAN VERİ ALMIYOR , EKLEDİĞİM BİRKAÇ KİŞİYİ GÖSTERİYOR
ONUN İÇİN BU ŞEKİLDE KAPATTIM.

import java.util.*;

 class Kullanici {
    String kullaniciAdi;
    String eposta;
    int yas;
    Profil profil;
    HashSet<Kullanici> arkadaslar = new HashSet<>();
    LinkedList<Gonderi> gonderiler = new LinkedList<>();

    public Kullanici(String ad, String mail, int yas, String biyografi, String resim) {
        this.kullaniciAdi = ad;
        this.eposta = mail;
        this.yas = yas;
        this.profil = new Profil(biyografi, resim);
    }

    static class Profil {
        String biyografi;
        String resimYolu;

        public Profil(String bio, String resimYolu) {
            this.biyografi = bio;
            this.resimYolu = resimYolu;
        }
    }

    public void arkadasEkle(Kullanici k) {
        if (k != this) {
            arkadaslar.add(k);
        }
    }

    public void gonderiPaylas(String metin) {
        gonderiler.add(new Gonderi(this, metin));
    }
}

    class Gonderi {
    Kullanici sahip;
    String icerik;
    HashSet<String> begenenler = new HashSet<>();

    public Gonderi(Kullanici sahip, String icerik) {
        this.sahip = sahip;
        this.icerik = icerik;
    }

    public void begen(String kullaniciAdi) {
        begenenler.add(kullaniciAdi);
    }

    public int begeniSayisi() {
        return begenenler.size();
    }
}

    class SosyalAg {
    ArrayList<Kullanici> kullanicilar = new ArrayList<>();
    HashMap<String, LinkedList<Gonderi>> gonderiHaritasi = new HashMap<>();

    public void kullaniciEkle(Kullanici k) {
        kullanicilar.add(k);
        gonderiHaritasi.put(k.kullaniciAdi, k.gonderiler);
    }

    public void tumKullanicilariVeGonderileriListele() {
        Iterator<Kullanici> it = kullanicilar.iterator();
        while (it.hasNext()) {
            Kullanici k = it.next();
            System.out.println("Kullanıcı: " + k.kullaniciAdi);
            for (Gonderi g : k.gonderiler) {
                System.out.println("  > Gönderi: " + g.icerik + " (" + g.begeniSayisi() + " beğeni)");
            }
        }
    }

    public void populerKullanicilariGoster() {
        TreeMap<Integer, String> populerlikSirasi = new TreeMap<>(Collections.reverseOrder());

        for (Kullanici k : kullanicilar) {
            int toplamBegeni = 0;
            for (Gonderi g : k.gonderiler) {
                toplamBegeni += g.begeniSayisi();
            }
            populerlikSirasi.put(toplamBegeni, k.kullaniciAdi);
        }

        System.out.println("\n--- En Popüler Kullanıcılar ---");
        for (Map.Entry<Integer, String> entry : populerlikSirasi.entrySet()) {
            System.out.println(entry.getValue() + " - " + entry.getKey() + " beğeni");
        }
    }

    public void yasi18denKucukleriListele() {
        System.out.println("\n--- 18 Yaş Altı Kullanıcılar ---");
        kullanicilar.removeIf(new java.util.function.Predicate<Kullanici>() {
            public boolean test(Kullanici k) {
                if (k.yas < 18) {
                    System.out.println(k.kullaniciAdi + " (" + k.yas + " yaşında)");
                }
                return false;
            }
        });
    }
}

    public class MınıSosyalAgUygulaması {
    public static void main(String[] args) {
        SosyalAg ag = new SosyalAg();

        Kullanici mehmet = new Kullanici("mehmet", "mehmet87@mail.com", 22, "Doktorum", "mehmet.jpg");
        Kullanici elif = new Kullanici("elif", "elif12@mail.com", 16, "Hayvanseverim", "elif.jpg");
        Kullanici zeynep = new Kullanici("zeynep", "zeynep27@mail.com", 27, "Java öğreniyorum :D", "zeynep.jpg");

        mehmet.arkadasEkle(zeynep);
        elif.arkadasEkle(mehmet);

        mehmet.gonderiPaylas("Bugün dışarı çıktım!");
        zeynep.gonderiPaylas("Kod yazıyorum.");
        elif.gonderiPaylas("Çok mutluyum!");

        mehmet.gonderiler.get(0).begen("zeynep");
        zeynep.gonderiler.get(0).begen("mehmet");
        elif.gonderiler.get(0).begen("mehmet");
        elif.gonderiler.get(0).begen("zeynep");

        ag.kullaniciEkle(mehmet);
        ag.kullaniciEkle(elif);
        ag.kullaniciEkle(zeynep);

        System.out.println("--- Kullanıcılar ve Gönderileri ---");
        ag.tumKullanicilariVeGonderileriListele();

        ag.populerKullanicilariGoster();

        ag.yasi18denKucukleriListele();
    }
}
*/

