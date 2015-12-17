package au.edu.cdu.dynamicproblems;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestUtil {
	public static String getOutputFileName(String datasetName,String className) {
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String destDir = "out/"+datasetName+"-" + className;

		String destFile = destDir + "-" + timeStamp + ".csv";
		return destFile;
	}
}
