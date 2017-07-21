package com.dk.pair;

import java.sql.Timestamp;

import com.dk.object.RfidInfo;
import com.dk.util.RFIDUtil;

public class Test {
	public static void main(String[] args) {
//		String str = "12345667788";
//		StringBuffer sb = new StringBuffer(str);
//		System.out.println(sb.substring(0,4));
//		System.out.println(sb.toString());
		
		Timestamp t1 = new Timestamp(System.currentTimeMillis());
		Timestamp t2 = new Timestamp(System.currentTimeMillis()+5000);
		System.out.println(t2.getTime()-t1.getTime());
		RFIDUtil util = new RFIDUtil();
		RfidInfo info = util.getInfo("011205000700000081260000386A040159FD05173000E280681000000039EC2F059F8110");
		System.out.println(System.currentTimeMillis()-info.getCreateTime().getTime());
	}

}
