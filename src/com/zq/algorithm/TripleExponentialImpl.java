package com.zq.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Description: TODO<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver.		Date		Author			Description<br>
 * ------------------------------------------------------<br>
 * 1.0		2018年7月26日 下午5:14:54		zhouqiang		created.<br>
 */

public class TripleExponentialImpl {

	/**
	 * This method is the entry point. It calculates the initial values and returns the forecast
	 * for the m periods.
	 * 
	 * @param y - Time series data.
	 * @param alpha - Exponential smoothing coefficients for level, trend, seasonal components.
	 * @param beta - Exponential smoothing coefficients for level, trend, seasonal components.
	 * @param gamma - Exponential smoothing coefficients for level, trend, seasonal components.
	 * @param perdiod - A complete season's data consists of L periods. And we need to estimate 
	 * the trend factor from one period to the next. To accomplish this, it is advisable to use 
	 * two complete seasons; that is, 2L periods. 
	 * @param m - Extrapolated future data points.
	 * @param debug - Print debug values. Useful for testing.
	 * 
     *				4 quarterly
     *     			7 weekly.
     *     			12 monthly
	 */
	public static double[] forecast(double[] y, double alpha, double beta,
			double gamma, int period, int m, boolean debug) {

		if (y == null) {
			return null;
		}

		int seasons = y.length / period;
		double a0 = calculateInitialLevel(y, period);
		double b0 = calculateInitialTrend(y, period);
		double[] initialSeasonalIndices = calculateSeasonalIndices(y, period, seasons);

		if (debug) {
			System.out.println(String.format(
					"Total observations: %d, Seasons %d, Periods %d", y.length,
					seasons, period));
			System.out.println("Initial level value a0: " + a0);
			System.out.println("Initial trend value b0: " + b0);
			printArray("Seasonal Indices: ", initialSeasonalIndices);
		}

		double[] forecast = calculateHoltWinters(y, a0, b0, alpha, beta, gamma,
				initialSeasonalIndices, period, m, debug);

//		if (debug) {
//			printArray("Forecast", forecast);
//		}

		return forecast;
	}
	
	/**
	 * This method realizes the Holt-Winters equations.
	 * 
	 * @param y
	 * @param a0
	 * @param b0
	 * @param alpha
	 * @param beta
	 * @param gamma
	 * @param initialSeasonalIndices
	 * @param period
	 * @param m
	 * @param debug
	 * @return - Forecast for m periods.
	 */
	private static double[] calculateHoltWinters(double[] y, double a0, double b0, double alpha,
			double beta, double gamma, double[] initialSeasonalIndices, int period, int m, boolean debug) {
		
		double[] St = new double[y.length];
		double[] Bt = new double[y.length];
		double[] It = new double[y.length];
		double[] Ft = new double[y.length + m];
		
		//Initialize base values
		St[1] = a0;
		Bt[1] = b0;
		   
		for (int i = 0; i < period; i++) {
			It[i] = initialSeasonalIndices[i];
		}
		
		Ft[m] = (St[0] + (m * Bt[0])) * It[0];//This is actually 0 since Bt[0] = 0
		Ft[m + 1] = (St[1] + (m * Bt[1])) * It[1];//Forecast starts from period + 2
		
		//Start calculations
		for (int i = 2; i < y.length; i++) {

			//Calculate overall smoothing
			if((i - period) >= 0) {
				St[i] = alpha * y[i] / It[i - period] + (1.0 - alpha) * (St[i - 1] + Bt[i - 1]);
			} else {
				St[i] = alpha * y[i] + (1.0 - alpha) * (St[i - 1] + Bt[i - 1]);
			}
			
			//Calculate trend smoothing
	        Bt[i] = gamma * (St[i] - St[i - 1]) + (1 - gamma) * Bt[i - 1];
	        
	        //Calculate seasonal smoothing
	        if((i - period) >= 0) {
	        	It[i] = beta * y[i] / St[i] + (1.0 - beta) * It[i - period];
	        }
	                                                      
            //Calculate forecast
	        if( ((i + m) >= period) ){
	        	Ft[i + m] = (St[i] + (m * Bt[i])) * It[i - period + m];
	        }
	        
	        if(debug){
				System.out.println(String.format(
						"i = %d, y = %f, S = %f, Bt = %f, It = %f, F = %f", i,
						y[i], St[i], Bt[i], It[i], Ft[i]));
	        }
		}
		
		return Ft;
	}

