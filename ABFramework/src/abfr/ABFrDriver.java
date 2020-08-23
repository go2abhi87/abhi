package abfr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.openqa.selenium.WebDriver;

public class ABFrDriver {

	public static WebDriver driver = null;
	public static BufferedReader br = null;
	public static HashMap<Integer, HashMap<Integer, String>> hmAll = null;
	public static HashMap<Integer, String> hm = null;
	public static String[] sectionNames = null;
	public static StringTokenizer sToken = null;
	public static HashMap<Integer, String> sTokenHm = null;
	public static HashMap<Integer, String> tcNameshm = null;
	public static HashMap<Integer, String> tsNameshm = null;
	public static HashMap<Integer, String> keywordhm = null;
	public static HashMap<Integer, String> keywordHirarchyhm = null;
	public static boolean lastKeyword = true;
	public static int hMcounter = 1;
	public static int hirarchyNumber = 1;
	public static String tsName = null;

	public static void main(String args[]) {
		{
			hmAll = new HashMap<Integer, HashMap<Integer, String>>();
			readAndAddToHashMap("D:\\ABFramework\\TEST.txt");
			readAndAddToHashMap("D:\\ABFramework\\Script\\TEST_INNER.txt");
			readAndAddToHashMap("D:\\ABFramework\\Script\\Execution\\TEST_INNER_MOST.txt");

			for (int i = 1; i <= getTestCaseList().size(); i++) {
				// System.out.println("TEST CASE NAME = " + getTestCaseList().get(i).trim());
				for (int j = 1; j <= getTestStepList(getTestCaseList().get(i)).size(); j++) {
					// System.out.println("TEST STEP = " +
					// getTestStepList(getTestCaseList().get(i)).get(j));

					for (int k = 1; k <= getAllSubKeywordsUnderAKeyword(
							getTestStepList(getTestCaseList().get(i)).get(j), hmAll).size(); k++) {
						// System.out.println(getAllSubKeywordsUnderAKeyword(getTestStepList(getTestCaseList().get(i)).get(j),
						// hmAll).get(k));
					}
				}
			}

			/*
			 * for (int i = 1; i <=
			 * getAllSubKeywordsInAKeyword("Open the Browser with Quikr URL", hmAll).size();
			 * i++) { System.out.println(
			 * getAllSubKeywordsInAKeyword("Open the Browser with Quikr URL",
			 * hmAll).get(i)); }
			 * 
			 * for (int i = 2; i <= getAllSubKeywordsOfAKeyword("Click On Sel",
			 * hmAll).size(); i++) {
			 * System.out.println(getAllSubKeywordsOfAKeyword("Click On Sel",
			 * hmAll).get(i)); }
			 */
			keywordHirarchyhm = new HashMap<Integer, String>();
			for (int i = 1; i <= getKeywordHirarchyOfAKeyword("Open the Browser with Quikr URL", hmAll).size(); i++) {
				System.out.println(getKeywordHirarchyOfAKeyword("Open the Browser with Quikr URL", hmAll).get(i));
			}
			System.out.println(getKeywordHirarchyOfAKeyword("Open the Browser with Quikr URL", hmAll).size());

		}
	}

	public static HashMap<Integer, String> getKeywordHirarchyOfAKeyword(String keywordName,
			HashMap<Integer, HashMap<Integer, String>> hmAll) {
		System.out.println("Length Of searched key = " +getAllSubKeywordsUnderAKeyword(keywordName, hmAll).size());
		if (getAllSubKeywordsUnderAKeyword(keywordName, hmAll).size() != 0) {
			System.out.println(getAllSubKeywordsUnderAKeyword(keywordName, hmAll).get(1));
			keywordHirarchyhm.put(hirarchyNumber + 1, getAllSubKeywordsUnderAKeyword(keywordName, hmAll).get(1));
			hirarchyNumber = hirarchyNumber + 1;
			System.out.println(keywordHirarchyhm.size());

			getKeywordHirarchyOfAKeyword(keywordHirarchyhm.get(hirarchyNumber), hmAll);
		}

		return keywordHirarchyhm;
	}

