package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueriesGetter {


    public static List<String> getQueries(String filePath) throws IOException {
        List<String> res = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while (line != null) {
                String[] lines = line.split(";");
                res.add(String.format(lines[0], lines[1].split(",")));
                line = br.readLine();
            }
        }
        return res;
    }
}
