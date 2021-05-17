package sample;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/*
Mängija klass kirjeldab erinevaid mängijaid
meetodid:
    konstruktor: saab kaartide listiga kaardid endale
    !!Käimine: Kui on sama numbriga kaart, saab käia ja eemaldada ühe oma kaardi ning tagastab käidud kaardi
    kaartide arv: tagastab kaartide arvu
    getNimi: tagastab mängija nime
    !!private kellele: mängija saab valida, kellele ta käib antud kaardi

 */
public class Mangija {
    private String nimi;
    private ArrayList<Kaart> kaardid;
    private List<Mangija> käimiseMängijad;

    public Mangija(String nimi, ArrayList<Kaart> kaardid) {
        this.nimi = nimi;
        this.kaardid = kaardid;
    }

    public String getNimi() {
        return nimi;
    }

    public int kaartide_arv() {
        return kaardid.size();
    }


    public int mituSobivatKaarti(Kaart praeguneKaart){
        int tagastus =0;
        for (Kaart kaart:kaardid){
            if (praeguneKaart.getNumber().equals(kaart.getNumber())){
                tagastus +=1;
            }
        }
        return tagastus;
    }

    public List<Kaart> getSobivadKaardid(Kaart praeguneKaart) {
        List<Kaart> sobivadKaardid = new ArrayList<>();
        for (Kaart kaart : kaardid){
            if (praeguneKaart.getNumber().equals(kaart.getNumber())) sobivadKaardid.add(kaart);
        }
        return sobivadKaardid;
    }

    public void eemaldaKaart(Kaart kaart){
        for (Kaart kontrollitavKaart: kaardid){
            if (kontrollitavKaart.equals(kaart)) {
                kaardid.remove(kaart);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Mangija{" +
                "nimi='" + nimi + '\'' +
                ", kaardid=" + kaardid +
                '}';
    }

    public List<Mangija> getKäimiseMängijad() {
        return käimiseMängijad;
    }
}




