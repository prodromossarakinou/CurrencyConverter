package gr.android.uom.currencyconverter;

public class Favourites implements Comparable<Favourites>{
    private String text;
    private String id;

    public Favourites(String text, String id) {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(Favourites o) {
        return o.getText().compareTo(this.text);

    }
    @Override
    public String toString(){
        return text;
    }
}
