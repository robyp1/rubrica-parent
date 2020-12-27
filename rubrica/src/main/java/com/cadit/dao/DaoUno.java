package com.cadit.dao;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import com.cadit.data.entities.RubricaCollector;
import com.cadit.domain.IEntity;
import com.cadit.util.Qwrap;

public class DaoUno extends DaoGeneric<RubricaCollector,Long> {

	public DaoUno(Class<IEntity<Long>> clazzs) {
		super(clazzs);
	}
	
	//definisci ulteriori metodi specializzati solo per questo dao...
	
	/**
	 * ricerca per chiave primaria
	 * @param id
	 * @param lock tipo di lock: ottimistico o pessimistico ...
	 * @return
	 */
	public RubricaCollector findById(Long id, LockModeType lock) {
		if (lock==null){
			lock= LockModeType.NONE; //default jpa, ovvero READ COMMITTED
		}
		EntityManager outer = PersistanceManager.getEntityManager();
		if (outer == null)
			 return null;
		return (RubricaCollector) outer.find(this.clazz, id,lock);
	}
	
	public List<RubricaCollector> findByExampleLikeNumero(String numero){
		String param = '%' + numero + '%';
		//costruisce la query e la esegue
		String sql = "Select x from " + Qwrap.getEntityName(this.clazz)  + " x "; 
		sql+=" where x.numero LIKE :param";
		Qwrap qwrap = query(sql);
		qwrap.par("param", param);
		return qwrap.multiResult(RubricaCollector.class);
	}

	public List<RubricaCollector> findByExampleLikeNomeCognome(String nome, String cognome, String numero) {
		String param1 = (!nome.isEmpty()) ? '%' + nome + '%': null;
		String param2 = (!cognome.isEmpty()) ? '%' + cognome + '%' : null;
		//costruisce la query e la esegue
		String sql = "Select x from " + Qwrap.getEntityName(this.clazz)  + " x "; 
		String sqlwhere = "";
		if (param1 != null) sqlwhere+= " where x.nome LIKE :param1";
		if (param1 != null && param2!=null) sqlwhere+=" and x.cognome LIKE :param2";
		else if (param1 == null && param2!= null) sqlwhere+=" where x.cognome LIKE :param2"; 
		Qwrap qwrap = query(sql + " " + sqlwhere);
		if (param1!=null) qwrap.par("param1", param1);
		if (param2!=null) qwrap.par("param2", param2);
		return qwrap.multiResult(RubricaCollector.class);
	}
}
