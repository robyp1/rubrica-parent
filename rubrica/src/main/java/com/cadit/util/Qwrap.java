package com.cadit.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.cadit.dao.DaoGeneric;

public class Qwrap {

	Query q;
	private PageWrap pageManager;

	public Qwrap(Query q) {
		super();
		this.q = q;
	}
	
	/**
	 * se si usa questo metodo, il bean Qwrap va messo in sessione(?? mi sa che non è serializzabile!)
	 * e va recuperato nella chiamata alla seconda pagina quando
	 * si chiama pagesResult o  pageResultbyPos
	 * @param firstpage
	 * @param pageSize
	 * @return 
	 * @return
	 */
	public <T> void wrappingResultToPages(int firstpage, int pageSize, Class<T> clazz, int totalRow ) {
		if (firstpage <1) 
			throw new IllegalStateException("La prima pagina deve partire da 1");
		this.pageManager = new PageWrap<T>(firstpage, pageSize, this, totalRow);
	}
	
	 public Qwrap par(String pname, Object v){
	    q.setParameter(pname, v);
	    return this;
	  }
	 
	 @SuppressWarnings("unchecked")
	public<T> T singleResult(Class<T> clazz){
	 try{
	      return (T) q.getSingleResult();
	    } catch(NoResultException nre){
	      return null;
	    }
	 }

	 @SuppressWarnings("unchecked")
	public<T> List<T> multiResult(Class<T> clazz){
		 try{
	      return  (List<T>) q.getResultList();
	    } catch(NoResultException nre){
	      return null;
	    }
	 }
	 
	 /*
	  *Eseguendo una query si vuole avere pagina per pagina
	  */
	@SuppressWarnings("unchecked")
	public<T> List<T> pagesResult(){
		 try{
			 if (pageManager.hasNext()){
				 return pageManager.next();
			 }
	      return new LinkedList<T>();
	    } catch(NoResultException nre){
	      return null;
	    }
	 }

	 
	 /**
	  * ritorna il nome della classe Entità del database
	  * @param type Class type dell'istanza entity da interrogare
	  * @return
	  */
	    public  final static String getEntityName(Class type) {        
	        Entity entity = (Entity)type.getAnnotation(Entity.class);

	        if (entity == null || entity.name() == null || entity.name().length() == 0) {
	            return type.getSimpleName();
	        } else {
	            return entity.name();
	        }
	    }

	
	   
}
