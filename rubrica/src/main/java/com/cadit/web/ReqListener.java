package com.cadit.web;

import java.io.PrintWriter;
import java.net.InetAddress;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.derby.drda.NetworkServerControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class ReqListener implements ServletContextListener {
	
	static final Logger log =  LoggerFactory.getLogger(ReqListener.class);
	PrintWriter pw = new PrintWriter(System.out);
	private NetworkServerControl derbyserver;
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			String userHomeDir = System.getProperty("user.home", ".");
		    String systemDir = userHomeDir + "/.rubrica";
		    // Set the db system directory and startup Server Derby for incoming connections.
		    System.setProperty("derby.system.home", systemDir);//il db viene salvato qui

			derbyserver = 	new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
			derbyserver.start(pw);
			log.info("Apache derby settings ok");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (derbyserver!=null){
			try {
				log.info("Apache derby db server stopping..");
				derbyserver.shutdown();
			} catch (Exception e) {
			   log.error(e.getMessage());
				e.printStackTrace();
			}
		}

	}
	
	//Test
//	public static void main(String[]args) throws Exception{
//		String userHomeDir = System.getProperty("user.home", ".");
//	    String systemDir = userHomeDir + "/.rubrica";
//	    // Set the db system directory.
//	    System.setProperty("derby.system.home", systemDir);
//		NetworkServerControl derbyserver_ = new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
//		derbyserver_.start(new PrintWriter(System.out));
//		//Thread.sleep(10000);
//		//derbyserver_.shutdown();
//		while (true){Thread.sleep(10000);}
//	}

}
