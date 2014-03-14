package com.example.myapp_autoconnecttgt;


import java.security.MessageDigest;

public class Md5 {

	/**
	 * @param args
	 */
	public static String getMD5(String src){
		String s=null;
		char []hexDigits={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		MessageDigest md;
		try {
			byte []strTemp=src.getBytes("UTF-8");
			md = MessageDigest.getInstance("MD5");
			md.update(strTemp);
			byte tmp[]=md.digest();
			int j=tmp.length;
			char str[]=new char[j*2];
			for(int i=0,k=0;i<j;i++){
				byte byte0=tmp[i];
				str[k++]=hexDigits[byte0>>4&0xf];
				str[k++]=hexDigits[byte0&0xf];
			}
			s=new String(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return s;
	}
}
