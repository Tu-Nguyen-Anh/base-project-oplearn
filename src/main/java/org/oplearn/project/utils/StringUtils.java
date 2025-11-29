package org.oplearn.project.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

    public static boolean isEmpty(String text){
        return text == null || text.isEmpty();

    }

    public static String buildParamExceptionList(List<String> name) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String nameItem : name) {
            stringBuilder.append(nameItem).append(", ");
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
        return stringBuilder.toString();
    }

    public static String makeBoldKey(String key) {
        return "<b>" + key + "</b>";
    }

    public static String buildTQuery(String summary, List<String> typeNames) {
        // Xây dựng chuỗi typeName
        String typeNamePart = typeNames.stream()
              .map(typeName -> "\"" + typeName + "\"")
              .collect(Collectors.joining(", "));

        // Ghép chuỗi query
        return String.format("summary EQ '%s' AND typeName IN (%s)", summary, typeNamePart);
    }

    public static String buildTQuery(String organization) {

        // Ghép chuỗi query
        return String.format("organization EQ '%s' ", organization);
    }

    public static String buildTQuery(String summary, List<String> typeNames, Integer victimId) {
        // Xây dựng chuỗi typeName
        String typeNamePart = typeNames.stream()
              .map(typeName -> "\"" + typeName + "\"")
              .collect(Collectors.joining(", "));

        // Ghép chuỗi query
        return String.format("summary EQ '%s' AND typeName in (%s) AND victimId = %d ", summary, typeNamePart, victimId);
    }

    public static String generateSignature(String secretKey, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    public static String getUnixTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
}