	public static HashMap<Integer, String> getAllSubKeywordsUnderAKeyword(String keywordName,
			HashMap<Integer, HashMap<Integer, String>> hmAll) {
		keywordhm = new HashMap<Integer, String>();
		outerloop: for (int i = 1; i <= hmAll.size(); i++) {
			for (int j = 1; j <= hmAll.get(i).size(); j++) {
				if (hmAll.get(i).get(j).trim().equals(keywordName.trim()) && !hmAll.get(i).get(j).startsWith("\t")) {
					int keywordNumber = 1;
					for (int k = getTestCaseStartAndEndLineNumbers(keywordName.trim(), hmAll.get(i))[0]
							+ 1; k <= getTestCaseStartAndEndLineNumbers(keywordName.trim(), hmAll.get(i))[1]; k++) {
						keywordhm.put(keywordNumber, hmAll.get(i).get(k));
						keywordNumber++;
					}
					break outerloop;
				}
			}
		}
		return keywordhm;
	}

	public static String searchForKeywordDefinition(String keywordName) {
		outerloop: for (int i = 1; i <= hmAll.size(); i++) {
			for (int j = 1; j <= hmAll.get(i).size(); j++) {
				if (hmAll.get(i).get(j).trim().equals(keywordName.trim()) && hmAll.get(i).get(j).startsWith("\t")) {
					System.out.println(hmAll.get(i).get(j).trim());
					keywordName = hmAll.get(i).get(j).trim();
					for (int k = getTestCaseStartAndEndLineNumbers(keywordName,
							hmAll.get(i))[0]; k <= getTestCaseStartAndEndLineNumbers(keywordName,
									hmAll.get(i))[1]; k++) {
						System.out.println(hmAll.get(i).get(k).trim());

					}

					lastKeyword = false;
					break outerloop;
				}
			}
		}

		return keywordName;
	}

	/*
	 * THIS METHOD WILL RETURN LIST OF TEST STEPS PRESENT IN A TEST CASE IN FIRST
	 * TEST CASE FILE ONLY.
	 */

	public static HashMap<Integer, String> getTestStepList(String testCaseName) {
		if (getTeststepNames_In_A_HashMap(testCaseName, hmAll.get(1)).isEmpty()) {
			System.out.println("NO TEST STEPS DEFINED UNDER THIS TEST CASE");
			return null;
		} else {
			return getTeststepNames_In_A_HashMap(testCaseName, hmAll.get(1));
		}
	}

	/*
	 * THIS METHOD WILL RETURN LIST OF TEST CASES TO BE EXECUTED. TEST CASES CAN BE
	 * DEFINED ONLY IN FIRST TEXT FILE AND SO IT WILL SEARCH TEST CASES ONLY IN THE
	 * FIRST TEXT FILE
	 */

	public static HashMap<Integer, String> getTestCaseList() {
		if (getTestCaseNames_In_A_HashMap("*****TEST CASES*****", hmAll.get(1)).isEmpty()) {
			System.out.println("TEST CASE SECTION NOT PRESENT IN TEST FILE");
			return null;
		} else {
			return getTestCaseNames_In_A_HashMap("*****TEST CASES*****", hmAll.get(1));
		}
	}

