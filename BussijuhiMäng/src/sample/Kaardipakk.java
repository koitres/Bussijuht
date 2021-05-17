package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kaardipakk{
    static private String[] mast = {"Poti", "Ärtu", "Ruutu", "Risti"};
    static private String[] number = {"Äss", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Soldat", "Emand", "Kuningas"};
    private List<Kaart> kaardipakk = new ArrayList<>();

    public Kaardipakk() { // Kaardipaki konstruktor mis loob uue täieliku paki
        for (int masts = 0; masts < mast.length; masts++) {
            for (int j = 0; j < number.length; j++) {
                Kaart kaart = new Kaart(mast[masts], number[j]);
                kaardipakk.add(kaart);
            }
        }
        this.kaardipakk = kaardipakk;
    }

    public void segaPakki() {
        Collections.shuffle(kaardipakk);
    }

    @Override
    public String toString() {
        return "Kaardipakk{" +
                "kaardipakk=" + kaardipakk +
                '}';
    }

    public Kaart getKaardipakki(int i) {
        return kaardipakk.get(i);
    }

    public List<ArrayList<Kaart>> jagaKaardipakk(Kaardipakk kaardipakk, int püramiid, int mängijateArv) {
        List<ArrayList<Kaart>> jagamine = new ArrayList<ArrayList<Kaart>>();
        for (int i = 0; i < mängijateArv; i++) {
            ArrayList<Kaart> list1 = new ArrayList<Kaart>();
            jagamine.add(list1);
        }
        for (int i = püramiid; i < 52; i++) {
            jagamine.get(i  %mängijateArv).add(getKaardipakki(i));
        }
        return jagamine;
    }
}