package com.rootrip.platform.util;

/**
 * 
 * @company suteam
 * @author ning
 * @since 2009-3-31
 * @version 1.0
 */
public class Encrypter {   
    private static int MIN_ASC = 32;   
  
    private static int MAX_ASC = 126;   
  
    private static int NUM_ASC = MAX_ASC - MIN_ASC + 1;   
  
    private static long MYPRIMENUMBER = 100537;   
  
    private static long MYPRIMENUMBER2 = 100609;   
  
    private static String KEYWORD = "rootrip2012"; //密匙   
  
       
    private static String decoder(String from_text) {   
        long key;   
        double offset;   
        int str_len;   
        int ch;   
        char[] word = from_text.toCharArray();   
        String to_text = "";   
        key = numericPassword(KEYWORD);   
        str_len = from_text.length() - 1;   
        for (int i = 0; i < str_len; i++) {   
            word[i] = from_text.charAt(i);   
            ch = word[i];   
            if (ch >= MIN_ASC && ch <= MAX_ASC) {   
                i = i + 1;   
                ch = ch - MIN_ASC;   
                offset = (NUM_ASC + 1) * ((double) (((key * i) % MYPRIMENUMBER)) / (double) (MYPRIMENUMBER));   
                ch = ((ch - (int) (offset)) % NUM_ASC);   
                if (ch < 0)   
                    ch = ch + NUM_ASC;   
                ch = ch + MIN_ASC;   
                i = i - 1;   
                to_text += (char) (ch);   
            }   
        }   
        return to_text;   
    }   
  
    private static String encoder(String from_text) {   
        long key;   
        double offset;   
        int str_len;   
        int ch;   
        char[] word = from_text.toCharArray();   
        String to_text = "";   
        key = numericPassword(KEYWORD);   
        str_len = from_text.length() - 1;   
        for (int i = 0; i <= str_len; i++) {   
            word[i] = from_text.charAt(i);   
            ch = word[i];   
            if (ch >= MIN_ASC && ch <= MAX_ASC) {   
                i = i + 1;   
                ch = ch - MIN_ASC;   
                offset = (NUM_ASC + 1) * ((double) (((key * i) % MYPRIMENUMBER)) / (double) (MYPRIMENUMBER));   
                ch = ((ch + (int) (offset)) % NUM_ASC);   
                ch = ch + MIN_ASC;   
                i = i - 1;   
                to_text = to_text + (char) (ch);   
            }   
        }   
        return to_text + "a";   
    }   
  
    private static long numericPassword(String password) {   
        long value, ch, shift1, shift2;   
        int str_len;   
  
        shift1 = 0;   
        shift2 = 0;   
        value = 0;   
        str_len = password.length();   
        for (int i = 0; i < str_len; i++) {   
            ch = password.charAt(i);   
            value = value ^ (ch * index(shift1));   
            value = value ^ (ch * index(shift2));   
            shift1 = (shift1 + 7) % 19;   
            shift2 = (shift2 + 13) % 23;   
        }   
        value = (value ^ MYPRIMENUMBER2) % MYPRIMENUMBER;   
        return value;   
    }   
  
    private static long index(long shadow) {   
        long i, j;   
        j = 1;   
        for (i = 1; i <= shadow; i++)   
            j = j * 2;   
        return j;   
    }   
       
       
    private static String base64Encoder(String str){           
        String returnstr = (new sun.misc.BASE64Encoder()).encode(str.getBytes());   
        return returnstr;   
           
    }   
       
    private static String base64Decoder(String str) throws Exception{   
        String returnstr = new String((new sun.misc.BASE64Decoder()).decodeBuffer(str));   
        return returnstr;   
    }   
       
    /**
     * 加密
     * @param password
     * @return
     */
    public static String cipher(String password){
    	//FIXME 加解密不支持中文
        password = encoder(password);   
        password = base64Encoder(password);   
        return password;   
    }   
    /**
     * 解密
     * @param password
     * @return
     * @throws Exception
     */
    public static String decipher(String password) throws Exception{   
        password = base64Decoder(password);   
        password = decoder(password);   
        return password;   
    }  
}  