	public static void readAndAddToHashMap(String location) {
		hmAll.put(hMcounter, new HashMap<Integer, String>());
		try {
			String sCurrentLine;
			int lineNumber = 1;
			br = new BufferedReader(new FileReader(location));
			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.trim().isEmpty()) {
					hmAll.get(hMcounter).put(lineNumber, sCurrentLine);
					lineNumber++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		hMcounter++;
	}

	/*
	 * THIS METHOD RETURNS START LINE AND END LINE OF A GIVEN SECTION RETURN TYPE IS
	 * ARRAY OF INTEGER WHERE 0TH ELEMENT WILL BE START LINE AND 1ST ELEMENT WILL BE
	 * END LINE
	 */

	public static int[] getSetionStartAndEndLineNumbers(String sectionName, HashMap<Integer, String> hm) {
		int ret[] = new int[2];

		for (int i = 1; i <= hm.size(); i++) {
			if (hm.get(i).trim().equals(sectionName.trim())) { // SEARCHING GIVEN SECTION NAME IN EACH LINE. ONCE FOUND,
																// FOR LOOP WILL
				// BE TERMINATED AND ITERATE VALUE WILL BE ASSIGNED TO START LINE
				// NUMBER.
				ret[0] = i;
				break;
			}
		}
		for (int i = ret[0] + 1; i <= hm.size(); i++) { // "*****" TEXT IN EACH LINE AFTER START LINE NUMBER. ONCE
														// FOUND, FOR LOOP WILL BE TERMINATED AND ITERATE VALUE WILL BE
														// ASSIGNED TO END LINE NUMBER.
			if (hm.get(i).trim().contains("*****")) {
				ret[1] = i - 1;
				break;
			}
		}
		if (ret[0] > 0 && ret[1] == 0) { // IF END LINE NUMBER COULD NOT FOUND USING ABOVE LOOP. THEN ASSIGNING LAST
											// LINE NUMBER TO END LINE.
			ret[1] = hm.size();
		}
		return ret;
	}

	/*
	 * THIS METHOD RETURNS START LINE AND END LINE OF A GIVEN TEST CASE, RETURN TYPE
	 * IS ARRAY OF INTEGER WHERE 0TH ELEMENT WILL BE START LINE AND 1ST ELEMENT WILL
	 * BE END LINE
	 */

	public static int[] getTestCaseStartAndEndLineNumbers(String tcName, HashMap<Integer, String> hm) {
		int ret[] = new int[2];

		for (int i = 1; i <= hm.size(); i++) {
			if (hm.get(i).trim().equals(tcName.trim()) && !hm.get(i).startsWith("\t")) {
				while (hm.get(i).startsWith(" ")) {
					hm.put(i, hm.get(i).substring(1));
				}
				ret[0] = i;
				break;
			}
		}
		for (int i = ret[0] + 1; i <= hm.size(); i++) {
			if (!hm.get(i).startsWith("\t")) {
				ret[1] = i - 1;
				break;
			}
		}
		if (ret[0] > 0 && ret[1] == 0) { // IF END LINE NUMBER COULD NOT FOUND USING ABOVE LOOP. THEN ASSIGNING LAST
			// LINE NUMBER TO END LINE.
			ret[1] = hm.size();
		}
		// System.out.println("START LINE NUMBER = " + ret[0]);
		// System.out.println("END LINE NUMBER = " + ret[1]);
		return ret;
	}

	/*
	 * THIS METHOS TAKES SECTION NAME AS PARAMETER AND RETURNS ALL THE TEST CASES
	 * PRESENT IN A HASH MAP STARTING FROM 1.
	 */

	public static HashMap<Integer, String> getTestCaseNames_In_A_HashMap(String sectionName,
			HashMap<Integer, String> hm) {
		tcNameshm = new HashMap<Integer, String>();
		int tcNumber = 1;
		for (int i = getSetionStartAndEndLineNumbers(sectionName, hm)[0]
				+ 1; i <= getSetionStartAndEndLineNumbers(sectionName, hm)[1]; i++) {
			while (hm.get(i).startsWith(" ")) {
				hm.put(i, hm.get(i).substring(1));
			}
			if (!hm.get(i).startsWith("\t")) {
				// System.out.println(line.trim());
				hm.put(i, hm.get(i));
				tcNameshm.put(tcNumber, hm.get(i));
				tcNumber++;
			}

		}
		// System.out.println("TC NUMBER = " + tcNames.size());
		return tcNameshm;
	}

	/*
	 * THIS METHOS TAKES TEST CASE NAME AS PARAMETER AND RETURNS ALL THE TEST STEPS
	 * PRESENT FOR A GIVEN TEST CASE IN A HASH MAP STARTING FROM 1.
	 */

	public static HashMap<Integer, String> getTeststepNames_In_A_HashMap(String tcName, HashMap<Integer, String> hm) {
		tsNameshm = new HashMap<Integer, String>();
		int tsNumber = 1;
		for (int i = getTestCaseStartAndEndLineNumbers(tcName, hm)[0]
				+ 1; i <= getTestCaseStartAndEndLineNumbers(tcName, hm)[1]; i++) {
			tsNameshm.put(tsNumber, hm.get(i));
			tsNumber++;
		}
		// System.out.println("TS NUMBER = " + tsNames.size());
		return tsNameshm;
	}
}
