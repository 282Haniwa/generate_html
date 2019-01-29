package csv2html;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

/**
* トランスレータ：CSVファイルをHTMLページへと変換するプログラム。
*/
public class Translator extends Object
{
	/**
	* CSVに由来するテーブルを記憶するフィールド。
	*/
	private Table inputTable;

	/**
	* HTMLに由来するテーブルを記憶するフィールド。
	*/
	private Table outputTable;

	/**
	* 属性リストのクラスを指定したトランスレータのコンストラクタ。
	* @param classOfAttributes 属性リストのクラス
	*/
	public Translator(Class<? extends Attributes> classOfAttributes)
	{
		super();

		Attributes.flushBaseDirectory();

		try
		{
			Constructor<? extends Attributes> aConstructor = classOfAttributes.getConstructor(String.class);

			this.inputTable = new Table(aConstructor.newInstance("input"));
			this.outputTable = new Table(aConstructor.newInstance("output"));
		}
		catch (NoSuchMethodException |
		InstantiationException |
		IllegalAccessException |
		InvocationTargetException anException) { anException.printStackTrace(); }

		return;
	}

	/**
	* 在位日数を計算して、それを文字列にして応答する。
	* @param periodString 在位期間の文字列
	* @return 在位日数の文字列
	*/
	public String computeNumberOfDays(String periodString)
	{
		String[] anArray = periodString.split("[0-9]:", 0);
		HashSet<String> aHashSet = new HashSet<String>();
		boolean judge = true;
		StringBuilder aBuilder = new StringBuilder();
		aBuilder.append(":");

		for(String aString : anArray)
		{
			if(!aString.isEmpty()) { aHashSet.add(aString); }
		}
		for(String aString : aHashSet)
		{
			if(judge) { judge = false;}
			else { aBuilder.append("/"); }
			aBuilder.append(aString);
		}
		String performString = aBuilder.toString();
		anArray = periodString.split(performString,0);
		List<String> aList = new ArrayList<String>();
		for(String aString : aList)
		{
			if(!aString.isEmpty()) { aList.add(aString); }
		}
		Calendar aCalendar = Calendar.getInstance();
		int calendarYear = Integer.parseInt(aList.get(0));
		int calendarMonth = Integer.parseInt(aList.get(1))-1;
		int calendarDay = Integer.parseInt(aList.get(2));
		aCalendar.set(calendarYear,calendarMonth,calendarDay);
		long previous = aCalendar.getTime().getTime();

		aCalendar = Calendar.getInstance();
		if(aList.size() > 3)
		{
			calendarYear = Integer.valueOf(aList.get(3));
			calendarMonth = Integer.valueOf(aList.get(4)) -1;
			calendarDay = Integer.valueOf(aList.get(5));
		}
		long rear = aCalendar.getTime().getTime();
		long dayTime = ((rear - previous) / (1000*60*60*24)) + 1;
		String days = String.format("%1$d" ,dayTime);
		return days;
	}

	/**
	* サムネイル画像から画像へ飛ぶためのHTML文字列を作成して、それを応答する。
	* @param aString 画像の文字列
	* @param aTuple タプル
	* @param no 番号
	* @return サムネイル画像から画像へ飛ぶためのHTML文字列
	*/
	public String computeStringOfImage(String aString, Tuple aTuple, int no)
	{
	  BufferedImage image = this.inputTable.thumbnails().get(no);
		List<String> anyStrings = aTuple.values();
		String index = anyStrings.get(aTuple.attributes().indexOfNo());
		String imageString = anyStrings.get(aTuple.attributes().indexOfImage());
		String thumnailString = anyStrings.get(aTuple.attributes().indexOfThumbnail());
		StringBuilder aBuilder = new StringBuilder();
		aBuilder.append("<a name=\"");
		aBuilder.append(thumnailString);
		aBuilder.append("\"href=\"");
		aBuilder.append(aString);
		aBuilder.append("\">");
		aBuilder.append("<img class=\"borderless\" src=\"");
		aBuilder.append(thumnailString);
		aBuilder.append("\" width=\"");
		aBuilder.append(image.getWidth());
		aBuilder.append("\" height=\"");
		aBuilder.append(image.getHeight());
		aBuilder.append("\" alt=\"");
		aBuilder.append(index);
		aBuilder.append("\"></a>");

		return aBuilder.toString();
	}

	/**
	* CSVファイルをHTMLページへ変換する。
	*/
	public void execute()
	{
		// 必要な情報をダウンロードする。
		Downloader aDownloader = new Downloader(this.inputTable);
		aDownloader.perform();

		// CSVに由来するテーブルをHTMLに由来するテーブルへと変換する。
		System.out.println(this.inputTable);
		this.translate();
		System.out.println(this.outputTable);

		// HTMLに由来するテーブルから書き出す。
		Writer aWriter = new Writer(this.outputTable);
		aWriter.perform();

		// ブラウザを立ち上げて閲覧する。
		try
		{
			Attributes attributes = this.outputTable.attributes();
			String fileStringOfHTML = attributes.baseDirectory() + attributes.indexHTML();
			ProcessBuilder aProcessBuilder = new ProcessBuilder("open", "-a", "Safari", fileStringOfHTML);
			aProcessBuilder.start();
		}
		catch (Exception anException) { anException.printStackTrace(); }

		return;
	}

	/**
	* 属性リストのクラスを受け取って、CSVファイルをHTMLページへと変換するクラスメソッド。
	* @param classOfAttributes 属性リストのクラス
	*/
	public static void perform(Class<? extends Attributes> classOfAttributes)
	{
		// トランスレータのインスタンスを生成する。
		Translator aTranslator = new Translator(classOfAttributes);
		// トランスレータにCSVファイルをHTMLページへ変換するように依頼する。
		aTranslator.execute();

		return;
	}

	/**
	* CSVファイルを基にしたテーブルから、HTMLページを基にするテーブルに変換する。
	*/
	public void translate()
	{
		int index = 0;
		int no = 0;
		int indexInput = this.inputTable.attributes().indexOfThumbnail();
		List<String> listCollection = new ArrayList<String>();
		for(String aString : this.inputTable.attributes().names())
		{
			if(index != indexInput)
			{
				listCollection.add(IO.htmlCanonicalString(aString));
				if(index == this.inputTable.attributes().indexOfPeriod()){ listCollection.add(IO.htmlCanonicalString("在位日数"));	}
			}
			index++;
		}
		this.outputTable.attributes().names(listCollection);

		for(Tuple aTuple : this.inputTable.tuples())
		{
			index = 0;
			List<String> aCollection = new ArrayList<String>();
			for(String aString : aTuple.values())
			{
				if(index != indexInput)
				{
					if(index == this.inputTable.attributes().indexOfImage()) { aCollection.add(this.computeStringOfImage(aString,aTuple,no)); }
					else { aCollection.add(IO.htmlCanonicalString(aString)); }
					if(index == this.inputTable.attributes().indexOfPeriod()) { aCollection.add(this.computeNumberOfDays(aString)); }
				}
				index++;
			}
			Tuple outTuple = new Tuple(this.outputTable.attributes(), aCollection);
			this.outputTable.add(outTuple);
			no++;
		}
		return;
	}
}
