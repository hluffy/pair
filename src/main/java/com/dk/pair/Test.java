package com.dk.pair;

import java.sql.Timestamp;

public class Test {
	public static void main(String[] args) {
//		String str = "12345667788";
//		StringBuffer sb = new StringBuffer(str);
//		System.out.println(sb.substring(0,4));
//		System.out.println(sb.toString());
		
		Timestamp t1 = new Timestamp(System.currentTimeMillis());
		Timestamp t2 = new Timestamp(System.currentTimeMillis()+5000);
		System.out.println(t2.getTime()-t1.getTime());
	}

}
