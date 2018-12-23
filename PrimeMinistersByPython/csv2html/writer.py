#!/usr/bin/env python
# -*- coding: utf-8 -*-

import datetime
import os
import sys

from io import IO

class Writer(IO):
	"""ライタ：情報のテーブルをHTMLページとして書き出す。"""

	def __init__(self, output_table):
		"""ライタのコンストラクタ。HTMLページを基にするテーブルを受け取る。"""

		super(Writer, self).__init__(output_table)

		return

	def perform(self):
		"""HTMLページを基にするテーブルから、インデックスファイル(index_html)に書き出す。"""

		class_attributes = self.attributes().__class__
		base_directory = class_attributes.base_directory()
		index_html = class_attributes.index_html()

		html_filename = os.path.join(base_directory, index_html)
		with open(html_filename, 'wb') as a_file:
			a_file.write('<html lang="ja">')
			self.write_head(a_file)
			a_file.write('<body>')
			self.write_header(a_file)
			self.write_body(a_file)
			self.write_footer(a_file)
			a_file.write('</body>')
			a_file.write('</html>')

		return

	def write_body(self, file):
		"""ボディを書き出す。つまり、属性リストを書き出し、タプル群を書き出す。"""

		file.write(self.table().__str__())

		return

	def write_footer(self, file):
		"""フッタを書き出す。"""

		date = datetime.datetime.now().strftime('%Y/%m/%d %H:%M:%S')
		file.write('<hr>')
		file.write('<div class="right-small">Created by Hukurou 9th (CSV2HTML written by Python) {}</div>'.format(date))

		return

	def write_head(self, file):
		"""htmlのヘッドを書き出す。"""

		file.write('<head>')
		file.write('<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">')
		file.write('<meta name="author" content="Hukurou 9th">')
		file.write('<style type="text/css">')
		style_filename = os.path.join('assets', 'style.css')
		with open(style_filename, 'r') as a_style_file:
			file.writelines(a_style_file.readlines())
		file.write('</style>')
		# TODO: html_canonical_stringの適応
		file.write('<title>{}</title>'.format(self.attributes().title_string()))
		file.write('</head>')

		return

	def write_header(self, file):
		"""ヘッダを書き出す。"""

		# TODO: html_canonical_stringの適応
		file.write('<div class="belt"><h2>{}</h2></div>'.format(self.attributes().caption_string()))

		return
