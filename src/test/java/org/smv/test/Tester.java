package org.smv.test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Tester {
    public static void main(String[] args)
    {
        String str = "test.do/ni";
        System.out.println(str.matches("test.do"));
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> table = new Hashtable();
        map.put(null, "dingxiangyong");
        table.put(null, "dingxiangyong");
        System.out.println(map.get(null));
        //System.out.println(table.get(null));
        
    }
}
