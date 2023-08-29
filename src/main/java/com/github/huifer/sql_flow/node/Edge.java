package com.github.huifer.sql_flow.node;

import com.github.huifer.sql_flow.utils.FlowTempStorageUtils;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@EqualsAndHashCode
@ToString
public class Edge {

  /**
   * 起点
   */
  private AbstractNode start;
  /**
   * 终点
   */
  private AbstractNode end;

  /**
   * 执行条件
   */
  private Term term;


  @Transactional(rollbackFor = Exception.class)
  public Object run(Object o) throws Exception {

    if (start instanceof QuerySqlNode) {

      List<Map<String, Object>> run = ((QuerySqlNode) start).run(o);
      FlowTempStorageUtils.setQueryData(start.getUid(), run);
      Object run1 = end.run(run);
      FlowTempStorageUtils.setQueryData(end.getUid(), run1);
      return run1;
    }

    return null;
  }
}
