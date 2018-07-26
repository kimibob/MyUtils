package com.zq.algorithm.quicksort;

import java.util.ArrayList;
import java.util.Arrays;

public class Qsort {
	public static void main(String[] args) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		array.add(1);
		array.add(4);
		array.add(2);
		array.add(0);
		array.add(5);
		array.add(3);
		System.out.println(quicksort(array));
		int[] arr = {1,4,2,5,3,6};
		qsort(arr, 0, arr.length-1);
		System.out.println(Arrays.toString(arr));
	}
	
	public static ArrayList<Integer> quicksort(ArrayList<Integer> array ){
		if(array.size() <2){
			return array;
		}else{
			ArrayList<Integer> less = new ArrayList<Integer>();
			ArrayList<Integer> greater = new ArrayList<Integer>();
			int pivot = array.get(0);
			for (int i = 1; i < array.size(); i++) {
				if(array.get(i) <= pivot){
					less.add(array.get(i));
				}else{
					greater.add(array.get(i));
				}
			}
			ArrayList<Integer> a = quicksort(less);
			a.add(pivot);
			a.addAll(quicksort(greater));
			return a;
		}
	}
	
	public static void qsort(int[] arr, int low, int high){
	    if (low < high){
	        int pivot=partition(arr, low, high);        //将数组分为两部分
	        qsort(arr, low, pivot-1);                   //递归排序左子数组
	        qsort(arr, pivot+1, high);                  //递归排序右子数组
	    }
	}
	private static int partition(int[] arr, int low, int high){
	    int pivot = arr[low];     //默认选数组中第一个当做中轴记录
	    while (low<high){		  //当low等于high的时候完成第一次排中轴记录定位(此时中轴记录左边的都比它小，右边的都比它大)
	        while (low<high && arr[high]>=pivot) {
	        	high--;
	        }
	        arr[low]=arr[high];             //交换比中轴小的记录到左端
	        while (low<high && arr[low]<=pivot) {
	        	low++;
	        }
	        arr[high] = arr[low];           //交换比中轴小的记录到右端
	    }
	    //扫描完成，中轴到位
	    arr[low] = pivot;
	    //返回的是该中轴记录的最终正确位置(low==high)
	    return low;
	}
}
