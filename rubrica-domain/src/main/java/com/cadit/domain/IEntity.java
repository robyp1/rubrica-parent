package com.cadit.domain;

import java.io.Serializable;

public interface IEntity <I extends Serializable> extends Serializable{

/**
  * Property rappresenta la primary key.
  */
  String P_ID = "id";

  /**
   * restituisce la primary key
   * @return
   */
  I getId();
  
  /**
   * imposta la primary key
   * @param id
   */
  void setId(I id);
}
