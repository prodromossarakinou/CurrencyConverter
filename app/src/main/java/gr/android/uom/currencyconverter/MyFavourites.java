package gr.android.uom.currencyconverter;


//κλάση MyFavourites
//Η οποία χρησιμοποιείται για την προβολή της λίστα απο την MyFavourites
public class MyFavourites implements Comparable<MyFavourites> {
    //περιέχει μια παράμετρο text η οποία είναι το text το οποίο εμφανίζεται στην λίστα του MyFavourites
    //και ένα id το οποίο είναι το id του Document απο το firestore
    //το Id το χρησιμοποιεί για να διευλυνεί άλλες μεθόδους όπως για παράδειγμα το delete.
    private String text;
    private String id;
    public MyFavourites(String text, String id) {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }
    //επικάληψη της μεθόδου compareΤο ώστε να μπορει να ταξινομηθεί η λίστα
    @Override
    public int compareTo(MyFavourites o) {
        return this.text.compareTo(o.getText());

    }
    //Επικάληψη της toString ώστε να επιστρέφει το text
    //πλεονασμός αφού υπάρχει η getText.
    @Override
    public String toString(){
        return text;
    }


}
