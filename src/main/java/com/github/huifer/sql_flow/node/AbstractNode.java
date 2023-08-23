package com.github.huifer.sql_flow.node;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class AbstractNode {

  public abstract NodeType type();


  @Setter
  private String uid;

  abstract public Object run(Object param);

}
