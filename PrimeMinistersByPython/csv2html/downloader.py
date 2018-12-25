#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os
import shutil
import urllib
import .attributes as attributes

from io import IO
from reader import Reader
from table import Table


class Downloader(IO):
	"""ダウンローダ：CSVファイル・画像ファイル・サムネイル画像ファイルをダウンロードする。"""

	def __init__(self, input_table):
		"""ダウンローダのコンストラクタ。"""

		super(Downloader, self).__init__(input_table)

		return

	def download_csv(self):
		"""情報を記したCSVファイルをダウンロードする。"""
		self.PrimeMinisterCSV = "{}".format(attributes.AttributesForPrimeMinisters.csv_url())
		self.TokugawaShogunateCSV = "{}".format(attributes.AttributesForTokugawaShogunate.csv_url())
		return


	def download_images(self, image_filenames):
		"""画像ファイル群または縮小画像ファイル群をダウンロードする。"""

		'''
		TODO:(NOBU) PrimeMinisterとTokugawaShogunateファイルの二つが必要??
		'''

		if os.path.isdir("./images"):
			shutil.rmtree("./images")
		os.makedirs("./images")

		if os.path.isdir("./thumbnails"):
			shutil.rmtree("./thumbnails")
		os.makedirs("./thumbnails")

		for num, image_filename enumerate(image_filenames,39):
			name = "./images/{:01}_{}.jpg".format(num)
			image_url = '{}/{}'.format(image_filenames, name)
			urllib.request.urlretrieve(image_url, name)



		

		return

	def perform(self):
		"""すべて（情報を記したCSVファイル・画像ファイル群・縮小画像ファイル群）をダウンロードする。"""
		image_filenames = reader.perform()
		self.download_csv()
		self.download_images()

		return
