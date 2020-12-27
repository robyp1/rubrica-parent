package com.cadit.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;

import com.cadit.domain.IDao;
import com.cadit.domain.IEntity;
import com.cadit.util.Qwrap;

/**
 * metodi base per lettura scrittura e update su database
 * - persistent/managed/attached: lo stato dell'oggetto viene sincronizzato
 * aggiornando lo stato su db una volta chiuso il Persistence Context(flush o commit), 
 * (ogni sua modifica viene resa persistente sul db)
 * - detached : l'oggetto non è sincronizzato al db e ogni sua modifica 
 * non viene salvata su db (occorre passarlo come parametro al metodo
 *  merge oppure saveOrUpdate.
 * NB: A volte prima di aggiornarlo se si vuole prendere l'ultima versione
 * su db occorre sincronizzarlo con il meotod refresh, che si occupata aggiorna
 * lo stato con quello salvato su db (attenzione: l'oggetto viene sovrascritto in cache
 * con i valori presenti sul db) 
 * - Transient: l'oggetto è nuovo e non è ancora presente su db
 * @author Roby
 * cancellare il package com.cadit.ex.dao quando non serve più
 * @param <T> :Entity<I> tutti gli entity devono avere un metodo getID e setID
 * che imposta e restituisce la chiave primaria (implementano Entity<I>):
 * se un oggetto non ha chiave comporla con più campi tramite una classe esterna (o embedded?)
 * @param <I>: è il tipo associato alla chiave primaria.
 */
public class DaoGeneric<T extends IEntity<I>, I extends Serializable> implements IDao<T,I> {

	protected Class<IEntity<I>> clazz;

	@SuppressWarnings(value = "unchecked")
	
	/**
	 * usare l'altro costruttore
	 */
	private DaoGeneric(){
		Class<?> c= getClass().getSuperclass();
	        this.clazz = (Class<IEntity<I>>) c;
	      
	}
	
