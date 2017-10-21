package com.ef.parser;

import com.ef.database.JDBCObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author user
 */
public class Parser {

//    private static
    private static void readFile(String fileName) {
        List<String> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            //1. filter line 3
            //2. convert all content to upper case
            //3. convert it into a List
            list = stream
                    .filter(line -> Integer.parseInt(line.split("|")[0]) > 100)
                    .map(line -> line.split("|")[0])
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void db() {
        JDBCObject.makeJDBCConnection();
    }

    public static void main(String[] args) {
        String startDate = "2017-01-01.13:00:00";
        String duration = "hourly";
        String threshold = "100";
        String fileName = "txt0";
        readFile(fileName);
    }
}
