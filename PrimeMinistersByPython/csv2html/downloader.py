#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os
import shutil
import urllib
import urllib2
import attributes
import re

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
		urllib.urlretrieve(self.attributes().csv_url(), self.attributes().csv_filename())
		return


	def download_images(self, image_filenames):
		"""画像ファイル群または縮小画像ファイル群をダウンロードする。"""
		for image_filename in image_filenames:
			image_url = self.attributes().base_url() + image_filename
			image_filename = os.path.join(self.attributes().base_directory(), image_filename)
			site = urllib2.urlopen(image_url)
			with open(image_filename, "wb") as image_file:
				image_file.write(site.read())
			# image_url = '{}/{}'.format(self.attributes().base_url(), image_filename)
			# image_url = self.attributes().base_url() + image_filename
			# urllib2.urlretrieve(image_url, './images/aaaa.jpg')
		# for thumbnail_filename in image_filename:
		# 	thumbnail_url = '{}/{}'.format(self.attributes().csv_url, thumbnail_filename)
		# 	utllib.urlretrieve(thumbnail_url, thumbnail_filename)
		return

	def perform(self):
		"""すべて（情報を記したCSVファイル・画像ファイル群・縮小画像ファイル群）をダウンロードする。"""
		self.download_csv()
		reader = Reader(self.table())
		reader.perform()
		self.download_images(self.table().image_filenames()[1:])
		self.download_images(self.table().thumbnail_filenames()[1:])

		return
