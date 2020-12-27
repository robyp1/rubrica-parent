package com.cadit.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;


@ManagedBean(name="tipoContattoService")
public class ServiceTipoContatto implements Serializable{

	
	public List<TipoContatti> getTipoContatti(){
		TipoContatti[] v = TipoContatti.values();
		return  Arrays.asList(v);
	}
	
//	public static void main(String[] args){
//		List<TipoContatti> tcontatti = new ServiceTipoContatto().getTipoContatti();
//		for (TipoContatti tc : tcontatti){
//			System.out.println(tc.getTipo());
//		}
//	}
}
