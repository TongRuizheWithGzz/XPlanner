package com.codemover.xplanner.Service;


import java.text.ParseException;
import java.util.HashMap;

public interface ReaderService {
    public HashMap<String,String> extractDate(String in) throws ParseException;


}
