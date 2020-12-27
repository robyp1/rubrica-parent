package com.cadit.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cadit.daotest.TestDao;

@ManagedBean(name="TestMe")
@ViewScoped
public class TestMe {
	
	static final Logger log =  LoggerFactory.getLogger(TestMe.class);

	public void execute(){
		TestDao t = new TestDao();
		try {
			t.insertMioNumeroTest();
			t.selectFirst();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TestMe Session scoped backed bean error found! " + e.getMessage());
		}

	}
}
