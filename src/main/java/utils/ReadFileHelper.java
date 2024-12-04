package utils;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;


public class ReadFileHelper {
    public static List<String> extractNumbers(String filePath, String pattern) throws IOException{
        List<String> numbers = new ArrayList<>();
        String content = Files.readString(Paths.get(filePath));
        Matcher matcher = Pattern.compile(pattern).matcher(content);

        while (matcher.find()) {
            String licenceNumber = matcher.group().replaceAll("\\s+", "");
            numbers.add(licenceNumber.toUpperCase());
        }
        return numbers;
    }
}
