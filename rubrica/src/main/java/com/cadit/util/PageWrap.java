package com.cadit.util;

import java.util.Iterator;
import java.lang.IllegalStateException;
import java.util.List;

public class PageWrap<T> implements Iterator<List<T>>{

	private int index = 0;
	private int pageSize;
	private Qwrap qw_;
	private int totalrow_ = 0;
	private boolean next=false;
	
	
	/**
	 * 
	 * @param index numero di pagina (prima pagina parte da 0)
	 * @param pageSize dimensione record per pagina
	 * @param qw istanza di Qrap contenente la query da impaginare
	 * @param totalrow contiene il numero totale di record da impaginare (di solito è il ris di una select count)
	 */
	public PageWrap(int Index, int PageSize, Qwrap qw, int totalrow) {
		super();
		this.index = Index;
		this.pageSize = PageSize;
		this.qw_ = qw;
		this.totalrow_ = totalrow;
	}


	public boolean hasNext(){
		//return next;// non corretto, forse bisogna avere il tot pagine /pageSize per stabilire se ci sono ancora pagine!
		if ( totalrow_ > 0)
		{
			if (index+1 < (totalrow_/pageSize)){
				next=true;
			}
			else next=false;
		}
		 return next;
	}

	
    @SuppressWarnings("unchecked")
	public List<T> next(){
    	qw_.q.setFirstResult(getFirst());
    	qw_.q.setMaxResults(pageSize);
    	List<T> results= qw_.q.getResultList();
    	if (results.size() > 0 ){
    		next=true;//non andrebbe fatto qui..
    	}
    	index++;//incremento la pagina
    	return results;
    	
    }
    
    
    private int getFirst(){
    	return index * pageSize;
    }
}
