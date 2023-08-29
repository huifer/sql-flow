package com.github.huifer.sql_flow.node;

import static org.junit.jupiter.api.Assertions.*;

import com.zaxxer.hikari.util.DriverDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class DeleteSqlNodeTest {

  @Test
  void test() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.setDataSource(new DriverDataSource(
        "jdbc:mysql://192.168.1.11:3306/sql-flow?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false",
        "com.mysql.cj.jdbc.Driver", new Properties(), "root", "YouCon123!"));

    DeleteSqlNode deleteSqlNode = new DeleteSqlNode(jdbcTemplate);
    deleteSqlNode.setSql("""
        DELETE FROM t_ins where id = :id
        """);

    ArrayList<FieldMono> whereFields1 = new ArrayList<>();
    whereFields1.add(new FieldMono("主键", "id"));
    whereFields1.add(new FieldMono("主键", "uc"));
    deleteSqlNode.setWhereFields(whereFields1);


    HashMap<String, Object> paramMap = new HashMap<>();
    paramMap.put("id", 3);
    deleteSqlNode.execute(jdbcTemplate, paramMap);
  }
}