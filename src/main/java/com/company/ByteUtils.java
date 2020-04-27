package com.company;
/**
 * 用于实现基本数据类型和字节数组的转换
 * @version 1.1
 * @author qyf
 * @date 2019.12.5
 */
public class ByteUtils {

    public static byte[] int2Bytes(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) ((i & 0xff000000) >> 24);
        b[1] = (byte) ((i & 0x00ff0000) >> 16);
        b[2] = (byte) ((i & 0x0000ff00) >> 8);
        b[3] = (byte)  (i & 0x000000ff);
        return b;
    }

    public static byte[] short2Bytes(short s) {
        byte[] b = new byte[2];
        b[0] = (byte) ((s & 0xff00) >> 8);
        b[1] = (byte) (s & 0x00ff);
        return b;
    }

    public static byte[] float2Bytes(float f){
        int floatBit=Float.floatToIntBits(f);
        return int2Bytes(floatBit);
    }

    public static int bytes2Int(byte[] arr, int index) {
        return 	(0xff000000 & (arr[index] << 24))  |
                (0x00ff0000 & (arr[index+1] << 16))  |
                (0x0000ff00 & (arr[index+2] << 8))   |
                (0x000000ff &  arr[index+3]);
    }

    public static int bytes2Short(byte[] arr, int index) {
        return 	(0xff00 & (arr[index] << 8))  |
                (0x00ff & arr[index+1]);
    }

    public static float bytes2Float(byte[] arr,int index){
        int i=bytes2Int(arr,index);
        return Float.intBitsToFloat(i);
    }

    public static void printHexString(String s, byte[] b)
    {
        System.out.print(s);
        for (int i = 0; i < b.length; i++)
        {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase() + " ");
        }
        System.out.println();
    }


}
