package com.cadit.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cadit.domain.IEntity;


/**
 * Esempio di entità riferimento alla tabella su database
 * costruita a mano o usare HibernateTools
 * @author Roby
 *
 */
@Entity
@Table(name="rubricacollector")
public class RubricaCollector implements IEntity<Long>{

	@Column(name="id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	@Column(name="Nome")
	String nome;
	@Column(name="Cognome")
	String cognome;
	@Column(name="TipoContatto")
	String tipoContatto;
	@Column(name="Numero")
	String numero;

	
	public RubricaCollector(){
		
	}


	public RubricaCollector( String nome, String cognome, String tipoContatto, String numero) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.tipoContatto = tipoContatto;
		this.numero = numero;
	}




	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCognome() {
		return cognome;
	}


	public void setCognome(String cognome) {
		this.cognome = cognome;
	}


	public String getTipoContatto() {
		return tipoContatto;
	}


	public void setTipoContatto(String tipoContatto) {
		this.tipoContatto = tipoContatto;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	@Override
	public Long getId() {
		return id;
	}


	@Override
	public void setId(Long id) {
		this.id=id;
	}


	@Override
	public String toString() {
		return "RubricaCollector [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", tipoContatto="
				+ tipoContatto + ", numero=" + numero + "]";
	}


	
	
}
