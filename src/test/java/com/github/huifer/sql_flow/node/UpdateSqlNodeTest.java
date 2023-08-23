package com.github.huifer.sql_flow.node;

import static org.junit.jupiter.api.Assertions.*;

import com.zaxxer.hikari.util.DriverDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class UpdateSqlNodeTest {

  @Test
  void test(){
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.setDataSource(new DriverDataSource(
        "jdbc:mysql://192.168.1.11:3306/sql-flow?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false",
        "com.mysql.cj.jdbc.Driver", new Properties(), "root", "YouCon123!"));

    UpdateSqlNode updateSqlNode = new UpdateSqlNode(jdbcTemplate);
    updateSqlNode.setTable("t_ins");
    updateSqlNode.setWhereSql("""
        
        WHERE id = :id
        """);
// UPDATE t_ins SET ug = :n

    ArrayList<FieldMono> whereFields1 = new ArrayList<>();
    whereFields1.add(new FieldMono("主键", "id"));
    whereFields1.add(new FieldMono("主键", "uc"));
    updateSqlNode.setWhereFields(whereFields1);

    ArrayList<UpdateMapping> updateMappings = new ArrayList<>();
    updateMappings.add(new UpdateMapping("gggg", "ug"));
    updateMappings.add(new UpdateMapping("gj", "gj"));
    updateSqlNode.setUpdateMappings(updateMappings);

    HashMap<String, Object> paramMap = new HashMap<>();
    paramMap.put("id", 1);
    paramMap.put("gggg", "修改");
    paramMap.put("gj", "修改");
    updateSqlNode.execute(jdbcTemplate, paramMap);
  }
}