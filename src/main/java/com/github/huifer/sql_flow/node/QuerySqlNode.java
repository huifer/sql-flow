package com.github.huifer.sql_flow.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Setter
@FieldNameConstants
@EqualsAndHashCode
@ToString
@Slf4j
public class QuerySqlNode extends AbstractSqlNode {

  /**
   * 需要执行的sql
   */
  private String sql;
  /**
   * 查询结果字段
   */
  private List<FieldMono> queryResponseFields;
  /**
   * 条件字段
   */
  private List<FieldMono> whereFields;

  public QuerySqlNode(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  private static String handlerSql(String originalSql, String param) {

    //1. 按照空格进行split
    String[] sqlParts = originalSql.split("\\s+");

    // 2. 找到:uuu 所在的索引
    List<Integer> indexs = new ArrayList<>();
    for (int i = 0; i < sqlParts.length; i++) {
      String sqlPart = sqlParts[i];
      if (sqlPart.equals(":" + param)) {
        indexs.add(i);
      } else if (sqlPart.equals("(:" + param + ")")) {
        indexs.add(i);
      } else if (sqlPart.equals("( :" + param + " )")) {
        indexs.add(i);
      }
    }

    List<Integer[]> list = new ArrayList<>();

    for (Integer index : indexs) {
      for (int i = index; i >= 0; i--) {
        String sqlPart = sqlParts[i];
        if (sqlPart.equalsIgnoreCase("and") || sqlPart.equalsIgnoreCase("or")
            || sqlPart.equalsIgnoreCase("where")) {
          list.add(new Integer[]{i, index});
          break;

        }
      }

    }

    for (Integer[] integers : list) {
      Integer start = integers[0];
      Integer end = integers[1];

      for (int i = start; i <= end; i++) {
        sqlParts[i] = "";
      }

    }
    return String.join(" ", sqlParts);
  }


  @Transactional(rollbackFor = Exception.class)
  @Override
  public List<Map<String, Object>> run(Object param) {
    return this.execute(getJdbcTemplate(), (Map<String, Object>) param);
  }

  public List<Map<String, Object>> execute(JdbcTemplate jdbcTemplate,
      Map<String, Object> paramMap) {
    if (sql == null) {
      throw new IllegalArgumentException("SQL query is not defined.");
    }

    String resolvedSql = resolveSql(sql, paramMap);

    log.info("执行sql = {}", resolvedSql);

    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
        jdbcTemplate);

    return namedParameterJdbcTemplate.queryForList(resolvedSql, paramMap);
  }


  private String resolveSql(String sql, Map<String, Object> paramMap) {
    List<String> ignoreName = new ArrayList<>();
    for (FieldMono whereField : this.getWhereFields()) {
      String enName = whereField.getEnName();
      if (!paramMap.containsKey(enName)) {
        ignoreName.add(enName);
      }
    }
    String changeSql = sql;
    for (String s : ignoreName) {
      changeSql = handlerSql(changeSql, s);
    }
    return changeSql;
  }

  @Override
  public NodeType type() {
    return NodeType.QUERY;
  }


}
