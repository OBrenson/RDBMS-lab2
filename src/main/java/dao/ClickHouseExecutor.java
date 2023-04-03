package dao;

import com.clickhouse.client.*;
import com.clickhouse.data.ClickHouseColumn;
import com.clickhouse.data.ClickHouseFormat;
import com.clickhouse.data.ClickHouseRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickHouseExecutor {

    private final ClickHouseNode server;

    public ClickHouseExecutor(String host, int port) throws SQLException {
        this.server = ClickHouseNode.of(String.format("http://%s:%d?compress=0&compress_algorithm=gzip", host, port));
    }

    public Map<Integer, List<String>> executeQuery(String query) {
        Map<Integer, List<String>> res = new HashMap<>();
        try(ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol())) {
            ClickHouseResponse response = client.read(server)
                    .format(ClickHouseFormat.RowBinaryWithNamesAndTypes)
                    .query(query).executeAndWait();
            List<String> columnsNames = new ArrayList<>();
            for(ClickHouseColumn col : response.getColumns()) {
                columnsNames.add(col.getColumnName());
            }
            res.put(0, columnsNames);
            int counter = 1;
            for(ClickHouseRecord r : response.records()) {
                List<String> values = new ArrayList<>();
                for(String col : columnsNames) {
                    values.add(r.getValue(col).asString());
                }
                res.put(counter, values);
                counter ++;
            }
        } catch (ClickHouseException e) {
            e.printStackTrace();
        }
        return res;
    }
}
