package com.github.huifer.sql_flow.node;

import com.zaxxer.hikari.util.DriverDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class InsertNodeTest {

  @Test
  public void test() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.setDataSource(new DriverDataSource(
        "jdbc:mysql://192.168.1.11:3306/sql-flow?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false",
        "com.mysql.cj.jdbc.Driver", new Properties(), "root", "YouCon123!"));

    InsertSqlNode insertNode = new InsertSqlNode(jdbcTemplate);
    insertNode.setJdbcTemplate(jdbcTemplate);
    ArrayList<Map<String, Object>> params = new ArrayList<>();
    HashMap<String, Object> e = new HashMap<>();
    e.put("n", "n");
    params.add(e);
    HashMap<String, Object> e2 = new HashMap<>();
    e2.put("n", "n222");
    params.add(e2);
    ArrayList<InsertMapping> insertMappings1 = new ArrayList<>();
    insertMappings1.add(new InsertMapping("n", "name"));
    insertNode.setInsertMappings(insertMappings1);
    insertNode.setTable("table_a");
    insertNode.run( params);
  }
}