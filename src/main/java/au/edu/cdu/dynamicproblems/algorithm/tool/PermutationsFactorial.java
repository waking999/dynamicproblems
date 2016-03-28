package au.edu.cdu.dynamicproblems.algorithm.tool;

public class PermutationsFactorial {
	private static final double E=2.71828;
	private static final double PIE_TOW_SQRT=2.5066;
	public static double stirling(int k){
		double rtn=PIE_TOW_SQRT*Math.sqrt(k)*Math.pow(k/E, k);
		
		return rtn;
		
	}
	
	public static void main(String[] agrs){
//		for(int k=0;k<=0;k++){
//			System.out.println(k+":"+stirling(k));
//		}
		
		int k=20;
		int r=0;
		if(k%2==0){
			r=k/2;
		}
		if(k%2!=0){
			r=(k+1)/2;
		}
		
		System.out.println(k+","+r+":"+numberOfCombination(k,r));
		System.out.println("2^"+k+":"+Math.pow(2, k));
		
	}
	
	public static double numberOfCombination(int k,int r){
		if(r==0){
			return 1;
		}
		if(k==r){
			return 1;
		}
		if (r>k){
			return -1;
		}
		
		double rtn=stirling(k)/stirling(r)/stirling(k-r);
		return rtn;
	}
	
}
