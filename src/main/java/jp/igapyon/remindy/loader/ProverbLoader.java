package jp.igapyon.remindy.loader;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProverbLoader {
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
