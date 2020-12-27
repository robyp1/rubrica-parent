package com.cadit.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ritorna l' EntityManager associato al thread
 * (ogni thread ha il suo em. perenne)
 * ThreadSafe
 * Normalmente in un contesto JEE, si può utilizzare
 * JTA + CMP (content management persistence) ovvero la transazione
 * viene gestita dal container (a meno di usare User Transaction)
 * e l'em. viene iniettato tramite CDI dal container (@PersistenceContext), 
 * così da non doversi preoccupare di utilizzare il threadlocal come fatto qui
 *  NOTA sulle transazioni:
 * se stessa transazione T1, le mod sono prese dalla cache
 * una altra transazione T2, vede le modifiche di T1 solo quando T1 committa
 * perchè l'entity manager è per thread e la cache di 1 liv è 
 * per transazione. Per avere una cache globale, indipendente 
 * dalle transazioni e in share tra i vari entity manager/transazioni introdurre
 * una cache di livello 2 tipo ehcache o jboss cache..
 * @author Roby
 *
 * @param <T>
 * @param <I>
 */
public final class PersistanceManager  {


	  //protected Logger  logger   = LoggerFactory.getLogger(getClass());
	  
	  private static final EntityManagerFactory emf;
	  private static ThreadLocal<EntityManager> them;
	   
	   static {
	       emf = Persistence.createEntityManagerFactory("testapp"); 
	       them = new ThreadLocal<EntityManager>();
	   } 

	 public static EntityManager getEntityManager() throws IllegalStateException{
	   EntityManager em = them.get();
	   
	   if (em== null){
		   em = emf.createEntityManager();
		   them.set(em);
	   }
	   return em;
	  }

	public static void releaseEntityManager() {
		 EntityManager em = them.get();
	     if (em!=null) them.set(null);
		
	}
	 
	
}
