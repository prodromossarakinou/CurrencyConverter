package gr.android.uom.currencyconverter;



public class Currencies{

    public static final String TITLE_KEY = "Code";
    public static final String BODY_KEY = "Name";

    private String Name;
    private String Code;
    public Currencies(String name,String code){
        Name = name;
        Code = code;


    }


    public void setName(String name){
        Name = name;

    }
    public void setCode(String code){
        Code = code;

    }
    public String getName(){
        return Name;

    }
    public String getCode(){
        return Code;

    }


}
