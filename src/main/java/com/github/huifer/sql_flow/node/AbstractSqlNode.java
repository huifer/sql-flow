package com.github.huifer.sql_flow.node;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

@Getter
public abstract class AbstractSqlNode extends AbstractNode {

  @Setter
  private JdbcTemplate jdbcTemplate;


  public AbstractSqlNode(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public abstract NodeType type();
}
