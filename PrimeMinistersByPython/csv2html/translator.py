#!/usr/bin/env python
# -*- coding: utf-8 -*-

# import commands
import subprocess
import datetime
import locale
import os
import os.path
import re
import shutil

from PIL import Image

from .downloader import Downloader
from .table import Table
from .tuple import Tuple
from .writer import Writer
from .attributes import AttributesForPrimeMinisters
from .attributes import AttributesForTokugawaShogunate

class Translator(object):
	"""トランスレータ：CSVファイルをHTMLページへと変換するプログラム。"""

	def __init__(self, classOfAttributes):
		"""トランスレータのコンストラクタ。"""

		super(Translator, self).__init__()

		self._input_table = Table('input', classOfAttributes)
		self._output_table = Table('output', classOfAttributes)

		return

	def compute_string_of_days(self, period):
		"""在位日数を計算して、それを文字列にして応答する。"""

		period_list = re.split('〜', period)
		start = re.split('[年月日]', period_list[0])
		end = re.split('[年月日]', period_list[1])
		start_date = datetime.date(int(start[0]), int(start[1]), int(start[2]))
		if end[0] != '':
			end_date = datetime.date(int(end[0]), int(end[1]), int(end[2]))
		else:
			end_date = datetime.date.today()
		return str((end_date - start_date).days + 1)

	def compute_string_of_image(self, tuple):
		"""サムネイル画像から画像へ飛ぶためのHTML文字列を作成して、それを応答する。"""

		no_index = tuple.attributes().keys().index('no')
		image_index = tuple.attributes().keys().index('image')
		thumbnail_index = tuple.attributes().keys().index('thumbnail')
		img_html = '<a name="{no}" href="{image}"><img class="borderless" src="{thumbnail}" width="25" height="32" alt="{alt}"></a>'.format(
			no = tuple.values()[no_index],
			image = tuple.values()[image_index],
			thumbnail = tuple.values()[thumbnail_index],
			alt = tuple.values()[image_index].split('/')[1],
		)
		return img_html

	def execute(self):
		"""CSVファイルをHTMLページへと変換する。"""

		# ダウンローダに必要なファイル群をすべてダウンロードしてもらい、
		# 入力となるテーブルを獲得する。
		a_downloader = Downloader(self._input_table)
		a_downloader.perform()

		# トランスレータに入力となるテーブルを渡して変換してもらい、
		# 出力となるテーブルを獲得する。
		print(self._input_table)
		self.translate()
		print(self._output_table)

		# ライタに出力となるテーブルを渡して、
		# Webページを作成してもらう。
		a_writer = Writer(self._output_table)
		a_writer.perform()

		# 作成したページをウェブブラウザで閲覧する。
		class_attributes = self._output_table.attributes().__class__
		base_directory = class_attributes.base_directory()
		index_html = class_attributes.index_html()
		a_command = 'open -a Safari ' + base_directory + os.sep + index_html
		subprocess.getoutput(a_command)

		return

	@classmethod
	def perform(the_class, class_attributes):
		"""属性リストのクラスを受け取り、CSVファイルをHTMLページへと変換する。"""

		# トランスレータのインスタンスを生成する。
		a_translator = the_class(class_attributes)
		# トランスレータにCSVファイルをHTMLページへ変換するように依頼する。
		a_translator.execute()

		return

	def translate(self):
		"""CSVファイルを基にしたテーブルから、HTMLページを基にするテーブルに変換する。"""

		for tuple in self._input_table.tuples():
			values = []
			for attribute in self._output_table.attributes().keys():
				if attribute == "image":
					values.append(self.compute_string_of_image(tuple))
				elif attribute == "days":
					period_index_in_tuple = tuple.attributes().keys().index("period")
					values.append(self.compute_string_of_days(tuple.values()[period_index_in_tuple]))
				elif attribute in self._input_table.attributes().keys():
					attribute_index_in_tuple = tuple.attributes().keys().index(attribute)
					values.append(tuple.values()[attribute_index_in_tuple])
				else:
					values.append(None)
			self._output_table.add(Tuple(self._output_table.attributes(), values))
		return
