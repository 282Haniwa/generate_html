#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os
import shutil
import urllib
import .attributes as attributes
import re

from io import IO
from reader import Reader
from table import Table
from urllib2 import Request


class Downloader(IO):
	"""ダウンローダ：CSVファイル・画像ファイル・サムネイル画像ファイルをダウンロードする。"""

	def __init__(self, input_table):
		"""ダウンローダのコンストラクタ。"""

		super(Downloader, self).__init__(input_table)

		return

	def download_csv(self):
		"""情報を記したCSVファイルをダウンロードする。"""
		urllib.urlretrieve(self.attributes().csv_url(), self.attributes().csv_filename())
		reader = Reader(self.table())
		reader.perform()
		return


	def download_images(self, image_filenames):
		"""画像ファイル群または縮小画像ファイル群をダウンロードする。"""
		for image_filename in image_filenames:
			photo_url = '{}/{}'.format(attributes.csv_url(), photo_name)
			urllib.request.urlretrieve(photo_url, image_filename)
		return

	def perform(self):
		"""すべて（情報を記したCSVファイル・画像ファイル群・縮小画像ファイル群）をダウンロードする。"""
		image_filenames = reader.perform()
		self.download_csv()
		self.download_images()

		return
