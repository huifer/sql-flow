package com.github.huifer.sql_flow.node;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@EqualsAndHashCode
@ToString
public class Edge {

  private AbstractNode start;
  private AbstractNode end;


  public void run(Object o) {

    if (start instanceof QuerySqlNode) {
      List<Map<String, Object>> run = ((QuerySqlNode) start).run(o);
        end.run(run);



    }

  }
}
