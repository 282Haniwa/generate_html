package csv2html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Calendar;
import utility.StringUtility;

/**
* ライタ：情報のテーブルをHTMLページとして書き出す。
*/
public class Writer extends IO
{
	/**
	 * ファイルにhtmlを書き込む際のインデントの大きさ
	 */
	private static Integer indent = 0;

	/**
	* ライタのコンストラクタ。
	* @param aTable テーブル
	*/
	public Writer(Table aTable)
	{
		super(aTable);

		return;
	}

	/**
	* HTMLページを基にするテーブルからインデックスファイル(index.html)に書き出す。
	*/
	public void perform()
	{
		try
		{
			Attributes attributes = this.attributes();
			String fileStringOfHTML = attributes.baseDirectory() + attributes.indexHTML();
			File aFile = new File(fileStringOfHTML);
			FileOutputStream outputStream = new FileOutputStream(aFile);
			OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream, StringUtility.encodingSymbol());
			BufferedWriter aWriter = new BufferedWriter(outputWriter);

			aWriter.write("<html lang=\"ja\">\n");
			this.writeHeadOn(aWriter);
			aWriter.write("<body>\n");
			this.writeHeaderOn(aWriter);
			this.writeTableBodyOn(aWriter);
			this.writeFooterOn(aWriter);
			aWriter.write("</body>\n");
			aWriter.write("</html>\n");

			aWriter.close();
		}
		catch (UnsupportedEncodingException | FileNotFoundException anException) { anException.printStackTrace(); }
		catch (IOException anException) { anException.printStackTrace(); }

		return;
	}

	/**
	* 属性リストを書き出す。
	* @param aWriter ライタ
	*/
	public void writeAttributesOn(BufferedWriter aWriter)
	{
		try
		{
			aWriter.write(Writer.withIndent("<tr>\n"));
			Writer.indent++;

			for(String writeString : this.attributes().names())
			{
				aWriter.write(Writer.withIndent("<td class=\"center-pink\"><strong>"));
				aWriter.write(writeString);
				aWriter.write("</strong></td>\n");
			}
			Writer.indent--;
			aWriter.write(Writer.withIndent("</tr>\n"));
		}
		catch(IOException anException) { anException.printStackTrace(); }
		return;
	}

	/**
	* フッタを書き出す。
	* @param aWriter ライタ
	*/
	public void writeFooterOn(BufferedWriter aWriter)
	{
		StringBuilder aBuilder = new StringBuilder();
		Calendar writeCalendar = Calendar.getInstance();
		int second = writeCalendar.get(Calendar.SECOND);
		int minute = writeCalendar.get(Calendar.MINUTE);
		int hour = writeCalendar.get(Calendar.HOUR_OF_DAY);
		int day = writeCalendar.get(Calendar.DATE);
		int month = writeCalendar.get(Calendar.MONTH) + 1;
		int year = writeCalendar.get(Calendar.YEAR);

		aBuilder.append("On");
		aBuilder.append(String.format("%1$04d/" ,year));
		aBuilder.append("/");
		aBuilder.append(String.format("%1$02d/",month));
		aBuilder.append("/");
		aBuilder.append(String.format("%1$02d",day));

		aBuilder = new StringBuilder();
		aBuilder.append("At :");
		aBuilder.append(String.format("%1$02d",hour));
		aBuilder.append(".");
		aBuilder.append(String.format("%1$02d",minute));
		aBuilder.append(".");
		aBuilder.append(String.format("%1$02d",second));
		String aString = aBuilder.toString();
		try
		{
			aWriter.write("<hr>\n<div class=\"right-small\">Created by " + aString + "</div>\n");
		}
		catch(IOException anException) { anException.printStackTrace(); }
		return;
	}

	/**
	* htmlヘッダを書き出す。
	* @param aWriter ライタ
	*/
	public void writeHeadOn(BufferedWriter aWriter)
	{
		try
		{
			List<String> cssRows = IO.readTextFromFile("assets/style.css");
			aWriter.write("<head>\n");
			aWriter.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
			aWriter.write("<meta name=\"author\" content=\"Hukurou 9th\">\n");
			aWriter.write("<style type=\"text/css\">\n");
			cssRows.forEach((row) -> {
				try
				{
					aWriter.write(row);
					aWriter.newLine();
				}
				catch (IOException anException) { anException.printStackTrace(); }
			});
			aWriter.write("</style>\n");
			aWriter.write("<title>");
			aWriter.write(IO.htmlCanonicalString(this.attributes().titleString()));
			aWriter.write("</title>\n");
			aWriter.write("</head>\n");
		}
		catch (IOException anException) { anException.printStackTrace(); }

		return;
	}

	/**
	* htmlヘッダを書き出す。
	* @param aWriter ライタ
	*/
	public void writeHeaderOn(BufferedWriter aWriter)
	{
		try
		{
			aWriter.write("<div class=\"belt\">\n");
			aWriter.write("\t<h2>");
			aWriter.write(this.attributes().captionString());
			aWriter.write("</h2>\n");
			aWriter.write("</div>\n");
		}
		catch (IOException anException) { anException.printStackTrace(); }

		return;
	}

	/**
	* ボディを書き出す。
	* @param aWriter ライタ
	*/
	public void writeTableBodyOn(BufferedWriter aWriter)
	{
		try
		{
			aWriter.write(Writer.withIndent("<table class\"content\" summary=\"table\">\n"));
			Writer.indent++;
			aWriter.write(Writer.withIndent("<thead>\n"));
			Writer.indent++;
			this.writeAttributesOn(aWriter);
			Writer.indent--;
			aWriter.write(Writer.withIndent("</thead>\n"));
			aWriter.write(Writer.withIndent("<tbody>\n"));
			Writer.indent++;
			this.writeTuplesOn(aWriter);
			Writer.indent--;
			aWriter.write(Writer.withIndent("</tbody>\n"));
			Writer.indent--;
			aWriter.write(Writer.withIndent("</table>\n"));
		}
		catch (IOException anException) { anException.printStackTrace(); }

		return;
	}

	/**
	* タプル群を書き出す。
	* @param aWriter ライタ
	*/
	public void writeTuplesOn(BufferedWriter aWriter)
	{
		int number = 0;
		try
		{
			for(Tuple aTuple : this.tuples())
			{
				aWriter.write(Writer.withIndent("<tr>\n"));
				Writer.indent++;

				for(String aString : aTuple.values() )
				{
					if( number % 2 == 0) {aWriter.write(Writer.withIndent("<td class=\"center-blue\">")); }
					else { aWriter.write(Writer.withIndent("<td class=\"center-yellow\">")); }
					aWriter.write(aString);
					aWriter.write(Writer.withIndent("</td>\n"));
				}
				Writer.indent--;
				aWriter.write(Writer.withIndent("</tr>\n"));
				number++;
			}
		}
		catch (IOException anException) { anException.printStackTrace(); }

		return;
	}

	public static String withIndent(String aString) {
		StringBuilder aBuilder = new StringBuilder();
		for (Integer index = 0; index < Writer.indent; index++)
		{
			aBuilder.append("\t");
		}
		aBuilder.append(aString);

		return aBuilder.toString(); 
	}
}
