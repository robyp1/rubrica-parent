package com.cadit.domain;

import java.io.Serializable;
import java.util.List;


public abstract interface IDao<T extends IEntity<I>,I extends Serializable> {

	
	public T findById(I id);
	
	public List<T> findAll();
	
	public List<T> findByExample(T entity);
	
	/**
	 * save or update an object
	 * @param entity
	 * @return
	 */
	public T saveOrUpdate(T entity, I id);
	
	public T save(T entity);
	
	public T saveAndFlushPK (T entity);
	
	public boolean delete(I id) throws UnsupportedOperationException;
	
	 /**
	   * Refresh a persistant object that may have changed in another thread/transaction.
	   * 
	   * @param entity transient entity
	   */
	public void refresh(T entity);
	
	/**
	   * Write to database anything that is pending operation and clear it.
	   */
	public void flushAndClear();

	
	public void closeEntityManager() throws Exception;

    public void beginTransaction() throws IllegalStateException;
    
    public boolean endTransaction(boolean close)throws Exception;
    
    public void markRollback() throws IllegalStateException;
}
