package com.lsh.wms.core.dao.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by zengwenjun on 15/12/26.
 */
public class NsHead {
    public int id=0;
    public int version=0;
    public long log_id=0;
    public byte []provider=new byte[16];
    public long magic_num=0;
    public long reserved=0;
    public long body_len=0;
    private static final long CONST_MAGIC_NUM = 0xfb709394L;

    public NsHead(){

    }

    public byte[] pack(){
        magic_num = CONST_MAGIC_NUM;
        ByteBuffer buffer=ByteBuffer.allocate(36).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(24,(int)magic_num&0xFFFFFFFF);
        buffer.putInt(32,(int)body_len&0xFFFFFFFF);
        return buffer.array();
        /*

        ByteConvert.ushortToBytes(id, buffer, 0);
        ByteConvert.ushortToBytes(version, buffer, 2);
        ByteConvert.uintToBytes(log_id, buffer, 4);
        Array.copy(provider, 0, buffer, 8, 16);
        ByteConvert.uintToBytes(magic_num, buffer, 24);
        ByteConvert.uintToBytes(reserved, buffer, 28);
        ByteConvert.uintToBytes(body_len, buffer, 32);
        return buffer;
        */
    }

    public void unpack(byte[]bytes) throws Exception {
        ByteBuffer buffer=ByteBuffer.allocate(36).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(bytes);
        buffer.rewind();
        magic_num = buffer.getInt(24)&0x0FFFFFFFFL;
        body_len = buffer.getInt(32)&0x0FFFFFFFFL;
        /*
        id = (ByteConvert.bytesToUshort(bytes,0))&0x0FFFF;
        version = (ByteConvert.bytesToUshort(bytes,2))&0x0FFFF;
        log_id = (ByteConvert.bytesToUshort(bytes, 4))&0x0FFFFFFFFL;
        Array.copy(bytes, 8, provider, 0, 16);
        magic_num = (ByteConvert.bytesToUint(bytes, 24))&0x0FFFFFFFFL;
        reserved = (ByteConvert.bytesToUint(bytes, 28))&0x0FFFFFFFFL;
        body_len = (ByteConvert.bytesToUint(bytes, 32))&0x0FFFFFFFFL;
        */
        if(magic_num!=CONST_MAGIC_NUM){
            throw new Exception("MAGIC NUM ERR "+CONST_MAGIC_NUM+" "+magic_num+" "+body_len);
        }
    }

    public void echo(){
        System.out.println("id : "+id);
        System.out.println("version : "+version);
        System.out.println("log_id : "+log_id);
        System.out.println("reserved : "+reserved);
        System.out.println("body_len : "+body_len);
        System.out.println("magic_num : " + magic_num);
    }

}