	public DaoGeneric(Class<IEntity<I>> clazzs){
		this.clazz = clazzs; 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	/**
	 * restituisce e cerca l'oggetto in base alla chiave primaria
	 * la lettura è Read committed (niente letture sporche)
	 * come impostare il parametro LockModeType.NONE
	 * Se si vuole impedire che la transazione più vecchia
	 * termini dopo la più recente causando una sovrascrittura
	 * di questa ultima, usare l'optimistic locking che avvisa che la
	 * transazione più vecchia non può compiersi in quanto una altra
	 * prima ha già committato
	 */
	public T findById(I id) {
		EntityManager outer = PersistanceManager.getEntityManager();
		if (outer == null)
			 return null;
		return (T) outer.find(this.clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * select * from la classe T a cui il dao corrisponde
	 */
	public List<T> findAll() {
		Qwrap q= query("from " + Qwrap.getEntityName(this.clazz));
        return (List<T>) q.multiResult(clazz);
	}

	/**
	 * 
	 * come la insert, l'oggetto SE nuovo viene salvato in cache e 
	 * e sincronizzato su db: l'oggetto restituito
	 * se modificato, d'ora in poi al
	 * flush o alla commit+chiusura della connessione viene salvato automaticamente 
	 * sul db;
	 * se l'oggetto è già managed/persistente/attached, allora viene restituito 
	 * e solo al flush o chiusura/end Transaction viene reso persistente
	 * (e quindi il suo stato salvato su database (sincronizzato))
	 * T entity : può essere detached o managed/persisted
	 * I id : è la chiave primaria
	 */
	@Override
	public T saveOrUpdate(final T entity, I id) {
	@SuppressWarnings("unused")
	T persisted;
	if ( (persisted = findById(id)) == null){ //se non esiste su db
		 save(entity); //insert
		 return entity;
	 }//altrim. update al flush/close
	 return persisted; //l'istanza è a questo punto agganciata al db (persisted)
	}
	
	/**
	 * Come il metodo Save, solo che va usato all'interno di un contesto transazionale
	 * cioè va tra il begin ed il commit altrimenti select * va in lock
	 * Vedere se esiste il metodo WITH(nolock) nella HQL (jpa hibernate query language)
	 * Ad esempio si usa quando si vuole recuperare la chiave autogenerante dal db e quindi
	 * l'oggetto deve essere sincronizzato sul db
	 * @param entity
	 * @return
	 */
	@Override
	public T saveAndFlushPK(T entity) {
		if (entity != null){
			EntityManager outer = PersistanceManager.getEntityManager();
			outer.persist(entity);
			 //eseguo il flush: nel caso di inserimenti la cui pk viene autogenerata, così viene aggiornato l'oggetto su db
			outer.flush();
			//outer.refresh(entity); non credo ci sia bisogno di un refresh essendo entity reso managed/persisted, automaticamente dovrebbe essere già aggiornato
			return entity;
		}
		throw new IllegalStateException("manca l'oggetto da persistere sul db, entity: " + entity);
	}

	/**
	 * come la insert, l'oggetto nuovo viene subito salvato su db appena
	 * si esegue flush o il commit, a livello di cache è già persistente/managed (non più transient)
	 * questo ultimo è l'oggetto restituito che
	 * d'ora in poi, se modificato, al
	 * flush o alla chiusura della connessione 
	 *  viene salvato automaticamente su db senza richiamare save
	 * 
	 */
	@Override
	public T save(T entity) {
		if (entity != null){
			EntityManager outer = PersistanceManager.getEntityManager();
			outer.persist(entity);
			 //eseguo il flush: nel caso di inserimenti la cui pk viene autogenerata, così viene aggiornato l'oggetto su db
			//outer.flush();
			//outer.refresh(entity); non credo ci sia bisogno di un refresh essendo entity reso managed/persisted, automaticamente dovrebbe essere già aggiornato
			return entity;
		}
		throw new IllegalStateException("manca l'oggetto da persistere sul db, entity: " + entity);
	}

	/**
	 * cancella un oggetto in base alla chiave primaria, rendendo l'oggetto non più managed/persistent
	 */
	@Override
	public boolean delete(I id) throws IllegalStateException, TransactionRequiredException  {
		EntityManager outer = PersistanceManager.getEntityManager();
		if (outer == null)
			 return false;
		if (id != null){
			T entity = findById(id);
			if (entity != null){
				outer.remove(entity);
				return true;
			}
			else {
				throw new IllegalStateException("Delete failed: l'istanza è null");
			}
		}
		return false;
		
	}

	/**
	 * Aggiorna l'entity passata sincronizzandola con l'ultimo
	 * stato sul database, l'entity viene perciò sovrascritta
	 * con la versione su database, utile quando si vuole
	 * recuperare l'ultima versione attuale sul db per modificarla.
	 * Una volta modificata essendo l'entità restituita di tipo managed
	 * viene salvata su db (quando si fa commit o tolta 
	 * se si fa rollback) , attenzione: se e solo se è l'unica entità managed
	 * e/o tutte le altre managed non vengono modificate successivamente!
	 */
	  @Override
	  public void refresh(final T entity) {
		EntityManager outer = PersistanceManager.getEntityManager();
		if (outer == null)
			 return;
	    outer.refresh(entity);
	  }


/**
 * chiamare solo una volta alla fine per chiudere una transazione (commit/rollback)
 * e/o chiudere il persistence Context, togliendo alla fine anche il threadlocal
 */
	
	 public void closeEntityManager() throws Exception{
		 EntityManager outer = PersistanceManager.getEntityManager();
	    if(outer == null){
	      return;
	    }

	    if(outer.getTransaction().isActive()){
	      endTransaction(false);
	    }
		  //outer.flush(); non serve perchè lo fa la commit
	      outer.close();
	      outer = null;
	      PersistanceManager.releaseEntityManager();
	}
	
	


	@Override
	/**
	 * Aggiorna lo stato su db con gli oggetti persistent,modificati
	 * se si vuole leggere il risultato prima che venga fatta la commit
	 * ad esempiuio se si chiama persit() che inserisce una nuova
	 * row nel db, e si vuole avere a disposizione il valore 
	 * per estrarre la chiave primaria autogenerante è sufficiente
	 * invocare questo metodo prima di effettuare una find o 
	 * find by example
	 * Clear svuota la cache rendendo gli oggetti detached, va quindi
	 * dopo flush.
	 * Il flush può essere fatto anche prima di chiudere una transazione (inutile dopo persist/save)
	 * per rendere disponibili e salvate parte delle modifiche su db,
	 * ma se si esegue poi un rollback anche queste vengono eliminate
	 * (?? da verificare..)
	 */
	public void flushAndClear() {
		 EntityManager outer = PersistanceManager.getEntityManager();
		    if(outer == null){
		      return;
		    }
		    outer.flush();//sincronizzo gli entity maanged/persistent al db (esegue insert, update su db)
		    outer.clear();	//svuota la cache 
	}

	/***
	 * passando una classe entità valorizzata con un set
	 * completo o parziale delle prorpiettà, ricerca 
	 * per queste se esistono uno o più row nel database
	 * e li ritorna
	 */
	@Override
	public List<T> findByExample(T example) {
		Map<String,Object> columnsNameMap = new LinkedHashMap<String,Object>();
		String name =null;
		//compongo i campi colonna con la reflection
		Field[] fields = example.getClass().getDeclaredFields();
		for (Field field: fields){
			//se è la colonna chiave primaria
			if (field.getName().equals(IEntity.P_ID)){
				continue;
			}
			//se non è una colonna di database (leggo le annotazioni)
			if (!field.isAnnotationPresent(Column.class) && (!field.isAnnotationPresent(Basic.class))){
				continue;
			}
			name = field.getName();
			field.setAccessible(true);
			Object val = null;
			try {
				val = field.get(example);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				//e.printStackTrace();
				continue;
			}
			if (val == null) {
				continue;
			}
			columnsNameMap.put(name,val);
		}//end loop
		//costruisce la query e la esegue
		return mapParamsToQuery(Qwrap.getEntityName(this.clazz),columnsNameMap);
	}
	
	/**
	 * crea le query e rende disponibile  metodi di utilità
	 * come impostare i parametri (preparedStatements)
	 * @param q
	 * @return
	 * @throws IllegalStateException
	 */
	public static Qwrap query(String q) throws IllegalStateException{
		EntityManager outer = PersistanceManager.getEntityManager();   
	    if(outer == null){
	      throw new IllegalStateException("Creating a query when there is no active transaction!");
	    }
	    
	    return new Qwrap(outer.createQuery(q));
	  }

	/**
	 * se l'oggetto è transient fa lo stesso di save or saveOrUpate (persist)
	 * rendendolo managed/attached/persistent e salvato sul db alla flush o commit.
	 * Se l'oggetto passato è detached aggiorna l'entity sul db (update) al
	 * flush o commit; 
	 * si usa di solito
	 * quando bisogna fare l'update di un oggetto detached
	 * (che era prima in stato persisted/managed/attached),
	 * oppure detto in altra maniera se si crea un nuovo oggetto con chiave primaria(id) già su db
	 * e si modificano alcune proprietà dello stesso che vanno ad aggiornare
	 * (update) quelle su db al sucessivo flush o commit.
	 * Fa una copia dell'oggetto entity passato e lo persiste,
	 * l'oggetto passato come parametro è e rimane detached 
	 * e ogni volta che lo si modifica va rifatto il merge.
	 * Il metodo invece restituisce l'oggetto persistente che rimane tale
	 * finchè non viene fatta flush+clear (clear svuota la cache di
	 * primo livello rendendo gli oggetti detached , 
	 * flush rende persistente su db le modifiche) o non viene
	 * fatto commit (che a sua volta fa flush e clear).
	 * Se l'oggetto è managed (persisted) non fa nulla finchè non 
	 * si chiude la transazione(commit) o si esegue il flush.
	 * @param entity l'oggetto (di solito detached)
	 * @return l'oggetto managed!
	 */
	public T merge(T entity){
		  EntityManager outer = PersistanceManager.getEntityManager();   
		    if(outer == null){
		      throw new IllegalStateException("update but there is no active transaction!");
		    }
		    if(entity == null){
			      throw new IllegalStateException("update but object entity is null");
			} 
		    return outer.merge(entity);
	  }


	 /** Mark the transaction as rollback only, if there is an active transaction to begin with. 
	   Utilizzare nelle Exception per fare invocare la rollback in caso
	   di errore
	   */
	  public void markRollback() throws IllegalStateException{
		 EntityManager outer = PersistanceManager.getEntityManager();
	    if(outer != null){
	      outer.getTransaction().setRollbackOnly();
	    }
	  }
	  
	  public boolean isRollbackOnly() throws IllegalStateException{
		EntityManager outer = PersistanceManager.getEntityManager();
	    return outer != null && outer.getTransaction().getRollbackOnly();
	  }

	  /**
	   * il commit o rollback viene fatto nella closeEntityManager
	   */
	  public void beginTransaction() throws IllegalStateException{
		  EntityManager outer = PersistanceManager.getEntityManager();
		  outer.getTransaction().begin();
	  }
	  
	  public boolean endTransaction(boolean close)throws Exception {
			 EntityManager outer = PersistanceManager.getEntityManager();
			 boolean result = false;
			    if(outer == null){
			      return result;
			    }
				 if(outer.getTransaction().isActive()){
				        
				        if(outer.getTransaction().getRollbackOnly()){
				          outer.getTransaction().rollback();
				          result = false;
				        } else {
				          outer.getTransaction().commit();
				          result = true;
				        }
				    }
				if (close){
					closeEntityManager(); 
				}
			 return result;
		}
	  
	  /**
	   * rilascia il threadlocal da chiamare solo quando si usa il persistence manager 
	   * in una select e si è sicuri che il thread termina
	   */
	  public void releasePersistenceEntityManager(){
		  PersistanceManager.releaseEntityManager();
	  }
	  
	  
	  @SuppressWarnings("unchecked")
	protected List<T> mapParamsToQuery(String entityname,
				Map<String, Object> columnsNameMap) {
			String sql = "Select x from " + entityname + " x "; 
			int i=0;
			for (String key : columnsNameMap.keySet()){
				if (i==0) {
					sql+=" where x." + key + "=:param" + String.valueOf(i);
				}
				else{
					sql+=" and x." + key + "=:param" + String.valueOf(i);
				}
				i++;
			}
			//System.out.println(sql);
			Qwrap qwrap = query(sql);
			int j = 0;
			for (Object value: columnsNameMap.values()){
				qwrap.par("param"+String.valueOf(j), value);
				j++; 
			}
			return (List<T>) qwrap.multiResult(this.clazz);
		}
}
