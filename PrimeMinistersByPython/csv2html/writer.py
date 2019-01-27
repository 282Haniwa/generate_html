#!/usr/bin/env python
# -*- coding: utf-8 -*-

import datetime
import os
import sys

from .io import IO

class Writer(IO):
	"""ライタ：情報のテーブルをHTMLページとして書き出す。"""

	def __init__(self, output_table):
		"""ライタのコンストラクタ。HTMLページを基にするテーブルを受け取る。"""

		super(Writer, self).__init__(output_table)

		return

	def perform(self):
		"""HTMLページを基にするテーブルから、インデックスファイル(index_html)に書き出す。"""
		#TODO: 生成したhtmlのインデントをちゃんとしたい。

		class_attributes = self.attributes().__class__
		base_directory = class_attributes.base_directory()
		index_html = class_attributes.index_html()

		html_filename = os.path.join(base_directory, index_html)
		with open(html_filename, 'wt') as a_file:
			a_file.write('<html lang="ja">\n')
			self.write_head(a_file)
			a_file.write('<body>\n')
			self.write_header(a_file)
			self.write_body(a_file)
			self.write_footer(a_file)
			a_file.write('</body>\n')
			a_file.write('</html>\n')

		return

	def write_body(self, file):
		"""ボディを書き出す。つまり、属性リストを書き出し、タプル群を書き出す。"""

		indent = 0
		with_indent = (lambda string: (indent * '\t') + string)
		file.write(with_indent('<table class="content" summary="table">\n'))
		indent += 1
		file.write(with_indent('<thead>\n'))
		indent += 1
		file.write(with_indent('<tr>\n'))
		indent += 1
		for name in self.table().attributes().names():
			file.write(with_indent('<td class="center-pink"><strong>{}</strong></td>\n'.format(name)))
		indent -= 1
		file.write(with_indent('</tr>\n'))
		indent -= 1
		file.write(with_indent('</thead>\n'))
		file.write(with_indent('<tbody>\n'))
		indent += 1
		for index, tuple in enumerate(self.table().tuples()):
			file.write(with_indent('<tr>\n'))
			indent += 1
			if index % 2 == 0:
				for value in tuple.values():
					file.write(with_indent('<td class="center-blue">{}</td>\n'.format(value)))
			else:
				for value in tuple.values():
					file.write(with_indent('<td class="center-yellow">{}</td>\n'.format(value)))
			indent -= 1
			file.write(with_indent('</tr>\n'))
		indent -= 1
		file.write(with_indent('</tbody>\n'))
		indent -= 1
		file.write(with_indent('</table>\n'))
		return

	def write_footer(self, file):
		"""フッタを書き出す。"""

		date = datetime.datetime.now().strftime('%Y/%m/%d %H:%M:%S')
		file.write('<hr>\n')
		file.write('<div class="right-small">Created by Hukurou 9th (CSV2HTML written by Python) {}</div>\n'.format(date))

		return

	def write_head(self, file):
		"""htmlのヘッドを書き出す。"""

		lines = []
		lines.append('<head>\n')
		lines.append('<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">\n')
		lines.append('<meta name="author" content="Hukurou 9th">\n')
		lines.append('<style type="text/css">\n')
		style_filename = os.path.join('assets', 'style.css')
		with open(style_filename, 'r') as a_style_file:
			lines.extend(a_style_file.readlines())
		lines.append('</style>\n')
		# TODO: html_canonical_stringの適応
		lines.append('<title>{}</title>\n'.format(self.attributes().title_string()))
		lines.append('</head>\n')
		file.writelines(lines)

		return

	def write_header(self, file):
		"""ヘッダを書き出す。"""

		file.write('<div class="belt">\n')
		# TODO: html_canonical_stringの適応
		file.write('\t<h2>{}</h2>\n'.format(self.attributes().caption_string()))
		file.write('</div>\n')

		return
