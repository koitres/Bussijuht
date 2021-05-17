package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BussijuhtGraafika extends Application {



    @Override
    public void start(Stage peaLava) throws Exception {
        final boolean[] esimeneKordKeerata = {true};//Vajalikud globaalsed muutujad
        Kaardipakk kaardid = new Kaardipakk();
        List<Mangija> mängijadList = new ArrayList<>();
        List<String> mängijateNimed = new ArrayList<>();
        List<Mangija> mängijadKäia = new ArrayList<>();
        Map<Mangija, List<Kaart>> mängijadKaardid = new LinkedHashMap<>();
        kaardid.segaPakki();
        Püramiid püramiid = new Püramiid(kaardid);
        final Kaart[] uusKaart = {null};
        final boolean[] kasKeeratud = {false};
        final String[] valitudMängija = {null};

        final int[] tase = {-1};

        BorderPane piir = new BorderPane();

        // tekstivälja loomine ja lisamine piiripaanile (üles)
        MinuText väljund = new MinuText();
        VBox tekstid = new VBox();
        tekstid.setSpacing(15);
        MinuText edasi = new MinuText();
        väljund.seaText("Tere tulemast mängima mängu Bussijuht");
        tekstid.getChildren().addAll(väljund, edasi);
        piir.setTop(tekstid);

        VBox püramiidG = new VBox(10);//Püramiidi konstruktsioon
        HBox rida5 = new HBox(10);
        HBox rida4 = new HBox(10);
        HBox rida3 = new HBox(10);
        HBox rida2 = new HBox(10);
        HBox rida1 = new HBox(10);
        HBox[] read = {rida5, rida4, rida3, rida2, rida1};


        for (int i = 0; i < 5; i++) { //Lisab püramiidi ruudud (kaardid)
            for (int j = 0; j < i + 1; j++) {
                Rectangle kaart = new Rectangle(50, 50, Color.BLUE);
                kaart.setTranslateX(120 - 30 * i);
                read[i].getChildren().addAll(kaart);
            }
        }

        püramiidG.getChildren().addAll(rida5, rida4, rida3, rida2, rida1); //Lisab püramiidi paani peale
        piir.setCenter(püramiidG);
        String juhendid = "Sisesta siia uue mängija nimi";

        VBox nupuala = new VBox(10); //Siin teen kõik vajlikud nupud
        HBox infod = new HBox(10);
        HBox tegevused = new HBox(10);
        HBox mängijad = new HBox(10);
        Button eiKäi = new Button("Jätan käimata");
        Button keeraja = new Button("Keera");
        Button info = new Button("Mängu reeglid");
        Button nõuded = new Button("Mängu juhend");
        Button logi = new Button("mängu logi (viimased 10 rida)");
        TextField sisend = new TextField(juhendid);
        Button kinnitaja = new Button("kinnita valik");
        infod.getChildren().addAll( info, nõuded);
        tegevused.getChildren().addAll(keeraja, sisend);
        mängijad.getChildren().add(eiKäi);
        nupuala.getChildren().addAll(tegevused, mängijad, infod);
        piir.setBottom(nupuala);

        try (Writer kirjutaja = new BufferedWriter(new FileWriter("logi.txt"));
        ){
            kirjutaja.write("Mäng algas");
        } catch (IOException e) {
            e.printStackTrace();
        }


        sisend.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sisend.clear();
            }
        });

        sisend.setOnKeyPressed(new EventHandler<KeyEvent>() { //Uue mängija lisamine
            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.ENTER) {
                    String nimi = sisend.getText();
                    if (mängijateNimed.contains(nimi)) throw new MängijaNimeErind("Selline nimi on juba olemas");
                    if (nimi.isEmpty()) throw new MängijaNimeErind("Mängijale ei tohi anda tühja nime");
                    Button uusmängija = new Button(nimi);
                    mängijateNimed.add(nimi);
                    mängijad.getChildren().addAll(uusmängija);
                    sisend.setText(juhendid);
                }
            }
        });

        keeraja.setOnMouseClicked(new EventHandler<MouseEvent>() { //keeramise nupu eventahandler
            public void handle(MouseEvent me) {
                if (mängijad.getChildren().size() >= 3) {
                    if (!kasKeeratud[0]) { //Kontrollib, kas võib juba keerata uut kaaerti
                        kasKeeratud[0] = true;
                        if (esimeneKordKeerata[0]) { //Teeb esimese kaardi keeramisel mängijate listi
                            tegevused.getChildren().remove(sisend);
                            List<ArrayList<Kaart>> kaardihunnikud = kaardid.jagaKaardipakk(kaardid, püramiid.kaarte_kokku(), mängijateNimed.size());
                            for (int i = 0; i < mängijateNimed.size(); i++) {
                                String nimi = mängijateNimed.get(i);
                                Mangija mangija = new Mangija(nimi, kaardihunnikud.get(i));
                                mängijadList.add(mangija);
                                mängijadKaardid.put(mangija, null);
                            }
                            esimeneKordKeerata[0] = false;
                            tegevused.getChildren().add(kinnitaja);
                            infod.getChildren().add(logi);

                            for (Node nupp : mängijad.getChildren().sorted()) {
                                nupp.setOnMouseClicked(new EventHandler<MouseEvent>() { //Igale mängija nupule teeb oma eventhandleri, mis seab viimaseks mängijaks (globaalne muutuja) selle nupu nime
                                    public void handle(MouseEvent me) {
                                        String sõne = String.valueOf(me.getTarget().toString());
                                        try{String[] uusSõne = sõne.split("\"");
                                            valitudMängija[0] = uusSõne[1];}
                                        catch (ArrayIndexOutOfBoundsException e) {
                                            try {
                                                String[] uusSõne = sõne.split("\'");
                                                valitudMängija[0] = uusSõne[1];
                                            } catch (ArrayIndexOutOfBoundsException f) {
                                                if (!kasKeeratud[0])
                                                    väljund.lisaText("Midagi läks valesti, proovi uuesti valida mängija");
                                            }
                                        }
                                    }
                                });
                            }
                        }

                        if (püramiid.kaarte_kokku() > püramiid.getJärg()) { //Kui mäng ei ole veel läbi
                            int lonkse = püramiid.tase();
                            uusKaart[0] = püramiid.keera();
                            joonistaKaart(uusKaart[0], püramiidG, lonkse);
                            if (lonkse > tase[0]) {
                                väljund.lisaText("jõudsime järgmisele tasemele, nüüd on lonkse " + lonkse);
                                tase[0] = lonkse;
                            }
                            for (Mangija mängija : mängijadList) {
                                if (mängija.mituSobivatKaarti(uusKaart[0]) > 0) { // seab iga mängijaga vastavusse tema selle vooru sobivad kaardid ja lisab käivate mängijate nimekirja
                                    mängijadKäia.add(mängija);
                                    List<Kaart> sobivadKaardid = mängija.getSobivadKaardid(uusKaart[0]);
                                    mängijadKaardid.put(mängija, sobivadKaardid);
                                }
                            }
                            if (!mängijadKäia.isEmpty()) {//kui on kellelgi käia
                                edasi.seaText(mängijadKäia.get(0).getNimi() + ", vali mängija nupule vajutades: kellele annad juua. \n Su kaart on " + mängijadKäia.get(0).getSobivadKaardid(uusKaart[0]).get(0));
                            } else { //kui kellelgi polnud sobivat kaarti
                                kasKeeratud[0] = false;//lubab uuesti keerata
                                edasi.seaText("Kellelgi ei olnud seda kaarti, keera uuesti! ");
                            }
                        } else {//kui mäng on läbi
                            väljund.seaText("mäng on läbi saanud");
                            tegevused.getChildren().remove(keeraja);
                        }
                    }
                } else {
                    väljund.seaText("Enne esimest keeramist pead sisestama vähemalt 2 mängijat");
                }
            }


        });


        kinnitaja.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if (kasKeeratud[0] && !mängijadKäia.isEmpty()) {
                    Mangija mängija = mängijadKäia.get(0);
                    List<Kaart> sobivadKaardid = mängija.getSobivadKaardid(uusKaart[0]);
                    Kaart kaart = sobivadKaardid.get(0);

                    if (!valitudMängija.equals(null) && !valitudMängija[0].equals("Jätan käimata")) { //Kui mängija käib kaardi ära kellelegi
                        väljund.seaText(valitudMängija[0] + " joob " + mängija.getNimi() + " tahtel " + püramiid.tase() + " lonksu.");
                        mängija.eemaldaKaart(kaart);
                        mängijadKaardid.get(mängija).remove(kaart);
                    } else if (valitudMängija[0].equals("Jätan käimata")){ // kui mängija otsustab mitte käia oma kaarti eemaldatakse hetkel mapist aga kaart jääb mängijale alles
                        väljund.seaText(mängija.getNimi() + " jättis oma kaardi käimata!");
                        mängijadKaardid.get(mängija).remove(kaart);
                    } else { // eri juht, kui ei ole kedagi valitud, kellele käia
                        väljund.seaText(mängija.getNimi() + ", kuna te ei valinud enne kinnitamist mängijat, peate ise jooma");
                    }


                    if (mängijadKaardid.get(mängija).size() == 0) { //Kui mängijal on kaardid otsas, siis eemaldab ta järjekorrast
                        mängijadKäia.remove(mängija);

                        if (mängijadKäia.isEmpty()) { //Kui rohkemad mängijad ei taha käia kaarte
                            kasKeeratud[0] = false;
                            edasi.seaText("Nüüd võite jälle keerata");
                        }else { // annab võimaluse järgmisele mängijale käimiseks
                            edasi.seaText(mängijadKäia.get(0).getNimi() + ", vali mängija nupule vajutades: kellele annad juua. \n Su kaart on " + mängijadKaardid.get(mängijadKäia.get(0)).get(0));
                        }
                    }else{ //Kui samalt mängijalt on ka teine kaart
                        edasi.seaText(mängijadKäia.get(0).getNimi() + ", vali mängija nupule vajutades: kellele annad juua. \n Su kaart on " + mängijadKaardid.get(mängijadKäia.get(0)).get(0));
                    }

                } else if(mängijadKäia.isEmpty()){ // kui juba alguses ei olnud kellelgi seda kaarti
                    väljund.setText("");
                    edasi.seaText("Ühelgi mängijal ei olnud seda kaarti, keerake järgmine kaart");
                    kasKeeratud[0] = false;
                }

            }
        });

        nõuded.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Stage kusimus = new Stage();
                Label label = new Label( "Esmalt tutvu mängu reeglitega, siis sisesta mängijate nimed. \n" +
                        "mängu mängimiseks peab sisestama vähemalt 2 mängijat enne, kui keerad esimest korda kaarti. \n "+
                        "Ei tohi valida samade nimedega mängijaid! \n" +
                        "Nimi ei tohi sisaldada jutumärke, ega ülakomasid! \n " +
                        "Nimed ei tohi olla tühjaks jäetud! \n" +
                        "Seejärel järgige üleval servas olevaid juhiseid. \n" +
                        "Ülemine tekst on toimunud tegevuse kirjeldus ja alumine ütleb mida edasi teha! \n"+
                        "Nupp \"Keera\" keerab järgmise kaardi, kuid seda saab teha alles siis, kui kõik mängijad on ära käinud.\n" +
                        "Nupp \"Kinnita valik\" kinnitab, et soovid teha käigu just viimasena valitud mängijale.\n" +
                        "Kui valid mängijaks \"Jätan käimata\" siis käiv ei kaota kaarti ja saab tulevikus sama kaarti kasutada. \n" +
                        "NB! MÄNG EI PROPAGEERI ALKOHOLI TARBIMIST, ALKOHOL VÕIB KAHJUSTADA TEIE TERVIST!");
                Button okButton = new Button("Välju");
                okButton.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        kusimus.hide();
                    }
                });
                FlowPane pane = new FlowPane(70, 70);
                pane.setAlignment(Pos.CENTER);
                pane.getChildren().addAll(okButton);

                VBox vBox = new VBox(70);
                vBox.setAlignment(Pos.CENTER);
                vBox.getChildren().addAll(label, pane);

                Scene stseen2 = new Scene(vBox);
                kusimus.setMinHeight(300); // alumine kõrguse piirajad
                kusimus.setMinWidth(300); //alumine laiuse piiraja
                kusimus.setScene(stseen2);
                kusimus.show();
            }
        });
        info.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Stage kusimus = new Stage();
                Label label = new Label("Mängus tehakse kaardipakist püramiid, alumises reas on 5 kaarti ja igas järgnevas\n" +
                        " reas on kaardi võrra vähem kui eelnevas. Ülejäänud kaardid jaotatakse mängijate vahel laiali. Seejärel\n" +
                        " hakatakse alumisest tasandist (kus on 5 kaarti) kaarte ümber pöörama, nii kaua kui jõutakse püramiidi tipu.\n" +
                        " Kui kaardi pööramisel on mängijal käes sama kaart, mis just püramiidis ilmnes, saab ta anda kaasmängijale \n" +
                        "lonkse, mida teine peab ära jooma. Esimesel tasandil saavad mängijad iga kaardi eest anda ühe lonksu ning \n" +
                        "iga kord kui minnakse uuele tasandile, tõuseb ka lonksude arv mida antakse ühe võrra.\n" +
                        "Kuna mängu võidab kõige kainem inimene ja see sõltub ka füüsisest, siis mäng lonksude üle arvet ei pea.\n"+
                        "NB! MÄNG EI PROPAGEERI ALKOHOLI TARBIMIST, ALKOHOL VÕIB KAHJUSTADA TEIE TERVIST!");

                Button okButton = new Button("Välju");
                okButton.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        kusimus.hide();
                    }
                });
                FlowPane pane = new FlowPane(70, 70);
                pane.setAlignment(Pos.CENTER);
                pane.getChildren().addAll(okButton);

                VBox vBox = new VBox(70);
                vBox.setAlignment(Pos.CENTER);
                vBox.getChildren().addAll(label, pane);

                Scene stseen2 = new Scene(vBox);
                kusimus.setMinHeight(300); // alumine kõrguse piirajad
                kusimus.setMinWidth(300); //alumine laiuse piiraja
                kusimus.setScene(stseen2);
                kusimus.show();
            }
        });

        logi.setOnMouseClicked(new EventHandler<MouseEvent>() { //Logi nupule vajutades näitab faili sisu
            @Override
            public void handle(MouseEvent mouseEvent) {
                Stage kusimus = new Stage();
                Label label =new Label();
                List<String> read= new ArrayList<>();
                try ( Scanner scanner = new Scanner(new File("logi.txt"))){ //Avab faili
                    while (scanner.hasNextLine()) {
                        String rida = scanner.nextLine();//Loeb read failist sisse
                        read.add(rida+"\n");
                    }
                    if (read.size()>10){
                        StringBuilder ehitaja = new StringBuilder();
                        for (int i = read.size()-10; i < read.size() ; i++) {//Teeb viimasest 10 reast stringi
                            ehitaja.append(read.get(i));
                        }
                        label.setText(String.valueOf(ehitaja)); //seab tehtud stringi tekstina väljundaknasse
                    } else {
                        StringBuilder ehitaja = new StringBuilder();
                        for (int i = 0; i < read.size() ; i++) {
                            ehitaja.append(read.get(i));
                        }
                        label.setText(String.valueOf(ehitaja)); //seab tehtud stringi tekstina väljundaknasse
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


                Button okButton = new Button("Välju");
                okButton.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        kusimus.hide();
                    }
                });
                FlowPane pane = new FlowPane(70, 70);
                pane.setAlignment(Pos.CENTER);
                pane.getChildren().addAll(okButton);

                VBox vBox = new VBox(70);
                vBox.setAlignment(Pos.CENTER);
                vBox.getChildren().addAll(label, pane);

                Scene stseen2 = new Scene(vBox);
                kusimus.setMinHeight(300); // alumine kõrguse piirajad
                kusimus.setMinWidth(300); //alumine laiuse piiraja
                kusimus.setScene(stseen2);
                kusimus.show();
            }
        });


        //piir.setMinSize(400, 400);


        // stseeni loomine ja näitamine
        Scene stseen1 = new Scene(piir, 500, 500, Color.SNOW);
        peaLava.setTitle("Sündmused");
        peaLava.setMinHeight(500); // alumine kõrguse piirajad
        peaLava.setMinWidth(500); //alumine laiuse piiraja
        peaLava.setScene(stseen1);
        peaLava.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    private void joonistaKaart(Kaart kaart, VBox püramiidG, int tase){ // eemaldab ühe kaardi kujutise ristküliku ja lisab kaardi numbri ja masti tekstina asemele
        HBox rida = (HBox) püramiidG.getChildren().get(5 - tase);
        rida.getChildren().remove(0);
        VBox nähtavKaart = new VBox();
        nähtavKaart.setTranslateX(120 - 30 * (5 - tase));
        Text mast = new Text(kaart.getMast());
        Text number = new Text(kaart.getNumber());
        nähtavKaart.getChildren().addAll(mast, number);
        rida.getChildren().add(nähtavKaart);
    }
}

