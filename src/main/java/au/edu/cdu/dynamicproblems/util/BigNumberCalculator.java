package au.edu.cdu.dynamicproblems.util;

import java.math.BigDecimal;
@Deprecated
public class BigNumberCalculator {
	private static final int DEF_DIV_SCALE=10;
	
	private BigNumberCalculator(){
		
	}
	
	
	public static double div(double v1,double v2){
		return div(v1,v2,DEF_DIV_SCALE);
	}
	
	public static double div(double v1,double v2,int scale){
		if(scale<0){
			throw new IllegalArgumentException("The scale must be positive.");
		}
		BigDecimal b1=new BigDecimal(Double.toString(v1));
		BigDecimal b2=new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale,BigDecimal.ROUND_HALF_UP).doubleValue();
		
	}
	
	public static double mul(double v1,double v2){
		BigDecimal b1=new BigDecimal(Double.toString(v1));
		BigDecimal b2=new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}
}

