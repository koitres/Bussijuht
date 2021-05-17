package sample;
public class Kaart{
    private String mast;
    private String number;
//    private boolean mängitud; // võibolla millalgi vaja

    public Kaart(String mast, String number) {
        this.mast = mast;
        this.number = number;
    }

    public String getMast() {
        return mast;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return mast + " " + number;
    }
}



