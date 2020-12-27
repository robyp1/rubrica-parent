package com.cadit.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.cadit.dao.DaoUno;
import com.cadit.data.entities.RubricaCollector;
import com.cadit.util.ServiceContatti;
import com.cadit.util.ServiceTipoContatto;
import com.cadit.util.TipoContatti;

@ManagedBean(name="insertBean")
public class InsertBean {

	RubricaCollector contact = new RubricaCollector();

	private List<RubricaCollector> contatti;
	private Map<String,String> tipocontatti = new HashMap<String, String>();
	
	@ManagedProperty("#{contattiService}")
	private ServiceContatti cservice;
	@ManagedProperty("#{tipoContattoService}")
	private ServiceTipoContatto tservice;

	public List<RubricaCollector> getContatti() {
		return contatti;
	}
	

	//viene eseguito dopo l'instanziamento della classe
	@PostConstruct
	public void init(){
		contatti = cservice.getListaContatti();
		List<TipoContatti> l = tservice.getTipoContatti();
		for ( TipoContatti tc : l){
			tipocontatti.put(tc.getTipo(), tc.getTipo());
		}
	}


	public void setCservice(ServiceContatti cservice) {
		this.cservice = cservice;
	}


	public void setTservice(ServiceTipoContatto tservice) {
		this.tservice = tservice;
	}
	
	public RubricaCollector getContact() {
		return contact;
	}


	public Map<String, String> getTipocontatti() {
		return tipocontatti;
	}


	public void setContact(RubricaCollector contact) {
		this.contact = contact;
	}



	public void update(){
		DaoUno d = new DaoUno((Class)RubricaCollector.class);
		d.beginTransaction();
		//insert
		//RubricaCollector persistentObj = d.saveAndFlushPK(res);
		RubricaCollector persistentObj = d.merge(contact);//inserisce se nuovo oppure aggiorna
		System.out.println("Updated: " + persistentObj);
		try {
			d.closeEntityManager();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucess: Inserimento avvenuto con successo"));
			
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed: Si è verificato un errore"));
		}finally{
			contact = null;
		}
		
	}
	
	public void save(){
		DaoUno d = new DaoUno((Class)RubricaCollector.class);
		d.beginTransaction();
		//insert
		RubricaCollector persistentObj = d.saveAndFlushPK(contact);
		System.out.println("Saved: " + persistentObj);
		try {
			d.closeEntityManager();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucess: Inserimento avvenuto con successo"));
			
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed: Si è verificato un errore"));
		}finally{
			contact = null;
		}
		
	}
	
	public void delete(){
		DaoUno d = new DaoUno((Class)RubricaCollector.class);
		d.beginTransaction();
		boolean result = d.delete(contact.getId());
		if (result) { 
			System.out.println("Delete: " + contact);
			}
		else {  
			System.out.println("Can't delete: " + contact);
			}
		try {
			d.closeEntityManager();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucess: Inserimento avvenuto con successo"));
			
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed: Si è verificato un errore"));
		}finally{
			contact = null;
		}
		
	}
}
