package sample;

import javafx.scene.text.Text;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MinuText extends Text {
    private String tekst;


    public MinuText(String text) throws IOException {
        super(text);
        tekst  = text;
    }

    public MinuText() throws IOException {
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void seaText(String text){
        tekst = text;
        this.setText(text);
        try (Writer kirjutaja = new BufferedWriter(new FileWriter("logi.txt", true));
        ){
            kirjutaja.write(text + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void lisaText(String text){
        tekst = tekst + "\n" + text;
        this.seaText(tekst);
    }
}
