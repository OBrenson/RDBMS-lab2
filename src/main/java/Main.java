import dao.ClickHouseExecutor;
import parser.QueriesGetter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        List<String> queries = QueriesGetter.getQueries("src/main/resources/scripts.txt");
        ClickHouseExecutor executor = new ClickHouseExecutor("127.0.0.1", 8123);
        for(String query : queries) {
            System.out.println("QUERY: " + query);
            System.out.println("RESULT: ");
            Map<Integer, List<String>> res = executor.executeQuery(query);
            printColumns(res);
        }
    }

    public static void printColumns(Map<Integer, List<String>> map) {
        String formatter = "";
        for(int i = 0; i < map.get(0).size(); i++) {
            formatter += "%30s ";
        }
        for(int i = 0; i < map.size(); i++) {
            System.out.format(formatter, map.get(i).toArray());
            System.out.println();
        }
    }
}
