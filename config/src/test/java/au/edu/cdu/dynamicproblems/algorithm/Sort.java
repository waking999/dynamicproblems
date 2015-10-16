package au.edu.cdu.dynamicproblems.algorithm;

import java.util.*;

public class Sort {

    static class ValueComparator implements Comparator<Integer> {

        Map<Integer, Integer> base;

        ValueComparator(Map<Integer, Integer> base) {
            this.base = base;
        }

        @Override
        public int compare(Integer a, Integer b) {
            if (a.equals(b)) return 0;
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else return 1;
        }
    }

    public static void main(String[] args) {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        ValueComparator vc = new ValueComparator(map);
        TreeMap<Integer, Integer> sorted = new TreeMap<Integer, Integer>(vc);
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 2);
        sorted.putAll(map);
//        for (Integer key : sorted.keySet()) {
//            System.out.println(key + " : " + sorted.get(key)); // why null values here?
//        }
        Integer u=new Integer(1);
        System.out.println(sorted.get(u));
        
        //System.out.println(sorted.values()); // But we do have non-null values here!
    }
}