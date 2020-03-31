package com.company;


/**
 * 新松机器人通信协议消息头
 * @version 1.1
 * @author qyf
 * @date 2019.12.5
 */
public class SiasunMsgHead {
    private final int mark=0x0A050A05;
    private byte flag;
    private int code;
    private short length;
    private short seqNum;
    private byte isLastPackage;
    private byte bodyChecksum;
    private final int reserved=0;
    private byte headChecksum;

    public byte getFlag() {
        return flag;
    }

    public void setFlag(boolean isResponseFrame,boolean needResponse) {
        if (isResponseFrame&&needResponse) this.flag=(byte)0b11000000;
        else if (isResponseFrame&&!needResponse) this.flag=(byte)0b10000000;
        else if (!isResponseFrame&&needResponse) this.flag=0b01000000;
        else if (!isResponseFrame&&!needResponse) this.flag=0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public short getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(short seqNum) {
        this.seqNum = seqNum;
    }

    public boolean getIsLastPackage() {
        return isLastPackage==1;
    }

    public void setIsLastPackage(boolean isLastPackage) {
        this.isLastPackage = (byte)(isLastPackage?1:0);
    }

    public byte getBodyChecksum() {
        return bodyChecksum;
    }

    public void setBodyChecksum(byte bodyChecksum) {
        this.bodyChecksum = bodyChecksum;
    }

    public byte getHeadChecksum() {
        return headChecksum;
    }

    public void setHeadChecksum(byte headChecksum) {
        this.headChecksum = headChecksum;
    }

    public byte calcHeadChecksum(){
        byte[] bytes=new byte[19];
        short result=0;
        this.byteAssign(bytes,0,this.mark);
        this.byteAssign(bytes,4,this.flag);
        this.byteAssign(bytes,5,this.code);
        this.byteAssign(bytes,9,this.length);
        this.byteAssign(bytes,11,this.seqNum);
        this.byteAssign(bytes,13,this.isLastPackage);
        this.byteAssign(bytes,14,this.bodyChecksum);
        this.byteAssign(bytes,15,this.reserved);
        for (int i=0;i<19;i++){
            result+=bytes[i];
            if (result>=0x100){
                result-=0x100;
                result+=1;
            }
        }
        return (byte)((byte)result^(byte)0b11111111);
    }

    public byte[] toByteArray(){
        byte[] bytes=new byte[20];
        this.byteAssign(bytes,0,this.mark);
        this.byteAssign(bytes,4,this.flag);
        this.byteAssign(bytes,5,this.code);
        this.byteAssign(bytes,9,this.length);
        this.byteAssign(bytes,11,this.seqNum);
        this.byteAssign(bytes,13,this.isLastPackage);
        this.byteAssign(bytes,14,this.bodyChecksum);
        this.byteAssign(bytes,15,this.reserved);
        this.byteAssign(bytes,19,this.headChecksum);
        return bytes;
    }

    private void byteAssign(byte[] target,int index,int src){
        byte[] bytes= ByteUtils.int2Bytes(src);
        System.arraycopy(bytes,0,target,index,4);
    }

    private void byteAssign(byte[] target,int index,short src){
        byte[] bytes= ByteUtils.short2Bytes(src);
        System.arraycopy(bytes,0,target,index,2);
    }

    private void byteAssign(byte[] target,int index,byte src){
        target[index]=src;
    }
}
