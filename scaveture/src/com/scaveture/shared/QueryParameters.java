package com.scaveture.shared;

import java.util.HashMap;
import java.util.Map;

public class QueryParameters {
    private Map<String, String> parms = new HashMap<String, String>();
    
    public QueryParameters(String query) {
        if(query.startsWith("?")) {
            query = query.substring(1);
        }
        String[] pairs = query.split("&");
        for(String pair : pairs) {
            String[] values = pair.split("=");
            if(values.length == 2) {
                parms.put(values[0].trim(), values[1].trim());
            }
            else if(values.length == 1) { // empty parm
                parms.put(values[0].trim(), "");
            }
        }
    }
    
    public String getString(String key) {
        if(parms.containsKey(key)) {
            return parms.get(key);
        }
        return null;
    }
    
    public long getLong(String key) {
        long value = -1L;
        if(parms.containsKey(key)) {
            try {
                value = Long.parseLong(parms.get(key));
            }
            catch(NumberFormatException ex) {
                // eat it for now
            }
        }
        return value;
    }
    
    public int getInteger(String key) {
        int value = -1;
        if(parms.containsKey(key)) {
            try {
                value = Integer.parseInt(parms.get(key));
            }
            catch(NumberFormatException ex) {
                // eat it for now
            }
        }
        return value;
    }
}
