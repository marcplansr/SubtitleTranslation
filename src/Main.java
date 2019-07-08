
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import com.rmtheis.yandtran.language.Language;
import com.rmtheis.yandtran.translate.Translate;

public class Main {

	public static void main(String[] args) throws Exception {

		Translate.setKey("trnsl.1.1.20190708T115247Z.8c8ac38f3f4493a3.e45b97bb3885a9f777b8ebcad8d0b081e10f1e10");

		ArrayList<String> listOfFiles = getFiles();
		for (int i = 0; i < listOfFiles.size(); ++i) {
			ArrayList<MyStringLine> stringArray = getSubsStrings("res/" + listOfFiles.get(i) + "_en.srt");

			String tranlatedString = translateSubs(stringArray);
			String outputSRT = mergeSubs(stringArray, tranlatedString);
			exportToSRT(outputSRT, "res/" + listOfFiles.get(i) + "_es.srt");
			System.out.println("File: " + "res/" + listOfFiles.get(i) + "_es.srt ok!");

		}
	}

	/**
	 * @return
	 */
	public static ArrayList<String> getFiles() {
		File folder = new File("res/");
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> output = new ArrayList<String>();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				String fileName = file.getName();
				fileName = fileName.substring(0, fileName.length() - 7);
				output.add(fileName);
			}
		}
		return output;
	}

	/**
	 * @param subsArray
	 * @return
	 */
	public static String getFullString(ArrayList<MySubtitle> subsArray) {
		String output = "";
		for (int i = 0; i < subsArray.size(); ++i) {
			output += subsArray.get(i).getSubsString();
		}
		return output;
	}

	/**
	 * writes given string to srt file
	 * 
	 * @param exportString
	 * @throws FileNotFoundException
	 */
	public static void exportToSRT(String exportString) throws FileNotFoundException {
		PrintWriter out = new PrintWriter("subs_out.srt");
		out.print(exportString);
		out.close();
	}

	/**
	 * writes given string to srt file
	 * 
	 * @param exportString
	 * @throws FileNotFoundException
	 */
	public static void exportToSRT(String exportString, String filename) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filename);
		out.print(exportString);
		out.close();
	}

	/**
	 * merges lines not to be translated from original str string lines with
	 * translated subtitles to form new translated str string
	 * 
	 * @param stringArray
	 * @param tranlatedString
	 * @return
	 * @throws IOException
	 */
	public static String mergeSubs(ArrayList<MyStringLine> stringArray, String tranlatedString) throws IOException {

		String output = "";
		BufferedReader reader = new BufferedReader(new StringReader(tranlatedString));
		for (int i = 0; i < stringArray.size(); ++i) {
			MyStringLine currentLine = stringArray.get(i);

			if (currentLine.isTranslate()) {
				output += reader.readLine() + "\n";
			} else {
				output += stringArray.get(i).getStringLine() + "\n";
			}
		}
		output = output.substring(0, output.length() - 1);
		reader.close();
		return output;
	}

	/**
	 * @param ArrayList<MyStringLine> stringArray
	 * @return String
	 * @throws Exception
	 */
	public static String translateSubs(ArrayList<MyStringLine> stringArray) throws Exception {

		String translateIn = "";
		String translateOut = "";
		int byteLengthCounter = 0;
		MyStringLine currentLine;

		for (int i = 0; i < stringArray.size(); ++i) {
			currentLine = stringArray.get(i);
			if (currentLine.isTranslate()) {
				if (byteLengthCounter + currentLine.getStringByteLength() < 10240) {
					translateIn += currentLine.getStringLine() + "\n";
					byteLengthCounter += currentLine.getStringByteLength() + 1;
				} else {
					translateOut += translateString(translateIn) + "\n";
					translateIn = currentLine.getStringLine() + "\n";
					byteLengthCounter = currentLine.getStringByteLength() + 1;
				}
			}

		}
		translateOut += translateString(translateIn);
//    	return translateOut.substring(0, translateOut.length() - 1);
		return translateOut;
	}

	/**
	 * Extracts strings from srt subtitles file and stores them in array of
	 * MyStringLine data type
	 * 
	 * @param fileName
	 * @return ArrayList<MyStringLine>
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static ArrayList<MyStringLine> getSubsStrings(String fileName)
			throws FileNotFoundException, UnsupportedEncodingException {

		File file = new File(fileName);
		Scanner input = new Scanner(file);
		String newLine;

		ArrayList<MyStringLine> output = new ArrayList<MyStringLine>();
		output.add(new MyStringLine(input.nextLine()));
		output.add(new MyStringLine(input.nextLine()));

		while (input.hasNextLine()) {
			newLine = input.nextLine();
			if (newLine.length() == 0) {
				output.add(new MyStringLine(newLine));
				output.add(new MyStringLine(input.nextLine()));
				output.add(new MyStringLine(input.nextLine()));
			} else {
				output.add(new MyStringLine(true, newLine));
			}
		}
		input.close();
		return output;
	}

	/**
	 * Extracts strings from srt subtitles file and stores them in array of
	 * MyStringLine data type
	 * 
	 * @param fileName
	 * @return ArrayList<MyStringLine>
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static ArrayList<MySubtitle> getSubtitles(String fileName)
			throws FileNotFoundException, UnsupportedEncodingException {

		File file = new File(fileName);
		Scanner input = new Scanner(file);
		@SuppressWarnings("unused")
		String newLine;

		ArrayList<MySubtitle> output = new ArrayList<MySubtitle>();

		while (input.hasNextLine()) {
			input.nextLine();
			boolean addSub = true;
			String currentTime = input.nextLine();
			ArrayList<String> currentArray = new ArrayList<String>();

			while (input.hasNextLine() && addSub) {
				String currentLine = input.nextLine();
				if (currentLine.length() > 0) {
					currentArray.add(currentLine);
				} else {
					addSub = false;
				}
			}
			MySubtitle currentSubtitle = new MySubtitle(currentTime, currentArray);
			output.add(currentSubtitle);
		}
		input.close();
		return output;
	}

	/**
	 * @param toTranslate
	 * @return
	 * @throws Exception
	 */
	public static String translateString(String toTranslate) throws Exception {
		String translatedText = Translate.execute(toTranslate, Language.ENGLISH, Language.SPANISH);
		return translatedText;
	}

}
