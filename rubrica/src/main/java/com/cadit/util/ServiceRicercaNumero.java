package com.cadit.util;

import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import com.cadit.dao.DaoUno;
import com.cadit.data.entities.RubricaCollector;

@ManagedBean(name="ricercaNumeroService")
public class ServiceRicercaNumero {

	
	public List<RubricaCollector> getListaNumeri(String example){
		DaoUno d = new DaoUno((Class)RubricaCollector.class);	
		try{
			List<RubricaCollector> result = d.findByExampleLikeNumero(example);
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
	
	public List<RubricaCollector> find(String nome, String cognome, String numero){
		DaoUno d = new DaoUno((Class)RubricaCollector.class);
		List<RubricaCollector> result = new LinkedList<>();
		try{
			if ( (nome!=null && !nome.isEmpty()) || (cognome!=null && !cognome.isEmpty())){
				 result = d.findByExampleLikeNomeCognome(nome,cognome, numero);
			}
			else if (numero != null && !numero.isEmpty()){
				 result = d.findByExampleLikeNumero(numero);
			}
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
