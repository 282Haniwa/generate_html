#!/usr/bin/env python
# -*- coding: utf-8 -*-

import csv

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
		import re
		with open(filename, 'rb') as data:
			a_string = data.read()
			regex_title = r"[一-龠ぁ-んァ-ヴ\,\,]+\n"
        	regex_main = r"[\d]+[ー〜（）\,\-\"一-龠ぁ-んァ-ヴ\w(\s|　)]+\/[\d]*\.jpg\,[\w]*\/[\d]*\.jpg"
        	regex_tokugawa = regex_main+r"\,[\"一-龠ぁ-んァ-ヴ\,]*\n"
        	regex_tokugawa_1 = regex_tokugawa + r"[一-龠ぁ-んァ-ヴ\"\,]*\n"
        	csv_rows = re.findall("("+regex_tokugawa_1
                                +"|"+regex_tokugawa+"|"
                                +regex_main+"|"+regex_title+")"
                                , a_string)
		return csv_rows
		
		

	@classmethod
	def html_canonical_string(the_class, a_string):
		"""指定された文字列をHTML内に記述できる正式な文字列に変換して応答する。"""
		
		return None

	def table(self):
		"""テーブルを応答する。"""

		return self._table

	def tuples(self):
		"""タプル群を応答する。"""

		return self.table().tuples()

	def write_csv(self, filename, rows):
		"""指定されたファイルにCSVとして行たち(rows)を書き出す。"""
		with open('filename', 'wt') as filename:
			csvout = csv.writer(filename)
			csvout.writerows(rows)
		return
