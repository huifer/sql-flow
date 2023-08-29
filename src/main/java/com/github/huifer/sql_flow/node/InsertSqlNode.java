package com.github.huifer.sql_flow.node;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Getter
@Setter
@FieldNameConstants
@EqualsAndHashCode
@ToString
public class InsertSqlNode extends AbstractSqlNode {

  private String table;
  private List<InsertMapping> insertMappings;

  public InsertSqlNode(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public NodeType type() {
    return NodeType.INSERT;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)

  public Integer run(Object param) {
    try {
      return execute((List<Map<String, Object>>) param);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Integer execute(List<Map<String, Object>> params) throws Exception {
    if (table == null || insertMappings == null || insertMappings.isEmpty() || params == null
        || params.isEmpty()) {
      throw new IllegalArgumentException("Invalid input data for executing INSERT statement.");
    }

    List<String> columns = new ArrayList<>();
    List<String> valuesPlaceholder = new ArrayList<>();
    List<Object> values = new ArrayList<>();

    for (InsertMapping mapping : insertMappings) {
      columns.add(mapping.getTarget());
      valuesPlaceholder.add("?");
    }

    List<String> valueRows = new ArrayList<>();

    for (Map<String, Object> paramMap : params) {
      List<Object> rowValues = new ArrayList<>();
      for (InsertMapping mapping : insertMappings) {
        String paramName = mapping.getSource();
        if (!paramMap.containsKey(paramName)) {
          throw new IllegalArgumentException("Missing parameter value for column: " + paramName);
        }
        rowValues.add(paramMap.get(paramName));
      }
      values.addAll(rowValues);
      valueRows.add("(" + String.join(", ", Collections.nCopies(rowValues.size(), "?")) + ")");
    }

    String columnsString = String.join(", ", columns);
    String valuesRowsString = String.join(", ", valueRows);
    String sql = "INSERT INTO " + table + " (" + columnsString + ") VALUES " + valuesRowsString;

    return this.getJdbcTemplate().update(sql, values.toArray());
  }


}
