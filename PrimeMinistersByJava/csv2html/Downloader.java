package csv2html;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import utility.ImageUtility;

/**
 * ダウンローダ：CSVファイル・画像ファイル・サムネイル画像ファイルをダウンロードする。
 */
public class Downloader extends IO
{

	/**
	 * ダウンローダのコンストラクタ。
	 * @param aTable テーブル
	 */
	public Downloader(Table aTable)
	{
		super(aTable);
		return;
	}

	/**
	 * 総理大臣の情報を記したCSVファイルをダウンロードする。
	 */
	public void downloadCSV()
	{
		String fileUrl = this.attributes().csvUrl();
		String name = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
		List<String> textCollections = IO.readTextFromURL(fileUrl);
		File textFile = new File(this.attributes().baseDirectory() + name);
		IO.writeText(textCollections,textFile);
		System.out.println("at : " + fileUrl);
		System.out.println("to :" + textFile);

		return;
	}

	/**
	 * 総理大臣の画像群をダウンロードする。
	 */
	public void downloadImages()
	{
		int indexOfImage = this.attributes().indexOfImage();
		this.downloadPictures(indexOfImage);

		return;
	}

	/**
	 * 総理大臣の画像群またはサムネイル画像群をダウンロードする。
	 * @param indexOfPicture 画像のインデックス
	 */
	private void downloadPictures(int indexOfPicture)
	{
		for(Tuple aTuple : this.tuples() )
		{
			String pictureString = aTuple.values().get(indexOfPicture);
			String urlString = this.attributes().baseUrl() + pictureString;
			StringBuilder aBuilder = new StringBuilder();
			BufferedImage loadImage = ImageUtility.readImageFromURL(urlString);
			List<String> separatorCollection = IO.splitString(pictureString,"/");
			int indexOfLast = separatorCollection.size() -1;
			System.out.println("At :" + urlString);
			for(int number = 0; number < indexOfLast; number++)
			{
				aBuilder.append(File.separator);
				aBuilder.append(separatorCollection.get(number));
			}
			urlString = aBuilder.toString() + separatorCollection.get(indexOfLast);
			File textFile = new File(this.attributes().baseDirectory() + urlString);
			File aDirectory = new File(textFile.getParent());
			if(!aDirectory.exists()){ aDirectory.mkdirs(); }
			ImageUtility.writeImage(loadImage, textFile);
			System.out.println("To :" + textFile);
		}
		return;
	}

	/**
	 * 総理大臣の画像群をダウンロードする。
	 */
	public void downloadThumbnails()
	{
		int indexOfThumbnail = this.attributes().indexOfThumbnail();
		this.downloadPictures(indexOfThumbnail);

		return;
	}

	/**
	 * 総理大臣の情報を記したCSVファイルをダウンロードして、画像群やサムネイル画像群もダウロードする。
	 */
	public void perform()
	{
		this.downloadCSV();
		Reader tableReader = new Reader(this.table());
		tableReader.perform();
		this.downloadThumbnails();
		this.downloadImages();
		return;
	}
}
