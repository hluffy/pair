package com.dk.pair;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import com.dk.object.RfidInfo;
import com.dk.util.RFIDUtil;

public class MainTest {
	static SerialPort serialPort = null;
	static StringBuffer sb = new StringBuffer();
	static BufferedInputStream bis = null;
	static RFIDUtil util = new RFIDUtil();
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		 //获得当前所有可用串口
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();    
        
        ArrayList<String> portNameList = new ArrayList<String>();

        //将可用串口名添加到List并返回该List
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            System.out.println(portName);
            portNameList.add(portName);
        }
        
        byte[] sendcmd2 = {0x01,0x00,0x00,0x07,(byte) 0xFF,(byte)0xFF,0x00,0x00};//写寄存器，设置循环读卡模式
        byte[] sendcmd5={0x01,0x00,0x00,(byte)0xF0,0x0F,0x00,0x00,0x00}; //写寄存器，执行读卡命令
        if(portNameList.size()!=0){
        	for (String portName : portNameList) {
				serialPort = openPort(portName,115200);
				sendToPort(serialPort,sendcmd2);
				sendToPort(serialPort,sendcmd5);
				addListener(serialPort, new SerialPortPort());
//				byte[] data = null;
//				while(true){
//					data = readFromPort(serialPort);
//					if(data!=null){
//						System.out.println(byteToString(data));
//					}
//				}
			}
        }
        
