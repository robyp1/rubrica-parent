package com.cadit.util;

public enum TipoContatti {
	
	CELLULARE_PERSONALE("Cellulare personale"),
	TELEFONO_ABITAZIONE("Telefono abitazione"),
	TELEFONO_UFFICIO("Telefono ufficio"),
	CELLULARE_UFFICIO("Cellulare ufficio"),
	ALTRO("Altro");
	
	private String tipo;

	TipoContatti(String _tipo){
		tipo = _tipo; 
	}

	public String getTipo() {
		return tipo;
	}


}


