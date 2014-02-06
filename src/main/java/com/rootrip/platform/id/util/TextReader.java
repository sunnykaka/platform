package com.rootrip.platform.id.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 从文件中读取文本。
 * @author zhang mingtao
 * @date 2011-9-28
 */
public class TextReader {
	public static String readJarTxt(String fileUri, String encoding)
			throws IOException {
		InputStream in = null;
		InputStreamReader reader = null;

		try {
			in = TextReader.class.getResourceAsStream(fileUri);
			reader = new InputStreamReader(in, encoding);
			StringBuilder sb = new StringBuilder();
			int c = reader.read();
			while (c >= 0) {
				sb.append((char) c);
				c = reader.read();
			}
			return sb.toString();
		} finally {
			closeReader(reader);
			closeIn(in);
		}
	}

	public static void closeReader(Reader r) {
		if (r != null) {
			try {
				r.close();
			} catch (IOException e) {
			}
		}
	}

	public static void closeIn(InputStream in) {
		try {
			if (in != null)
				in.close();
		} catch (IOException e) {
		}
	}
}