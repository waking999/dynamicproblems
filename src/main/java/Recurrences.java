import java.util.ArrayList;
import java.util.List;

public class Recurrences {
	public static void main(String args[]) {
		int k = 10;
		List<Integer> tList = new ArrayList<Integer>();
		List<Integer> bList = new ArrayList<Integer>();

		tList.add(1);
		tList.add(2);

		bList.add(1);

		for (int i = 1; i <= k; i++) {
			int b = 1 + bList.get(i - 1) + tList.get(i - 1);
			bList.add(b);
			if (i >= 2) {
				int t = 1 + tList.get(i - 1) + tList.get(i - 2) + bList.get(i - 1);
				tList.add(t);
			}
		}
		
		System.out.println(tList);
		System.out.println(bList);

	}

}
