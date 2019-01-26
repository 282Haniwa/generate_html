package csv2html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import utility.StringUtility;

/**
* ライタ：情報のテーブルをHTMLページとして書き出す。
*/
public class Writer extends IO
{
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

			this.writeHeaderOn(aWriter);
			this.writeTableBodyOn(aWriter);
			this.writeFooterOn(aWriter);

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
			aWriter.write("<tr>\n");

			for(String writeString : this.attributes().names())
			{
				aWriter.write("<td class = \"center-pink\"><strong>");
				aWriter.write(writeString);
				aWriter.write("</strong></td>\n");
			}
			aWriter.write("</tr>\n");
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
			aWriter.write("\t\t\t\t\t<tbody>\n\t\t\t\t</table>\n\t\t\t</td>\n\t\t</tr>\n\t\t</tr>\n\t</tbody>\n</table>\n<hr>\n<div class=\"right-small\">Created by " + aString + "</div>\n</body>\n</html>\n");
		}
		catch(IOException anException) { anException.printStackTrace(); }
		return;
	}

	/**
	* ヘッダを書き出す。
	* @param aWriter ライタ
	*/
	public void writeHeaderOn(BufferedWriter aWriter)
	{
		try
		{
			aWriter.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n<html lang=\"ja\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<meta http-equiv=\"Content-Style-Type\" content=\"text/css\">\n<meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\">\n<meta name=\"keywords\" content=\"Smalltalk,Oriented,Programming\">\n<meta name=\"description\" content=\"Prime Ministers\">");
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
		this.writeAttributesOn(aWriter);
		this.writeTuplesOn(aWriter);

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
				aWriter.write("<tr>\n");

				for(String aString : aTuple.values() )
				{
					if( number % 2 == 0) {aWriter.write("<td class=\"center-blue\">"); }
					else { aWriter.write("<td class=\"center-yellow\">"); }
					aWriter.write(aString);
					aWriter.write("</td>\n");
				}
				aWriter.write("</tr>\n");
				number++;
			}
		}
		catch (IOException anException) { anException.printStackTrace(); }

		return;
	}
}
