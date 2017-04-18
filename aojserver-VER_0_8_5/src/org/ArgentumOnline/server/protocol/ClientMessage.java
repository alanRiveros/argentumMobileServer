/**
 * ClientMessage.java
 * 
 * Created 03/feb/2007
 * 
    AOJava Server
    Copyright (C) 2003-2007 Pablo Fernando Lillia (alias Gorlok)
    Web site: http://www.aojava.com.ar
    
    This file is part of AOJava.

    AOJava is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    AOJava is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA 
 */
package org.ArgentumOnline.server.protocol;

/**
 * @author gorlok
 *
 */
public enum ClientMessage { 
	MSG_VAL		("VAL", ',', (byte) 105),  // Codigo de validacion
	MSG_CSI     ("CSI", ',', (byte) 109),  // Lista de items en inventario
	MSG_SHS     ("SHS", ',', (byte) 111),  // Lista de hechizos aprendidos
	MSG_UI      ("UI"),   // Indice del usuario
	MSG_TALK    ("||", (char)176), // Hablar: usa como separador al caracter �
	MSG_SYSMSG	("!!"),   // Mensaje glbal del sistema.
	MSG_EST     ("EST"),  // Estadisticas del usuario (mana, stamina, oro, etc.)
	MSG_EHYS    ("EHYS"), // Estadisticas de hambre y sed
	MSG_CM      ("CM", ',', (byte) 35),   // Cambiar de mapa
	MSG_TM      ("TM"),   // Musica del mapa
	MSG_TW		("TW"),	  // Sonido.
	MSG_CC      ("CC", ',', (byte) 68),   // Crear caracter.
	MSG_CP		("CP"),   // Enviar cambios del caracter.
	MSG_QDL		("QDL"),  // Quitar di�logo del caracter.
	MSG_QTDL	("QTDL"), // Quitar todos los di�golos.
	MSG_BP		("BP"),   // Borrar caracter.
	MSG_CFX     ("CFX", ',', (byte) 98),  // FX de usuario
	MSG_IP      ("IP"),   // Indice del usuario
	MSG_HO      ("HO"),   // Agregar objeto en mapa
	MSG_BO		("BO"),	  // Quitar objeto del mapa
	MSG_MP		("MP"),   // Mover caracter.
	MSG_FO		("FO"),   // Prender fogata.
	MSG_BQ      ("BQ"),   // Posicion bloqueda
	MSG_PU		("PU"),   // Posicion del usuario
	MSG_N2		("N2"),		// Usuario recibe da�o
	MSG_6		("6"),		// Usuario muere
	MSG_7		("7"),		// El escudo del usuario rechaz� el ataque.
	MSG_8		("8"),		// El golpe fue evitado por un escudo.
	MSG_U1		("U1"),		// El npc esquiv� el golpe.
	MSG_U2		("U2"),		// El npc fue da�ado por el golpe.
	MSG_U3		("U3"),		// El usuario evit� el golpe de otro usuario.
	MSG_N1		("N1"),		// Usuario esquiv� ataque del npc.
	MSG_N4		("N4"), 	// El usuario fue da�ado por otro.
	MSG_N5		("N5"),		// El usuario da�� a otro usuario.
	MSG_LOGGED  ("LOGGED", ',', (byte) 7), // Logged in ok
	MSG_SEGON   ("SEGON"),  // Activar seguro
	MSG_SEGOFF  ("SEGOFF"), // Desactivar seguro
	MSG_NOVER	("NOVER"),	// Invisibilidad
	MSG_LSTCRI	("LSTCRI"), // Enviar lista de criaturas del entrenador.
	MSG_NPCI	("NPCI"),	// Enviar inventario del npc.
	MSG_SELE    ("SELE"),
	MSG_PARADOK ("PARADOK"), // Paralizar
	MSG_DUMB	("DUMB"), 	 // Comienza estupidez del usuario. (En algunos casos puede ser permanente...)
	MSG_CEGU	("CEGU"),    // Comienza ceguera del usuario.
	MSG_NESTUP  ("NESTUP"),  // Fin de estupidez.
	MSG_NSEGUE  ("NSEGUE"),  // Fin de ceguera.
	MSG_LLU		("LLU"),     // Lluvia
	MSG_SUNI	("SUNI"), 	 // Subir nivel
	MSG_NENE	("NENE"), 	 // Numero de npcs en el mapa.
	MSG_FMSG	("FMSG", (char)176), // Enviar mensaje del foro.
	MSG_MFOR	("MFOR"),    // Fin de mensajes del foro.
	MSG_MCAR	("MCAR", (char)176), // Mensaje de cartel.
	MSG_BKW		("BKW"),	// World Save
	MSG_NAVEG	("NAVEG"),
	MSG_SPL		("SPL"),  // Enviar lista de spawn criatura para /CC
	MSG_ATR		("ATR"),  // Enviar atributos del usuario
	MSG_FAMA	("FAMA"), // Enviar reputaci�n del usuario
	MSG_MEST	("MEST"), // Enviar mini-estad�sticas del usuario.
	MSG_SHOWFUN ("SHOWFUN"), // Todos de joda, se arm� el clan.
	MSG_ZMOTD	("ZMOTD"),   // Cambiar el mensaje del d�a.
	MSG_LISTUSU ("LISTUSU"), // Lista de usuarios
	MSG_ABPANEL ("ABPANEL"), // Mostrar del GM.
	MSG_DOK		("DOK"),	// Descansar en fogata (comenzar o terminar)
	MSG_MEDOK	("MEDOK"),	// Comenzar a, o terminar de, meditar.
	MSG_ERR		("ERR", ',', (byte)120), 	// Error
	MSG_CHRINFO ("CHRINFO"), // Enviar informaci�n del personaje.
	MSG_T01		("T01"),	// Respuesta a la tecla U
	MSG_LAR		("LAR"),	// Env�a el listado de armadura construibles.
	MSG_OBR		("OBR"),	// Env�a el listado de objetos construibles.
	MSG_LAH		("LAH"),	// Env�a el listado de armas construibles.
	MSG_SFC		("SFC"),	// Usar serrucho.
	MSG_SFH		("SFH"),	// Herrer�a: construir con el yunque.
	MSG_RSOS	("RSOS"),   // Envia los pedidos de ayuda al GM
	MSG_MSOS	("MSOS"), 	// Fin de envio de pedidos de ayuda al GM
	MSG_SKILLS	("SKILLS"),	// Envia los skills del usuario
	MSG_DADOS	("DADOS"),  // Envia dados al cliente
	MSG_INITBANCO ("INITBANCO"), // Inicia operaci�n con el banco (cuidado con el corralito y el corral�n...)
	MSG_FINBANOK  ("FINBANOK"),  // Fin de banco.
	MSG_BANCOOK	  ("BANCOOK"),   // Actualiza ventana banco.
	MSG_SBO		 ("SBO"),	   // Envia objeto banco
	MSG_INITCOM  ("INITCOM"),  // Comienza comercio con npc.
	MSG_TRANSOK	 ("TRANSOK"),  // Actualiza ventana de comercio.
	MSG_FINCOMOK ("FINCOMOK"), // Fin de comercio con npc.
	MSG_COMUSUINV   ("COMUSUINV"),   // Comenzar comercio con usuario, enviando objeto.
	MSG_FINCOMUSUOK ("FINCOMUSUOK"), // Fin de comercio con usuario.
	MSG_LEADERI	("LEADERI"), // Envia info para el lider del clan.
	MSG_GUILDNE ("GUILDNE"), // Envia las noticias del clan.
	MSG_GL		("GL"),		 // Envia la lista de clanes.
	MSG_CLANDET	("CLANDET"), // Envia detalles sobre el clan.
	MSG_PEACEDE	("PEACEDE"), // Clanes: envia pedido de paz.
	MSG_PEACEPR ("PEACEPR"), // Clanes: envia la lista de proposiciones de paz.
	MSG_PETICIO ("PETICIO"), // Clanes: envia petici�n al clan (de ingreso).
	MSG_FINOK	("FINOK");   // Fin de la sesi�n de juego.
	
	
	private final String strCode;
	private final char sep;
	private final byte binCode; 
	
	ClientMessage(String code) {
		this.strCode = code;
		this.sep = ',';
		this.binCode = 0;
	}
	ClientMessage(String code, char sep) {
		this.strCode = code;
		this.sep = sep;
		this.binCode = 0;
	}
	
	ClientMessage(String code, char sep, byte bin) {
		this.strCode = code;
		this.sep = sep;
		this.binCode = bin;
	}
	
	public String strMessage() { return this.strCode; }
	public char separator() { return this.sep; }
	public byte binCode() { return this.binCode; }
	
}