//        System.out.println(openPort("com5", 38400));
        
	}
	
	
	
	
	/**
     * 添加监听器
     * @param port     串口对象
     * @param listener 串口监听器
     * @throws TooManyListeners 监听类对象过多
     */
    public static void addListener(SerialPort port, SerialPortEventListener listener) {

        try {
            
            //给串口添加监听器
            port.addEventListener(listener);
            //设置当有数据到达时唤醒监听接收线程
            port.notifyOnDataAvailable(true);
          //设置当通信中断时唤醒中断线程
            port.notifyOnBreakInterrupt(true);

        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }
	
	/**
     * 从串口读取数据
     * @param serialPort 当前已建立连接的SerialPort对象
     * @return 读取到的数据
     * @throws ReadDataFromSerialPortFailure 从串口读取数据时出错
     * @throws SerialPortInputStreamCloseFailure 关闭串口对象输入流出错
     */
    public static byte[] readFromPort(SerialPort serialPort){

        InputStream in = null;
        byte[] bytes = null;

        try {
        	in = serialPort.getInputStream();
            
            
            int bufflenth = in.available();        //获取buffer里的数据长度
//            System.out.println("bufflenth------------------"+bufflenth);
            bis = new BufferedInputStream(in,36);
            while (bufflenth != 0) {                             
                bytes = new byte[bufflenth];    //初始化byte数组为buffer中数据的长度
                bis.read(bytes);
                bufflenth = in.available();
            }
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch(IOException e) {
                e.printStackTrace();
            }

        }

        return bytes;

    }
    
    /**
     * 往串口发送数据
     * @param serialPort 串口对象
     * @param order    待发送数据
     * @throws SendDataToSerialPortFailure 向串口发送数据失败
     * @throws SerialPortOutputStreamCloseFailure 关闭串口对象的输出流出错
     */
    public static void sendToPort(SerialPort serialPort, byte[] order) {

        OutputStream out = null;
        
        try {
            
            out = serialPort.getOutputStream();
            out.write(order);
            out.flush();
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
	
	 /**
     * 打开串口
     * @param portName 端口名称
     * @param baudrate 波特率
     * @return 串口对象
	 * @throws Exception 
     * @throws SerialPortParameterFailure 设置串口参数失败
     * @throws NotASerialPort 端口指向设备不是串口类型
     * @throws NoSuchPort 没有该端口对应的串口设备
     * @throws PortInUse 端口已被占用
     */
    public static final SerialPort openPort(String portName, int baudrate) throws Exception {


            //通过端口名识别端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            //打开端口，并给端口名字和一个timeout（打开操作的超时时间）
            CommPort commPort = portIdentifier.open(portName, 2000);

            //判断是不是串口
            if (commPort instanceof SerialPort) {
                
                SerialPort serialPort = (SerialPort) commPort;
                
                try {                        
                    //设置一下串口的波特率等参数
                    serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);                              
                } catch (UnsupportedCommOperationException e) {  
                    e.printStackTrace();
                }
                
                //System.out.println("Open " + portName + " sucessfully !");
                return serialPort;
            
            }        
            else {
                //不是串口
                System.out.println("不是串口");
                return null;
            }
    }
    
    private static String byteToString(byte[] req){
    	StringBuffer str = new StringBuffer();
    	List<String> strs = new ArrayList<String>();
        for(int i = 0;i<req.length;i++){
        	int v = req[i] & 0xFF;
        	String hv = Integer.toHexString(v).toUpperCase();
        	if(hv.length() < 2){
        		strs.add("0"+hv);
        	}
        	else{
        		strs.add(hv);
        	}
        }
        for (String string : strs) {
			str.append(string);
		}
        
        return str.toString();
    }
    
    private static class SerialPortPort implements SerialPortEventListener{
    	

    	public void serialEvent(SerialPortEvent serialPortEvent) {
    		// TODO Auto-generated method stub
    		switch (serialPortEvent.getEventType()) {

	        case SerialPortEvent.BI: // 10 通讯中断
	            break;

	        case SerialPortEvent.OE: // 7 溢位（溢出）错误

	        case SerialPortEvent.FE: // 9 帧错误

	        case SerialPortEvent.PE: // 8 奇偶校验错误

	        case SerialPortEvent.CD: // 6 载波检测

	        case SerialPortEvent.CTS: // 3 清除待发送数据

	        case SerialPortEvent.DSR: // 4 待发送数据准备好了

	        case SerialPortEvent.RI: // 5 振铃指示

	        case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
	            break;
	        
	        case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
	            
	            //System.out.println("found data");
	        	byte[] data = null;
	            
	            try {
	                if (serialPort == null) {
	                	
	                }
	                else {
	                    data = readFromPort(serialPort);    //读取数据，存入字节数组
//	                    System.out.println(new String(data,"GBK"));
	                    String str = byteToString(data);
//	                    System.out.println(str);
	                    sb.append(str);
	                    
	                    if(sb.length()==72){
	                    	final String rfidData = sb.toString();
	                    	sb.setLength(0);
	                    	System.out.println(rfidData);
	                    	if("0112".equals(rfidData.substring(0,4))){
	                    		new Thread(new Runnable() {
									
									public void run() {
										// TODO Auto-generated method stub
										RfidInfo info = util.getInfo(rfidData);
										if(info.getCreateTime()!=null&&System.currentTimeMillis()-info.getCreateTime().getTime()>3000){
											boolean flag = util.addInfo(rfidData);
				                    		if(flag){
				                    			System.out.println("保存成功");
				                    		}else{
				                    			System.out.println("保存失败");
				                    		}
										}
									}
								}).start();
	                    		
	                    	}
	                    }
	                    if(sb.length()>72){
	                    	sb.setLength(0);
	                    }
	                    
//	                    String head = null;
//	                    if(sb.length()>=4){
//	                    	head = sb.substring(0,4);
//	                    }
//	                    if(sb.length()==24&&"0100".equals(head)){
//	                    	System.out.println(sb.toString());
//	                    	sb.setLength(0);
//	                    }else if(sb.length()>24&&"0100".equals(head)){
//	                    	System.out.println(sb.substring(0,24));
//	                    	String a = sb.substring(24);
//	                    	sb.setLength(0);
//	                    	sb.append(a);
//	                    }else if(sb.length()==72&&"0112".equals(head)){
//	                    	String a = sb.toString();
//	                    	 System.out.println(a);
//	                    	 RfidInfo info = util.getInfo(a);
//	                    	 if(info.getCreateTime()!=null&&System.currentTimeMillis()-info.getCreateTime().getTime()>5000){
//	                    		 boolean flag = util.addInfo(str);
//	                    		 if(flag){
//	                    			 System.out.println("保存成功");
//	                    		 }else{
//	                    			 System.out.println("保存失败");
//	                    		 }
//	                    	 }
//	                    	 sb.setLength(0);
//	                    }else if(sb.length()>72&&"0112".equals(head)){
//	                    	System.out.println(sb.substring(0,72));
//	                    	String a = sb.substring(72);
//	                    	RfidInfo info = util.getInfo(a);
//	                    	if(info.getCreateTime()!=null&&System.currentTimeMillis()-info.getCreateTime().getTime()>5000){
//	                    		 boolean flag = util.addInfo(str);
//	                    		 if(flag){
//	                    			 System.out.println("保存成功");
//	                    		 }else{
//	                    			 System.out.println("保存失败");
//	                    		 }
//	                    	}
//	                    	sb.setLength(0);
//	                    	sb.append(a);
////	                    	System.out.println(sb.toString());
//	                    }else if(sb.length()<4){
//	                    	
//	                    }else if("0100".equals(head)||"0112".equals(head)){
//	                    	
//	                    }else{
////	                    	System.out.println(sb.toString());
//	                    	sb.setLength(0);
//	                    }
	                    
//	                    if(sb.length()>=4){
//	                    	String a = sb.substring(0,4);
//	                    	System.out.println(a);
//	                    	if("0112".equals(a)){
//	                    		if(sb.length()>=72){
//	                    			String a1 = sb.substring(0,72);
//	                    			System.out.println(a1);
//	                    			String a2 = sb.substring(72);
//	                    			sb.setLength(0);
//	                    			sb.append(a2);
//	                    		}
//	                    	}else
//	                    	if("0100".equals(a)){
//	                    		if(sb.length()>=24){
//	                    			String a1 = sb.substring(0,24);
//	                    			System.out.println(a1);
//	                    			String a2 = sb.substring(24);
//	                    			sb.setLength(0);
//	                    			sb.append(a2);
//	                    		}
//	                    	}else{
//	                    		System.out.println(sb.toString());
//	                    		sb.setLength(0);
//	                    	}
//	                    }
	                    
	                }
	            } catch (Exception e) {
	                System.exit(0);    //发生读取错误时显示错误信息后退出系统
	            }    
	            break;
			}

    		
    	}
    	
    }
    

}


