/**
 * 
 */
package jpaex;

import java.util.LinkedList;
import java.util.List;

//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.persistence.EntityManager;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.postgresql.ds.PGConnectionPoolDataSource;
//
//import com.cadit.aspect.ITransactional;
//import com.cadit.dao.DaoGeneric;
//import com.cadit.dao.PersistanceManager;
//import com.cadit.data.entities.DataCollector;
//import com.cadit.ex.dao.Dba2;

/**
 * @author Roby
 *
 */
public class TestDBConnection {


//	private static DaoGeneric<DataCollector, String> dao = new DaoGeneric<DataCollector, String>((Class)DataCollector.class);
//	private static boolean opConn = true;
//	
//	public TestDBConnection() {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//		bind_jndi_datasource();        
//		opConn = true;
//	}
//
//	/**
//	 * Solo in un constesto web posso recuperare l'em dal contesto jndi
//	 * altrimenti come qui devo costruirlo esplicitamente
//	 * @throws NamingException
//	 */
//	private static void bind_jndi_datasource() throws NamingException {
//		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
//		System.setProperty(Context.URL_PKG_PREFIXES,  "org.apache.naming");
//		String dsname = "java:/comp/env/jdbc/TestappDS";
//	    String driver = "org.postgresql.Driver";
//        String schema = "test";
//        //String dbUri = "jdbc:postgresql://localhost:5432" + "/" + schema;
//        String username = "postgres";
//        String password = "p4ssw0rd";
//        PGConnectionPoolDataSource ds = new PGConnectionPoolDataSource();
//        ds.setUser(username);
//        ds.setPassword(password);
//        ds.setServerName("localhost");
//        ds.setPortNumber(5432);
//        ds.setDatabaseName("test");
//        
//        InitialContext ic = new InitialContext();
//        ic.createSubcontext("java:");
//        ic.createSubcontext("java:/comp");
//        ic.createSubcontext("java:/comp/env");
//        ic.createSubcontext("java:/comp/env/jdbc");
//        ic.bind("java:/comp/env/jdbc/TestappDS", ds);
//		
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//		if (opConn) dao.closeEntityManager();
//		opConn =false;
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//		List<String> keywords = new LinkedList();
//		if (opConn){
//			dao.beginTransaction();
//			keywords = dao.query("Select keyword from DataCollector").multiResult(String.class);
//			dao.closeEntityManager();
//		}
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	@Test
//	public void test() {
//		//fail("Not yet implemented");
//	}

}
