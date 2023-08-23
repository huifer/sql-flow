package com.github.huifer.sql_flow.node;

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
public class InsertMapping {

  /**
   * 原始字段，在insert语句中的value字段
   */
  private String source;
  /**
   * 目标字段，在insert语句中的写入字段
   */
  private String target;
}
