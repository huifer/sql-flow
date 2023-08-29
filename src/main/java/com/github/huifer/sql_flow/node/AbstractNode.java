package com.github.huifer.sql_flow.node;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

@Getter
public abstract class AbstractNode {

  public abstract NodeType type();


  @Setter
  private String uid;

  @Transactional(rollbackFor = Exception.class)
  abstract public Object run(Object param);

}
