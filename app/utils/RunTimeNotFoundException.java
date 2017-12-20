package utils;

public class RunTimeNotFoundException extends  RuntimeException{
    private String message ;
    public RunTimeNotFoundException(String m){
        message = m;
    }
}
