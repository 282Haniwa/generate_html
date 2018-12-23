#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os
import shutil
import urllib
import .attributes

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
		
		PrimeMinistersURL = "http://www.cc.kyoto-su.ac.jp/~atsushi/Programs/VisualWorks/CSV2HTML/PrimeMinisters/PrimeMinisters.csv"
		TokugawaShogunateURL = "http://www.cc.kyoto-su.ac.jp/~atsushi/Programs/VisualWorks/CSV2HTML/TokugawaShogunate/TokugawaShogunate.csv"
		PrimeMinistersCSV = "PrimeMinisters.csv"
		TokugawaShogunateCSV = "TokugawaShogunate.csv"
		urllib.request.urlretrieve(PrimeMinistersURL, PrimeMinistersCSV)
		urllib.request.urlretrieve(TokugawaShogunateURL, TokugawaShogunateCSV)
		return
		
		

	def download_images(self, image_filenames):
		"""画像ファイル群または縮小画像ファイル群をダウンロードする。"""
		if os.path.isdir("./images"):
			shutil.rmtree("./images")
		os.makedirs("./images")

		if os.path.isdir("./thumbnails"):
			shutil.rmtree("./thumbnails")
		os.makedirs("./thumbnails")
		'''
		TODO: (Nobu)ここから不要？
		'''
		for num in range(39,63):
			name = "./images/0{}.jpg".format(num)
			url = "http://www.cc.kyoto-su.ac.jp/~atsushi/Programs/VisualWorks/CSV2HTML/PrimeMinisters/images/0{}.jpg".format(num)
			urllib.request.urlretrieve(url, name)

		for num in range(39,63):
			name = "./thumbnails/0{}.jpg".format(num)
			url = "http://www.cc.kyoto-sy.ac.jp/~atsushi/Programs/VisualWorks/CSV2HTML/PrimeMinisters/thumbnails/0{}.jpg".format(num)
			urllib.request.urlretrieve(url, name)

		

		return

	def perform(self):
		"""すべて（情報を記したCSVファイル・画像ファイル群・縮小画像ファイル群）をダウンロードする。"""
		self.download_csv()
		self.download_images('~Desktop/PrimeMinisters/images')

		return
