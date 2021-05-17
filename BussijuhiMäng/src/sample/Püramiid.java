package sample;
import java.util.ArrayList;
import java.util.List;


public class Püramiid {
    private List<Kaart> kaardid;
    static final int külg = 5;
    private int järg = 0;

    public int kaarte_kokku(){
        int kaarte = 0;
        for (int i = 1; i <= külg; i++) {
            kaarte = kaarte+i;
        }
        return kaarte;
    }

    public Püramiid(Kaardipakk kaardipakk) {
        List<Kaart> kaardid = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            kaardid.add(kaardipakk.getKaardipakki(i));
        }
        this.kaardid = kaardid;
    }

    public int getJärg() {
        return järg;
    }

    public int tase( ){ //Kasutab teadmist, et mis on järg ja kui suur on püramiid ning tagastab taseme
        int kaarte = kaarte_kokku();
        int kulunud = 0;
        for (int i = 1; i < külg+1; i++) {
            kulunud = kulunud+i;
            if (kaarte-järg <= kulunud)
                return külg+1-i;
        }
        return -1;
    }

    public Kaart keera(){ //töötab ainult siis kui püramiidis veel on kaarte
        //tagastab järgmise kaardi ja liigub järjega edasi
        järg = järg + 1;
        return kaardid.get(järg - 1);
    }

    @Override
    public String toString() {
        return "Püramiid{" +
                "kaardid=" + kaardid +
                '}';
    }

}




