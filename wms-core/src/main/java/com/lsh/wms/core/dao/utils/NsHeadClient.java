package com.lsh.wms.core.dao.utils;

/**
 * Created by zengwenjun on 16/12/7.
 */
import java.net.Socket;
import java.io.*;

public class NsHeadClient {
    public static String jsonCall(String ip, int port, String jsonInput){
        NsHead writeHead = new NsHead();
        Socket client = null;
        try {
            byte[]  writeBytes = jsonInput.getBytes("UTF-8");
            writeHead.body_len = writeBytes.length;
            client = new Socket(ip, port);
            client.getOutputStream().write(writeHead.pack());
            client.getOutputStream().write(writeBytes);
            byte[] readHead = new byte[36];
            client.getInputStream().read(readHead);
            NsHead head = new NsHead();
            head.unpack(readHead);
            byte[] readBytes = new byte[(int) head.body_len];
            int offset = 0;
            int leftByteNum = (int) head.body_len;
            while (leftByteNum > 0) {
                int len = client.getInputStream().read(readBytes, offset, leftByteNum);
                leftByteNum -= len;
                offset += len;
            }
            return new String(readBytes, "UTF-8");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(client != null) {
                try {
                    client.close();
                }catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public static void main(String[]args){
        String rst = NsHeadClient.jsonCall("127.0.0.1", 8002, "test");
        System.out.println(rst);
    }
}
