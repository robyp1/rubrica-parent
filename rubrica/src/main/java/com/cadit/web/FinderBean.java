package com.cadit.web;

import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.apache.derby.impl.store.raw.data.CopyRowsOperation;

import com.cadit.data.entities.RubricaCollector;
import com.cadit.util.ServiceRicercaNumero;
import com.cadit.util.ServiceTipoContatto;

@ManagedBean(name="finderBean")
public class FinderBean {

	@ManagedProperty("#{ricercaNumeroService}")
	private ServiceRicercaNumero rcService;
	
	public String numero="";
	
	public String contattoNome="";
	public String contattoCognome="";

	private List<RubricaCollector> contatti=new LinkedList<>();
	
	public List<String> getCompleteNumero(String query){
		List<RubricaCollector> l = rcService.getListaNumeri(query);
		List<String> numeri = new LinkedList<>();
		if (l!=null && l.size()>0){
			for (RubricaCollector d : l){
				numeri.add(d.getNumero());
			}
		}
		return numeri;
	}
	
	
	//Per la riceerca
	//TO DO : da agganciare
	public void findContatto()
	{

		this.contatti= rcService.find(contattoNome, contattoCognome, numero  );
	}

	
	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public ServiceRicercaNumero getRcService() {
		return rcService;
	}

	public void setRcService(ServiceRicercaNumero rcService) {
		this.rcService = rcService;
	}



	public String getContattoNome() {
		return contattoNome;
	}


	public void setContattoNome(String contattoNome) {
		this.contattoNome = contattoNome;
	}


	public String getContattoCognome() {
		return contattoCognome;
	}


	public void setContattoCognome(String contattoCognome) {
		this.contattoCognome = contattoCognome;
	}


	public List<RubricaCollector> getContatti() {
		return contatti;
	}
	

	
}
