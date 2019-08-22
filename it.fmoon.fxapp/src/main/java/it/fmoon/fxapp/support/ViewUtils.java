package it.fmoon.fxapp.support;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import javafx.scene.Node;

public class ViewUtils {

	public static void setStyle(Node node, String styleProperty, String value) {
		String style = node.getStyle();
		Map<String,String> values = parseValues(style);
		values.put(styleProperty, value);
		String updatedStyle = values.entrySet().stream().collect(StringBuilder::new,(stringBuilder,entry)->{
			stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("; ");
		},StringBuilder::append).toString();
		node.setStyle(updatedStyle);
	}

	public static Map<String, String> parseValues(String style) {
		if (style==null || style.isBlank()) {
			return Maps.newHashMap();
		}
		return Arrays.asList(style.split(";")).stream()
		.filter(Predicate.not(String::isBlank))
		.map(entry->{
			String[] parts = entry.split(":");
			return Map.entry(parts[0].trim(), parts[1].trim());
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}
