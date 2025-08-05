/*
 * Copyright 2025 Toshiki Iga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.igapyon.remindy.loader;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@code ProverbLoader} は、リソース内の {@code proverbs.json} を読み込み、
 * 格言リスト（文字列の一覧）を返すユーティリティクラスです。
 *
 * <p>
 * 読み込みに失敗した場合は空のリストを返します。
 * </p>
 *
 * <p>
 * 使用例:
 * 
 * <pre>{@code
 * List<String> proverbs = ProverbLoader.load();
 * }</pre>
 * </p>
 *
 * 格言ファイル {@code proverbs.json} は、UTF-8 の JSON ファイルである必要があります。
 */
public class ProverbLoader {
	/**
	 * {@code proverbs.json} を読み込んで、格言のリストを返します。
	 *
	 * @return 格言のリスト（読み込み失敗時は空リスト）
	 */
	public static List<String> load() {
		try (InputStreamReader reader = new InputStreamReader(
				ProverbLoader.class.getClassLoader().getResourceAsStream("proverbs.json"), StandardCharsets.UTF_8)) {
			ObjectMapper mapper = new ObjectMapper();
			List<String> list = mapper.readValue(reader, new TypeReference<List<String>>() {
			});
			System.err.println("格言を " + list.size() + " 件読み込みました。");
			return list;
		} catch (Exception e) {
			System.err.println("proverbs.json 読み込みエラー: " + e.getMessage());
			return Collections.emptyList();
		}
	}
}
