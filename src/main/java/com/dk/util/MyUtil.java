package com.dk.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.dk.object.RfidInfo;

public class MyUtil {
	public static boolean addInfo(RfidInfo info){
		boolean flag = false;
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			String sql = "insert into rfid(name,epc,create_time,pc) values(?,?,?,?)";
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, info.getName());
			ps.setString(2, info.getEpc());
			ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			ps.setString(4, info.getPc());
			
			ps.execute();
			flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return flag;
	}
	
	public static RfidInfo getInfo(String str){
		Connection conn = null;
		PreparedStatement ps = null;
		RfidInfo info = null;
		
		try {
			String sql = "select * from rfid where epc = ? order by create_time desc limit 1";
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, str);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				info = new RfidInfo();
				info.setId(rs.getInt("id"));
				info.setName(rs.getString("name"));
				info.setEpc(rs.getString("epc"));
				info.setCreateTime(rs.getTimestamp("create_time"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return info;
	}

}
