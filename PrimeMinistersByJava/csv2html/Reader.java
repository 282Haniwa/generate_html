package csv2html;

import java.io.File;
import java.util.List;
import utility.StringUtility;

/**
 * リーダ：情報を記したCSVファイルを読み込んでテーブルに仕立て上げる。
 */
public class Reader extends IO
{
	/**
	 * リーダのコンストラクタ。
	 * @param aTable テーブル
	 */
	public Reader(Table aTable)
	{
		super(aTable);

		return;
	}

	/**
	 * ダウンロードしたCSVファイルを読み込む。
	 */
	public void perform()
	{
		String fileUrl = attributes().csvUrl();
		String name = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
		File textFile = new File(this.attributes().baseDirectory() + name);
		boolean judge = true;
		List<String> textCollections = IO.readTextFromFile(textFile);
		for(String aString : textCollections)
		{
			List<String> aRaw = IO.splitString(aString, ",");
			if(judge)
			{
				this.attributes().names(aRaw);
				judge = false;
			}
			else
			{
				Tuple aTuple = new Tuple(this.attributes(), aRaw);
				this.table().add(aTuple);
			}
		}

		return;
	}
}