	/**
	 * See: http://robjhyndman.com/researchtips/hw-initialization/
	 * 1st period's average can be taken. But y[0] works better.
	 * 
	 * @return - Initial Level value i.e. St[1]
	 */
	private static double calculateInitialLevel(double[] y, int period) {

		/**		
 		double sum = 0;
		for (int i = 0; i < period; i++) {
			sum += y[i];
		}
		
		return sum / period;
		 **/
		return y[0];
	}
	
	/**
	 * See: http://www.itl.nist.gov/div898/handbook/pmc/section4/pmc435.htm
	 * 
	 * @return - Initial trend - Bt[1]
	 */
	private static double calculateInitialTrend(double[] y, int period){
		
		double sum = 0;
		
		for (int i = 0; i < period; i++) {			
			sum += (y[period + i] - y[i]);
		}
		
		return sum / (period * period);
	}
	
	/**
	 * See: http://www.itl.nist.gov/div898/handbook/pmc/section4/pmc435.htm
	 * 
	 * @return - Seasonal Indices.
	 */
	private static double[] calculateSeasonalIndices(double[] y, int period, int seasons){
						
		double[] seasonalAverage = new double[seasons];
		double[] seasonalIndices = new double[period];
		
		double[] averagedObservations = new double[y.length];
		
		for (int i = 0; i < seasons; i++) {
			for (int j = 0; j < period; j++) {
				seasonalAverage[i] += y[(i * period) + j];
			}
			seasonalAverage[i] /= period;
		}
		
		for (int i = 0; i < seasons; i++) {
			for (int j = 0; j < period; j++) {
				averagedObservations[(i * period) + j] = y[(i * period) + j] / seasonalAverage[i];				
			}			
		}
		
		for (int i = 0; i < period; i++) {
			for (int j = 0; j < seasons; j++) {
				seasonalIndices[i] += averagedObservations[(j * period) + i];
			}			
			seasonalIndices[i] /= seasons;
		}
		
		return seasonalIndices;
	}
	
	/**
	 * Utility method to pring array values.
	 * 
	 * @param description
	 * @param data
	 */
	private static void printArray(String description, double[] data){
		
		System.out.println(String.format("******************* %s *********************", description));
		
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i]);
		}
		
		System.out.println(String.format("*****************************************************************", description));
	}
	
	public static void main(String[] args){
    	double[] real = {31773.765,17116.49,81519.713,57248.53,33990.332,61210.585,57077.519,106307.933,39954.826,24895.288,15297.352,151371.407,35801.957,105676.606,53746.672,86262.584,74237.717,63931.305,132065.675,34726.882,138613.448,107741.231,188604.036,145356.157,57224.113,143635.643,133900.53,227072.753,77458.186,144360.848,93214.68,125437.039,155853.47,135167.989,109583.664,118412.39,303088.665,129800.793,183406.617,283022.19,326034.834,20175.186,250684.837,66831.313,244159.015,126008.408,120872.832,94349.036,69687.193,87630.542,32620.101,102026.062,122729.778,94683.799,96068.678,49078.128,131389.371,83254.391,82667.877,162391.404,145428.188,84276.216,65362.418,34243.511,109182.084,140187.967,109597.13,65507.332,48143.879,179198.741,77541.915,90216.115,72319.825,127654.692,128521.948,39276.632,145788.813,100016.817,78850.418,136036.168,123318.86,70370.589,120809.516,64386.846,125063.561,138262.734,89819.657,88287.041,77732.843,128242.855,301394.43,69334.615,240650.225,104529.396,110336.767,205937.775,126321.616,222888.217,47683.059,92197.073,118161.473,32171.097,196736.739,197166.797,121368.714,70011.745,41382.811,104114.283,195668.496,177981.539,168254.159,171686.782,55633.782,25476.42,128944.39,216882.825,224466.235,116471.271,183345.904,83445.911,58076.987,60632.747,78333.366,203165.627,159638.921,122520.97,370216.223,124932.382,86380.819,138989.583,107794.139,104191.541,134713.315,104953.549,82068.436,128817.73,88544.02,149312.567,101062.219,220867.497,132699.86,96224.045,176007.051,135201.141,67183.329,174539.863,26338.905};
    	double alpha = 0.1, beta = 0.45, gamma = 0;
    	int period = 1, m = 145;
    	boolean debug = true;
    	double[] predict = forecast(real, alpha, beta, gamma, period, m, debug);
    	System.out.println("-----------predict----------------------------------");
    	for(int i = real.length; i < predict.length; i++){
    		System.out.println(predict[i]);
    	}
    	
	}
}