package au.edu.cdu.dynamicproblems.algorithm;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.util.LogUtil;

public class SCDPTest {
	private static Logger log = LogUtil.getLogger(SCDPTest.class);
	@Ignore
	@Test
	public void test0() {
		List<List<Integer>> F=new ArrayList<List<Integer>>();
		List<Integer> s1=new ArrayList<Integer>();
		s1.add(1);
		s1.add(2);
		s1.add(3);
		F.add(s1);
		
		
		List<Integer> s2=new ArrayList<Integer>();
		s2.add(2);
		s2.add(4);
		F.add(s2);
		
		List<Integer> s3=new ArrayList<Integer>();
		s3.add(3);
		s3.add(4);
		
		F.add(s3);
		
		List<Integer> s4=new ArrayList<Integer>();
		s4.add(4);
		s4.add(5);
		
		F.add(s4);
		
		
		List<Integer> U=new ArrayList<Integer>();
		U.add(1);
		U.add(2);
		U.add(3);
		U.add(4);
		U.add(5);
		
		
		int r=3;
		
		SCDP ag=new SCDP(F,U,r);
		ag.computing();
		String result=ag.getResult().getString();
		
		List<List<Integer>> sc=ag.getSC();
		Assert.assertEquals(2, sc.size());
		
		log.debug(result);
		
		
		
	}
}
