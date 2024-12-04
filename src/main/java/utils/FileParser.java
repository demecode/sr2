package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileParser {
    public static Map<String, Map<String, String>> parseDataFile(String filePath) throws IOException {
        Map<String, Map<String, String>> data_map = new HashMap<>();
        String content = Files.readString(Paths.get(filePath));

        System.out.println("RAW DATA 2: ");
        System.out.println(content);

        String[] lines = content.split("\\r?\\n");
        for (int i =1; i<lines.length; i++) {





            String[] fields = lines[i].split(",");
            if (fields.length >= 4) {
                Map<String, String> data_returned = new HashMap<>();
                data_returned.put("MAKE", fields[1].trim());
                data_returned.put("MODEL", fields[2]);
                data_returned.put("YEAR", fields[3]);
                data_map.put(fields[0].trim().replaceAll("\\s+", "").toUpperCase(), data_returned);
            } else {
                System.err.println("Broken lines : " + lines[i]);
            }
        }
        System.out.println("Parsed Datae: "+ data_map);
        return data_map;
    }
    private static String[] splitQuotes(String line){
        String regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        return line.split(regex);
    }

}
