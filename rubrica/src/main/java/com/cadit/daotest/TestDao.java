package com.cadit.daotest;

import java.util.List;
import java.util.ListIterator;


import com.cadit.dao.DaoUno;
import com.cadit.data.entities.RubricaCollector;
//import com.cadit.data.entities.DataCollector;
import com.cadit.domain.IDao;

/**
 * se stessa transazione T1 legge dalla cache
 * se altra transazione T2, vede le modifiche di T1 solo quando T1 committa
 * perchè l'entity manager è per thread e la cache di 1 liv è 
 * per transazione. per avere una cache globale, indipendente 
 * dalle transazioni e in share tra i vari entity manager introdurre
 * una cache di livello 2 tipo ehcache o jboss cache..
 * @author Roby
 *
 */
public class TestDao {

public void insertMioNumeroTest() throws Exception{
	DaoUno d = new DaoUno((Class)RubricaCollector.class);
	RubricaCollector res = new RubricaCollector("Roberto","Portoni","Cellulare","3495892644");
	d.beginTransaction();
	//insert
	RubricaCollector persistentObj = d.saveAndFlushPK(res);
	System.out.println(persistentObj);
	//update
	//persistentObj.setCount(300);
	d.closeEntityManager();
}

public void selectFirst() throws Exception{
	DaoUno d = new DaoUno((Class)RubricaCollector.class);	
	RubricaCollector result = d.findAll().get(0);
	System.out.println(result.getId() + "." + result.getCognome() + " "  + result.getNome() + " - tipo contatto " + result.getTipoContatto() + " -> " + result.getNumero() );
	d.closeEntityManager();
}	
	

//	public static String testquery(){
//		DaoUno d = new DaoUno((Class)DataCollector.class);
//		DataCollector res = d.query("Select p from DataCollector p where p.id =:param ").par("param", "casa").singleResult(com.cadit.data.entities.DataCollector.class);
//		//d.releasePersistenceEntityManager();
//		if (res!=null)
//			return res.getId() + " : " + res.getCount();
//		else return "";
//	}
//	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static void insertMondo100(IDao dao) throws Exception{
//		// Itransactional inserisce begin e commit prima della invocazione del metodo (vedere jpaex-annotation) con AspectJ 
//		//il dao è commentato perchè va passato in input al metodo
//		//DaoUno dao = new DaoUno((Class)DataCollector.class);
//		DataCollector res = new DataCollector("mondo", 100);
//		DaoUno d = (DaoUno)dao;
//		//dao.beginTransaction();
//		dao.saveOrUpdate(res, "mondo");
//		//dao.closeEntityManager();
//	}
//	
//	public static String testqueryDue(String keyword){
//		DaoUno d = new DaoUno((Class)DataCollector.class);
//		DataCollector res = d.query("Select p from DataCollector p where p.id =:param ").par("param", keyword).singleResult(com.cadit.data.entities.DataCollector.class);
//		d.releasePersistenceEntityManager();
//		return res.getId() + " : " + res.getCount();
//	}
//	
//	//@ITransactional - se si abilita il Dao va passato al metodo in input, come il primo metodo sopra
//	public static void insertCavolo200Update300() throws Exception{
//		DaoUno d = new DaoUno((Class)DataCollector.class);
//		DataCollector res = new DataCollector("cavolo2", 200);
//		d.beginTransaction();
//		//insert
//		DataCollector persistentObj = d.saveOrUpdate(res, "cavolo2");
//		System.out.println(persistentObj);
//		//update
//		persistentObj.setCount(300);
//		d.closeEntityManager();
//	}
//	
//	//@ITransactional - se si abilita il Dao va passato al metodo in input, come il primo metodo sopra
//	public static void insertCavolo300UpdateNot400() throws Exception{
//		DaoUno d = new DaoUno((Class)DataCollector.class);
//		DataCollector res = new DataCollector("cavolo2", 400);
//		d.beginTransaction();
//		//prelevo da db l'ultimo stato salvato, poi modifico l'entity
//		//d.refresh(res);
//		//res.setCount(400);
//		//update
//		DataCollector perObj = d.merge(res); //update2
//		System.out.println(perObj);
//		System.out.println(res);
//		//no update
//		res.setCount(500); //questo non viene aggiornato
//		d.closeEntityManager();
//	}
//	
//	public static String findCountingEqual(int howmany){
//		DaoUno d = new DaoUno((Class)DataCollector.class);
//		DataCollector res = new DataCollector("", howmany);
//		System.out.println("Searching word with count = " + howmany);
//		List<DataCollector> ris = d.findByExample(res);
//		ListIterator<DataCollector> it = ris.listIterator();
//		StringBuilder sb= new StringBuilder();
//		int i=0;
//		while (it.hasNext()){
//			DataCollector out = it.next();
//			i++;
//			sb.append("keyword: " + out.getId() + ", count: " + out.getCount() + "<br/>" );
//		}
//		sb.append("<br/>").append("Total rows: " + i);
//		return sb.toString();
//	}
//	
//	
//	//per index2
//	//test completo con refresh
//	public static void insertWorl10(IDao dao) throws Exception{
//		//DaoUno d = new DaoUno((Class)DataCollector.class);
//		DataCollector res = new DataCollector("world", 10); 
//		//d.beginTransaction();
//		//insert: res persist e va subito su db (perchè siamo nella 
//		//stessa transazione, stesso em, stessa cache L1)
//		//, non serve quindi flush or flushAndclear(), 
//		//solo con commit una altra transazione ha acesso, se errore rollback torno indietro		
//		DaoUno d = (DaoUno) dao; //cast a daouno,perchè i metodi specializzati e alcuni di dao generic non sono in interfaccia IDao
//		d.saveOrUpdate(res, "world");
//		DataCollector res2 = d.query("Select p from DataCollector p where p.id =:param ").par("param", "world").singleResult(com.cadit.data.entities.DataCollector.class);
//		//restituisce quindi già world:10, ma l'oggetto res2 restituito è detached
//		System.out.println("2. " + res2.getId() + ":"  +res2.getCount() );
//		//legge dal db e restituisce l'entità managed/persisted
//		DataCollector res3 = (DataCollector) d.findById("world");
//		System.out.println("3. " + res3.getId() + ":"  +res3.getCount() );
//		res.setCount(20);
//		//world:20 managed/persisted/attach
//		System.out.println("4. " + res.getId() + ":"  +res.getCount() );
//		//world:10 detached
//		System.out.println("5. " + res2.getId() + ":"  +res2.getCount() );
//		//world:20 managed/persisted/attach
//		System.out.println("6. " + res3.getId() + ":"  +res3.getCount() );
//		//siccome res2 è detached e non managed lo aggiorno con refresh e lo modifico
//		d.refresh(res2);//ora res2 è :20, questo diventa persistent, agg alla ultima commit o versione nella cache L1
//		res2.setCount(30);
//		//su db cosa andrà ora?
//		DataCollector res4 = d.query("Select p from DataCollector p where p.id =:param ").par("param", "world").singleResult(com.cadit.data.entities.DataCollector.class);
//		//rimasto world:20, ma attenzione che è ancora persistent, ogni sua modifica va su db 
//		System.out.println("7. " + res.getId() + ":"  +res.getCount() );
//		//diventato world:30, managed\persisted, alla commit viene salvato su db
//		System.out.println("8. " + res2.getId() + ":"  +res2.getCount() );
//		//rimasto world:20, ma attenzione che è ancora persistent, ogni sua modifica va su db
//		System.out.println("9. " + res3.getId() + ":"  +res3.getCount() );
//		//su db va a world:30
//		System.out.println("10. " + res4.getId() + ":"  +res4.getCount() );
//		res.setCount(40);// res era ancora persistent quindi sovrascrive res2
//		System.out.println("11. " + res.getId() + ":"  +res.getCount() + ", ma era persistent!" );
//		//d.closeEntityManager();//commit+close
//		//se chiamo testqueryDue("world") ottengo world:40 e non :30
//	}
//	
//
//	/**
//	 * chiamare quando finisce la Jsp e si usa un metodo ITRansactional,toglie il threadLocal dell'entitymanager dopo la commit
//	 * che viene fatta quando si esce dal metodo annotato(tramit AOP): altrimenti la tansazione non viene chiusa.
//	 * infatti se prima di fare commit si dovessse chiamare closeEntityManager(che a sua volta
//	 * chiama anche releasePersistenceEntityManager) , quando AOP
//	 * fa commit non trova più nel threadlocal l'entitymanagere e quindi riapre un nuovo entitymanager che NON HA
//	 * PERO' ALCUNA TRANSAZIONE, MENTRE QUELLA DI PRIMA RIMANE ATTIVA E LOCCA IL DB
//	 * @param d
//	 */
//	public static void releaseEM(DaoUno d){
//		d.releasePersistenceEntityManager();
//	}
}
