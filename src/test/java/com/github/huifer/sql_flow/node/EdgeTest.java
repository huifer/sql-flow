package com.github.huifer.sql_flow.node;

import com.github.huifer.sql_flow.utils.FlowTempStorageUtils;
import com.github.huifer.sql_flow.utils.GsonFactory;
import com.google.gson.Gson;
import com.zaxxer.hikari.util.DriverDataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class EdgeTest {

  Gson gson = GsonFactory.getGson();

  @Test
  public void run()throws Exception{
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.setDataSource(new DriverDataSource(
        "jdbc:log4jdbc:mysql://192.168.1.11:3306/sql-flow?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false",
        "net.sf.log4jdbc.DriverSpy", new Properties(), "root", "YouCon123!"));

    Connection connection = jdbcTemplate.getDataSource().getConnection();
    connection.setAutoCommit(false);



    QuerySqlNode queryNode = new QuerySqlNode(jdbcTemplate);
    queryNode.setUid("node_1");
    queryNode.setJdbcTemplate(jdbcTemplate);
    queryNode.setSql("""
            
        SELECT
          a.id,
          a.name,
          a.type,
          a.size,
          a.time,
          b.description,
          c.project_name,
          CASE
            WHEN a.size < 10 THEN 'Small'
            WHEN a.size >= 10 AND a.size < 50 THEN 'Medium'
            ELSE 'Large'
          END AS size_category
        FROM
          table_a a
        LEFT JOIN
          table_b b ON a.type = b.type_id
        LEFT JOIN
          table_c c ON a.type = c.type_id and c.id in (:uc)
        WHERE
          a.type IN (1, 2, 3)
          AND (b.description IS NOT NULL OR c.project_name IS NOT NULL)
          AND (c.project_status = :uuu OR c.project_status IS NULL)
        ORDER BY
          a.id;
            
        """);

    ArrayList<FieldMono> queryResponseFields1 = new ArrayList<>();
    queryResponseFields1.add(new FieldMono("主键", "a.id"));
    queryResponseFields1.add(new FieldMono("名称", "a.NAME"));
    queryResponseFields1.add(new FieldMono("类型", "a.type"));
    queryResponseFields1.add(new FieldMono("尺寸", "a.size"));
    queryResponseFields1.add(new FieldMono("描述", "b.description"));
    queryResponseFields1.add(new FieldMono("项目", "c.project_name"));
    queryNode.setQueryResponseFields(queryResponseFields1);

    ArrayList<FieldMono> whereFields1 = new ArrayList<>();
    whereFields1.add(new FieldMono("主键", "uuu"));
    whereFields1.add(new FieldMono("主键", "uc"));
    queryNode.setWhereFields(whereFields1);

    HashMap<String, Object> param = new HashMap<>();
    param.put("uuu", "Active");
    List<Integer> lists = new ArrayList<>();
    lists.add(1);
    lists.add(2);
//    param.put("uc", lists);

    InsertSqlNode insertNode = new InsertSqlNode(jdbcTemplate);
    insertNode.setUid("node_2");
    insertNode.setJdbcTemplate(jdbcTemplate);

    ArrayList<InsertMapping> insertMappings1 = new ArrayList<>();
    insertMappings1.add(new InsertMapping("project_name", "ug"));
    insertNode.setInsertMappings(insertMappings1);
    insertNode.setTable("t_ins");

    Edge edge = new Edge();

    edge.setStart(queryNode);
    edge.setEnd(insertNode);
    String json = gson.toJson(edge);
    System.out.println(json);
    edge.run(param);
    Map<String, Object> queryData = FlowTempStorageUtils.getQueryData();
    System.out.println(gson.toJson(queryData));
  }
}