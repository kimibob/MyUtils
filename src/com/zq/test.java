package com.zq;

import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * Description: TODO<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver. Date Author Description<br>
 * ------------------------------------------------------<br>
 * 1.0 2019年1月11日 下午5:44:00 zhouqiang created.<br>
 */

class WorkThread extends Thread
{
    @Override
    public void run()
    {
        while(true)
        {
            try {
                Thread.sleep((long) 0.5);//先释放资源，避免cpu占用过高
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
    }       
}

public class test {
	public static void main(String[] args) {
		
		for (int i = 0; i < 4; i++) {
			WorkThread a = new WorkThread();
			a.start();
		}
	}
}
