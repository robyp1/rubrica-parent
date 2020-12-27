package com.cadit.util;

import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import com.cadit.dao.DaoUno;
import com.cadit.data.entities.RubricaCollector;

@ManagedBean(name="contattiService")
public class ServiceContatti {

	
	public List<RubricaCollector> getListaContatti(){
		DaoUno d = new DaoUno((Class)RubricaCollector.class);	
		try{
			List<RubricaCollector> result = d.findAll();
			if (result==null) result = new LinkedList<>();
			return result;
		}finally{
			try {
				d.closeEntityManager();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
