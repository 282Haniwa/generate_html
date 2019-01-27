#!/usr/bin/env python
# -*- coding: utf-8 -*-

import csv
import html

class IO(object):
	"""入出力：リーダ・ダウンローダ・ライタを抽象する。"""

	def __init__(self, a_table):
		"""入出力のコンストラクタ。"""

		super(IO, self).__init__()

		self._table = a_table

		return

	def attributes(self):
		"""属性リストを応答する。"""

		return self.table().attributes()

	def read_csv(self, filename):
		"""指定されたファイルをCSVとして読み込み、行リストを応答する。"""

		with open(filename, 'rt', encoding='utf-8', newline='') as file:
			csv_reader = csv.reader(file, delimiter=',', doublequote=False, lineterminator='\n', quotechar='"')
			rows = []
			for row in csv_reader:
				rows.append(row)
		return rows

	@classmethod
	def html_canonical_string(the_class, a_string):
		"""指定された文字列をHTML内に記述できる正式な文字列に変換して応答する。"""

		result = a_string
		result = html.escape(result, quote=True)
		result = result.replace('\n', '<br>')
		return result

	def table(self):
		"""テーブルを応答する。"""

		return self._table

	def tuples(self):
		"""タプル群を応答する。"""

		return self.table().tuples()

	def write_csv(self, filename, rows):
		"""指定されたファイルにCSVとして行たち(rows)を書き出す。"""
		with open(filename, 'wt', encoding='utf-8', newline='') as filename:
			csvout = csv.writer(file, delimiter=',', doublequote=False, lineterminator='\n', quotechar='"')
			csvout.writerows(rows)
		return
