package com.zq.algorithm.dijkstra;

import java.util.BitSet;

public class test {
	public static void main(String[] args) throws InterruptedException {
//		BitSet bits1 = new BitSet(16);
//	     BitSet bits2 = new BitSet(16);
//	      
//	     // set some bits
//	     for(int i=0; i<16; i++) {
//	        if((i%2) == 0) bits1.set(i);
//	        if((i%5) != 0) bits2.set(i);
//	     }
//	     System.out.println("Initial pattern in bits1: ");
//	     System.out.println(bits1);
//	     System.out.println("\nInitial pattern in bits2: ");
//	     System.out.println(bits2);
//	     System.out.println(bits1.get(0)+"--"+bits1.get(1));
//	     printBitSet(bits1);  
		Thread.sleep(20000);
		byte[] b = new byte[1000000000];
		Thread.sleep(20000);
		b=null;
		System.gc();
		Thread.sleep(100000);
	}
	
	public static void printBitSet(BitSet bs){  
        StringBuffer buf=new StringBuffer();  
        buf.append("[\n");  
        for(int i=0;i<bs.size();i++){  
            if(i<bs.size()-1){  
                buf.append(getBitTo10(bs.get(i))+",");  
            }else{  
                buf.append(getBitTo10(bs.get(i)));  
            }  
            if((i+1)%8==0&&i!=0){  
                buf.append("\n");  
            }  
        }  
        buf.append("]");  
        System.out.println(buf.toString());  
    }  
	//true,false换成1,0为了好看  
    public static String getBitTo10(boolean flag){  
        String a="";  
        if(flag==true){  
            return "1";  
        }else{  
            return "0";  
        }  
    }  
}
