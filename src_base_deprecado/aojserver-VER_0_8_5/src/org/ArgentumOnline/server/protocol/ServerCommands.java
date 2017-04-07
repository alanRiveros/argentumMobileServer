/**
 * ServerCommands.java
 * 
 * Created on 26/12/2004
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
 * @author gorlok lillia
 *
 * Server commands definitions.
 */
public interface ServerCommands {

    /** <pre>
     * Comando gIvEmEvAlcOde
     * Descripcion: devuelve el valor usado para el CRC.
     * Ejemplo:
     * 	Cmd:	gIvEmEvAlcOde
     * 	Rta:	VAL345,343
     * </pre>
     */
    public final static String cmdGIVEMEVALCODE = "GIVEMEVALCODE";
    
    /** <pre>
     * Comando OLOGIN
     * Descripcion: intenta hacer el login del usuario.
     * Parametros: nombre del usuario, clave (md5), version,
     * Cantidad: 4
     * Separador: coma (,)
     * Ejemplo:
     * 	Cmd:    OLOGINpablo,7e4b64eb65e34fdfad79e623c44abd94,0.9.9,136######~CRC
     * 	Rta:	CSI1,460,Daga (Newbies),1,1,510,2,3,1,0,6
     *        	CSI2,467,Manzana Roja (Newbies),100,0,506,1,0,0,0,0
     * 		CSI3,468,Botella de agua (Newbies),100,0,537,13,0,0,0,0
     * 		CSI4,38,Pocion roja,100,0,542,11,0,0,0,6
     * 		CSI5,0,(None),0,0
     * 		...
     * 		CSI20,0,(None),0,0
     * 		SHS1,0,(None)
     * 		SHS2,0,(None)
     * 		...
     * 		SHS35,0,(None)
     * 		UI2
     * 		||*** Bienvenido a Argentun Online ***~89~43~213~1~0
     * 		EST100,30,100,90,100,90,100000,1,300,0
     * 		EHYS100,60,100,100
     * 		CM1,0
     * 		TM13-1
     * 		CC52,301,3,1884,50,50,2,2,0,999,2,pablo,0,0
     * 		CFX1884,1,0
     * 		IP1884
     * 		HO7002,15,12
     * 		HO7002,22,13
     * 		...
     * 		CC13,0,3,1,87,7
     * 		CC13,0,3,2,62,9
     * 		...
     * 		BQ41,31,1
     * 		BQ42,31,1
     * 		...
     * 		LOGGED
     * </pre>
     */
    public final static String cmdOLOGIN = "OLOGIN";
    
    /** <pre>
     * Comando NLOGIN
     * Descripcion: solicita crear un nuevo personaje
     * Parametros: 
     * Cantidad: 
     * Separador: 
     * Ejemplo:
     * </pre>
     */
    public final static String cmdNLOGIN = "NLOGIN";

    /** <pre>
     * Comando BORR
     * Descripcion: borra un personaje existente.
     * Parametros: 
     * Cantidad: 
     * Separador: 
     * Ejemplo:
     * </pre>
     */
    public final static String cmdBORR = "BORR";

    /** <pre>
     * Comando TIRDAD
     * Descripcion: 
     * Parametros: 
     * Cantidad: 
     * Separador: 
     * Ejemplo:
     * </pre>
     */
    public final static String cmdTIRDAD = "TIRDAD";

    /** <pre>
     * Comando /SALIR
     * Descripcion: desconecta una conexión.
     * Ejemplo:
     * 	Cmd:	/SALIR
     *  Rta:    FINOK
     * </pre>
     */
    public final static String cmdSALIR = "/SALIR";
    
    /** <pre>
     * Comando M
     * Descripcion: mover hacia el norte.
     * Parametro: direccion
     * Ejemplo:
     * 	Cmd:	M1
     * </pre>
     */
    public final static String cmdM = "M";
    
    /** <pre>
     * Comando RPU
     * Descripcion: Pedido de actualizacion de la posicion. Ver ACTUALIZAR.
     * Ejemplo:
     * 	Cmd:	RPU
     *  Rta:    PU50,50
     * </pre>
     */
    public final static String cmdRPU = "RPU";
    
    /** <pre>
     * Comando AT
     * Descripcion: comando para atacar.
     * Ejemplo:
     * 	Cmd:	AT
     * </pre>
     */
    public final static String cmdAT = "AT";
    
    /** <pre>
     * Comando SEG
     * Descripcion: comando para cambiar el estado del seguro.
     * Ejemplo:
     * 	Cmd:	SEG
     * </pre>
     */
    public final static String cmdSEG = "SEG";
    
    /** <pre>
     * Comando TAB
     * Descripcion: comando para entrar o salir del modo combate.
     * Ejemplo:
     * 	Cmd:	TAB
     * </pre>
     */
    public final static String cmdTAB = "TAB";
    
    /** <pre>
     * Comando /ACTUALIZAR
     * Descripcion: Pedido de actualizacion de la posicion. Ver RPU.
     * Ejemplo:
     * 	Cmd:	/ACTUALIZAR
     *  Rta:    PU50,50
     * </pre>
     */
    public final static String cmdACTUALIZAR = "/ACTUALIZAR";
    
    /** <pre>
     * Comando CHEA
     * Descripcion: cambiar direccion.
     * Parametro: direccion
     * Ejemplo:
     * 	Cmd:	CHEA1
     * </pre>
     */
    public final static String cmdCHEA = "CHEA";
    
    /** <pre>
     * Comando LC   ("left clic" o "leer celda")
     * Descripcion: leer el contenido de una posicion (clic boton izquierdo).
     * Parametros: posicion de la celda, (x,y).
     * Cantidad: 2
     * Separador: coma (,)
     *
     * Ejemplo:
     * 	Cmd:	LC50,50
     *  Rta:    ||
     * </pre>
     */
    public final static String cmdLC = "LC";
    
    /** <pre>
     * Comando RC   ("right clic" o "botón secundario")
     * Descripcion: abrir o cerrar puertas (clic boton derecho).
     * Parametros: posicion de la celda, (x,y).
     * Cantidad: 2
     * Separador: coma (,)
     *
     * Ejemplo 1: abrir puerta
     *	Cmd:    RC65,42~CRC
     *  Rta:
     *  	BQ65,42,0
     *  	BQ64,42,0
     *  	HO5592,65,42
     *
     * Ejemplo 2: cerrar puerta
     *	Cmd:    RC65,42~CRC
     *  Rta:
     *  	BQ65,42,1
     *  	BQ64,42,1
     *  	HO5593,65,42
     * </pre>
     */
    public final static String cmdRC = "RC";
    
    /** <pre>
     * Comando ;
     * Descripcion: hablar libremente.
     * Parametros: mensaje.
     * Cantidad: 1
     * Ejemplo:
     * 	Cmd:	;Hola a todos!
     * </pre>
     */
    public final static String cmdHablar = ";";
    
    /** <pre>
     * Comando -
     * Descripcion: gritar.
     * Parametros: mensaje.
     * Cantidad: 1
     * Ejemplo:
     * 	Cmd:	-Ahora conocerán mi FURIA!!!
     * </pre>
     */
    public final static String cmdGritar = "-";
    
    /** <pre>
     * Comando \
     * Descripcion: susurrar al oido de alguien.
     * Parametros: el usuario y el mensaje.
     * Cantidad: 2
     * Separador: espacio
     * Ejemplo:
     * 	Cmd:	\Usuario Te cuento un secreto...
     * </pre>
     */
    public final static String cmdSusurrar = "\\";
    
    /** <pre>
     * Comando /ONLINE
     * Descripcion: muestra los usuarios conectados.
     * Ejemplo:
     * 	Cmd:	/ONLINE
     * </pre>
     */
    public final static String cmdONLINE = "/ONLINE";
    
    /** <pre>
     * Comando /MOTD
     * Descripcion: muestra los mensajes del dia.
     * Ejemplo:
     * 	Cmd:	/MOTD
     * </pre>
     */
    public final static String cmdMOTD = "/MOTD";
    
    /** <pre>
     * Comando SKSE
     * Descripcion: Subir skills
     * Ejemplo:
     * 	Cmd:	SKSE1,0,0,2,3,0,3,0,0,0,0,1,0,0,0,0,0,0,0,0,0,~10968
     * </pre>
     */
    public final static String cmdSKSE = "SKSE";
    
    /** <pre>
     * Comando /DESC 
     * Descripcion: cambia la descripción del usuario.
     * Ejemplo:
     * 	Cmd:	/DESC soy el más capito :-)
     * </pre>
     */
    public final static String cmdDESC = "/DESC ";

    /** <pre>
     * Comando AG
     * Descripcion: agarrar o recoger algo.
     * Ejemplo:
     * 	Cmd:	AG~CRC
     * Rta:
	BO30,74
	CSI1,460,Daga (Newbies),1,1,510,2,3,1,0,6
	...
	CSI20,0,(None),0,0
     * </pre>
     */
    public final static String cmdAG = "AG";
    
    /** <pre>
     * Comando TI
     * Descripcion: tirar algo del inventario.
     * Parametros: numero de slot, y cantidad de objetos a tirar.
     * Separador: ,
     * Ejemplo:
     *  Cmd:    TI1,1
     * </pre>
     */
    public final static String cmdTI = "TI";
    
    /** <pre>
     * Comando EQUI
     * Descripcion: equipar un objeto del inventario.
     * Ejemplo:
     * </pre>
     */
    public final static String cmdEQUI = "EQUI";

    /** <pre>
     * Comando LH
     * Descripcion: lanzar hechizo.
     * Parametros: numero de slot del hechizo.
     * Separador: ,
     * Ejemplo:
     *  Cmd:    LH1
     * </pre>
     */
    public final static String cmdLH = "LH";
    
    /** <pre>
     * Comando WLC
     * Descripcion: clic izquierdo en modo trabajo.
     * Parametros: 
     * Separador: ,
     * Ejemplo:
     *  Cmd:    WLC
     * </pre>
     */
    public final static String cmdWLC = "WLC";
    
    /** <pre>
     * Comando UK
     * Descripcion: 
     * Ejemplo:
     *  Cmd:    UK
     * </pre>
     */
    public final static String cmdUK = "UK";
    
    /** <pre>
     * Comando /CURAR
     * Descripcion: curar al usuario.
     * Ejemplo:
     * 	Cmd:	/CURAR
     * </pre>
     */
    public final static String cmdCURAR = "/CURAR";

    /** <pre>
     * Comando /RESUCITAR
     * Descripcion: revivir al usuario muerto.
     * Ejemplo:
     * 	Cmd:	/RESUCITAR
     * </pre>
     */
    public final static String cmdRESUCITAR = "/RESUCITAR";
    
    /** <pre>
     * Comando /MEDITAR
     * Descripcion: meditar para recuperar mana.
     * Ejemplo:
     * 	Cmd:	/MEDITAR
     * </pre>
     */
    public final static String cmdMEDITAR = "/MEDITAR";
    
    /** <pre>
     * Comando /INFORMACION
     * Descripcion: 
     * Ejemplo:
     * 	Cmd:	/INFORMACION
     * </pre>
     */
    public final static String cmdINFORMACION = "/INFORMACION";
    
    /** <pre>
     * Comando /BALANCE
     * Descripcion: devuelve el balance bancario.
     * Ejemplo:
     * 	Cmd:	/BALANCE
     * </pre>
     */
    public final static String cmdBALANCE = "/BALANCE";
    
    /** <pre>
     * Comando /PASSWD 
     * Descripcion: cambiar la password de acceso.
     * Ejemplo:
     * 	Cmd:	/PASSWD afkljasf081048021
     * </pre>
     */
    public final static String cmdPASSWD = "/PASSWD ";
    
    /** <pre>
     * Comando USA
     * Descripcion: usar un item del inventario.
     * </pre>
     */
    public final static String cmdUSA = "USA";

    /** <pre>
     * Comando INFS
     * Descripcion: informacion del hechizo.
     * Parametros: número del slot del hechizo.
     * Ejemplo:
     *      INFS1
     * </pre>
     */
    public final static String cmdINFS = "INFS";
    
    /** <pre>
     * Comando /EST
     * Descripcion: enviar estadísticas del usuario.
     * Ejemplo:
     *      /EST
     * </pre>
     */
    public final static String cmdEST = "/EST";

    /** <pre>
     * Comando ATRI
     * Descripcion: enviar atributos del usuario.
     * Ejemplo:
     *      ATRI
     * </pre>
     */
    public final static String cmdATRI = "ATRI";

    /** <pre>
     * Comando FAMA
     * Descripcion: enviar la fama del usuario.
     * Ejemplo:
     *      FAMA
     * </pre>
     */
    public final static String cmdFAMA = "FAMA";

    /** <pre>
     * Comando ESKI
     * Descripcion: enviar los skills del usuario.
     * Ejemplo:
     *      ESKI
     * </pre>
     */
    public final static String cmdESKI = "ESKI";

    /** <pre>
     * Comando /DESCANSAR
     * Descripcion: descansar junto a una fogata.
     * Ejemplo:
     *      /DESCANSAR
     * </pre>
     */
    public final static String cmdDESCANSAR = "/DESCANSAR";

    /** <pre>
     * Comando /ENTRENAR
     * Descripcion: solicitar entrenar con una mascota.
     * Ejemplo:
     *      /ENTRENAR
     * </pre>
     */
    public final static String cmdENTRENAR = "/ENTRENAR";
    
    /** <pre>
     * Comando /COMERCIAR
     * Descripcion: solicitar comerciar con alguien.
     * Ejemplo:
     *      /COMERCIAR
     * </pre>
     */
    public final static String cmdCOMERCIAR = "/COMERCIAR";
    
    /** <pre>
     * Comando FINCOM
     * Descripcion: salir del modo comerciar.
     * Ejemplo:
     *      FINCOM
     * </pre>
     */
    public final static String cmdFINCOM = "FINCOM";

    /** <pre>
     * Comando ENTR
     * Descripcion: entrenar con una mascota.
     * Ejemplo:
     *      ENTR1
     * </pre>
     */
    public final static String cmdENTR = "ENTR";
    
    /** <pre>
     * Comando COMP
     * Descripcion: comprar.
     * Ejemplo:
     *      COMP
     * </pre>
     */
    public final static String cmdCOMP = "COMP";
    
    /** <pre>
     * Comando VEND
     * Descripcion: vender.
     * Ejemplo:
     *      VEND
     * </pre>
     */
    public final static String cmdVEND = "VEND";
    
    /** <pre>
     * Comando FINBAN
     * Descripcion: fin del modo banco.
     * Ejemplo:
     *      FINBAN
     * </pre>
     */
    public final static String cmdFINBAN = "FINBAN";
    
    /** <pre>
     * Comando /DEPOSITAR 
     * Descripcion: depositar oro en el banco.
     * Ejemplo:
     *      /DEPOSITAR 1000
     * </pre>
     */
    public final static String cmdDEPOSITAR = "/DEPOSITAR ";
    
    /** <pre>
     * Comando /RETIRAR 
     * Descripcion: retirar oro del banco.
     * Ejemplo:
     *      /RETIRAR 1000
     * </pre>
     */
    public final static String cmdRETIRAR = "/RETIRAR ";

    /** <pre>
     * Comando /BOVEDA
     * Descripcion: ver bóveda del banco.
     * Ejemplo:
     *      /BOVEDA
     * </pre>
     */
    public final static String cmdBOVEDA = "/BOVEDA";
    
    /** <pre>
     * Comando DEPO
     * Descripcion: depositar algo en la bóveda del banco.
     * Ejemplo:
     *      DEPO
     * </pre>
     */
    public final static String cmdDEPO = "DEPO";
    
    /** <pre>
     * Comando RETI
     * Descripcion: retirar algo de la bóveda del banco.
     * Ejemplo:
     *      RETI
     * </pre>
     */
    public final static String cmdRETI = "RETI";
    
    /** <pre>
     * Comando CNS
     * Descripcion: construye herreria.
     * Ejemplo:
     *      CNS
     * </pre>
     */
    public final static String cmdCNS = "CNS";
    
    /** <pre>
     * Comando CNC
     * Descripcion: construye carpinteria.
     * Ejemplo:
     *      CNC
     * </pre>
     */
    public final static String cmdCNC = "CNC";
    
    /** <pre>
     * Comando DESCOD
     * Descripcion: actualizar mandamientos del clan.
     * Ejemplo:
     *      DESCOD
     * </pre>
     */
    public final static String cmdDESCOD = "DESCOD";
    
    /** <pre>
     * Comando /QUIETO
     * Descripcion: ordenar 'quieto' a las mascotas.
     * Ejemplo:
     *      /QUIETO
     * </pre>
     */
    public final static String cmdQUIETO = "/QUIETO";
    
    /** <pre>
     * Comando /ACOMPAÑAR
     * Descripcion: ordenar 'acompañar' a las mascotas.
     * Ejemplo:
     *      /ACOMPAÑAR
     * </pre>
     */
    public final static String cmdACOMPAÑAR = "/ACOMPAÑAR";
    
    /** <pre>
     * Comando /AYUDA
     * Descripcion: enviar algo de ayuda al usuario.
     * Ejemplo:
     *      /AYUDA
     * </pre>
     */
    public final static String cmdAYUDA = "/AYUDA";

    /** <pre>
     * Comando FINCOMUSU
     * Descripcion: terminar el comercio con usuarios.
     * Ejemplo:
     *      FINCOMUSU
     * </pre>
     */
    public final static String cmdFINCOMUSU = "FINCOMUSU";
    
    /** <pre>
     * Comando COMUSUNO
     * Descripcion: rechazar el intercambio.
     * Ejemplo:
     *      COMUSUNO
     * </pre>
     */
    public final static String cmdCOMUSUNO = "COMUSUNO";
    
    /** <pre>
     * Comando COMUSUOK
     * Descripcion: aceptar el cambio.
     * Ejemplo:
     *      COMUSUOK
     * </pre>
     */
    public final static String cmdCOMUSUOK = "COMUSUOK";

    /** <pre>
     * Comando OFRECER
     * Descripcion: ofrecer un objeto a otro usuario.
     * Ejemplo:
     *      OFRECER
     * </pre>
     */
    public final static String cmdOFRECER = "OFRECER";
    
    /** <pre>
     * Comando /GM
     * Descripcion: pedir ayuda a un GM.
     * Ejemplo:
     *      /GM mensaje para el GM
     * </pre>
     */
    public final static String cmdGM = "/GM";
    
    /** <pre>
     * Comando FEST
     * Descripcion: pedir mini estadísticas.
     * Parámetros: ninguno.
     * Ejemplo:
     *      <C>     FEST
     *      <S>     MEST0,2,2,50,GUERRERO,0
     *
     * Respueta:
     * Comando MEST
     * Parámetros: ciudadanos matados, criminales matados, usuarios matados,
     *  npcs matados, clase, tiempo restante en carcel.
     * Separador: ,
     * </pre>
     */
    public final static String cmdFEST = "FEST";
    
    /** <pre>
     * Comando /AVENTURA
     * Descripcion: solicitar iniciar una quest o aventura.
     * Parámetros: ninguno.
     * </pre>
     */
    public final static String cmdAVENTURA = "/AVENTURA";
    
    /** <pre>
     * Comando /REWARD
     * Descripcion: solicitar recompensa de la aventura.
     * Parámetros: ninguno.
     * </pre>
     */
    public final static String cmdREWARD = "/REWARD";
    
    /** <pre>
     * Comando /INFOQ
     * Descripcion: solicitar info de la aventura.
     * Parámetros: ninguno.
     * </pre>
     */
    public final static String cmdINFOQ = "/INFOQ";
    
    /** <pre>
     * Comando /MERINDO
     * Descripcion: abandonar la aventura actual.
     * Parámetros: ninguno.
     * </pre>
     */
    public final static String cmdMERINDO = "/MERINDO";
    
    /** <pre>
     * Comando /ADIVINA
     * Descripcion: 
     * Parámetros: ninguno.
     * </pre>
     */
    public final static String cmdADIVINA = "/ADIVINA";
    
    /** <pre>
     * Comando /ENLISTAR
     * Descripcion: enlistar en una facción (Real o Caos)
     * Ejemplo:
     *      /ENLISTAR
     * </pre>
     */
    public final static String cmdENLISTAR = "/ENLISTAR";

    /** <pre>
     * Comando /RECOMPENSA
     * Descripcion: pedir recompensa a una facción (Real o Caos)
     * Ejemplo:
     *      /RECOMPENSA
     * </pre>
     */
    public final static String cmdRECOMPENSA = "/RECOMPENSA";
    
    /** <pre>
     * Comando DEMSG
     * Descripcion: dejar un mensaje en el foro público.
     * Ejemplo:
     *      DEMSGun tituloºun mensaje para el foro
     * </pre>
     */
    public final static String cmdDEMSG = "DEMSG";

    /** <pre>
     * Comando /UPTIME
     * Descripcion: devuelve el tiempo trascurrido desde el inicio del servidor.
     * </pre>
     */
    public final static String cmdUPTIME = "/UPTIME";

    /** <pre>
     * Comando /APOSTAR
     * Descripcion: jugar una cantidad de oro con el apostador.
     * Ejemplo: /APOSTAR 1000
     * </pre>
     */
    public final static String cmdAPOSTAR = "/APOSTAR ";

    /** <pre>
     * Comando DESPHE
     * Descripcion: Mover hechizo de lugar
     * </pre>
     */
    public final static String cmdDESPHE = "DESPHE";
    
    /** <pre>
     * Comando /FUNDARCLAN
     * Descripcion: Fundar un clan
     * </pre>
     */
    public final static String cmdFUNDARCLAN = "/FUNDARCLAN";
    
    /** <pre>
     * Comando /SALIRCLAN
     * Descripcion: Salir del clan.
     * </pre>
     */
    public final static String cmdSALIRCLAN= "/SALIRCLAN";    
		
    /** <pre>
     * Comando GLINFO
     * Descripcion: Informaciòn sobre un clan.
     * </pre>
     */
    public final static String cmdGLINFO = "GLINFO";

    /** <pre>
     * Comando CIG
     * Descripcion: Crear un clan nuevo.
     * </pre>
     */
    public final static String cmdCIG = "CIG";

    /** <pre>
     * Comando /VOTO 
     * Descripcion: Votar en la votaciòn del clan
     * </pre>
     */
    public final static String cmdVOTO = "/VOTO ";

    /** <pre>
     * Comando ACEPPEAT
     * Descripcion: 
     * </pre>
     */
    public final static String cmdACEPPEAT = "ACEPPEAT";

    /** <pre>
     * Comando PEACEOFF
     * Descripcion: 
     * </pre>
     */
    public final static String cmdPEACEOFF = "PEACEOFF";

    /** <pre>
     * Comando PEACEDET
     * Descripcion: 
     * </pre>
     */
    public final static String cmdPEACEDET = "PEACEDET";

    /** <pre>
     * Comando ENVCOMEN
     * Descripcion: 
     * </pre>
     */
    public final static String cmdENVCOMEN = "ENVCOMEN";

    /** <pre>
     * Comando ENVPROPP
     * Descripcion: 
     * </pre>
     */
    public final static String cmdENVPROPP = "ENVPROPP";

    /** <pre>
     * Comando DECGUERR
     * Descripcion: 
     * </pre>
     */
    public final static String cmdDECGUERR = "DECGUERR";

    /** <pre>
     * Comando DECALIAD
     * Descripcion: 
     * </pre>
     */
    public final static String cmdDECALIAD = "DECALIAD";

    /** <pre>
     * Comando NEWWEBSI
     * Descripcion: 
     * </pre>
     */
    public final static String cmdNEWWEBSI = "NEWWEBSI";

    /** <pre>
     * Comando ACEPTARI
     * Descripcion: 
     * </pre>
     */
    public final static String cmdACEPTARI = "ACEPTARI";

    /** <pre>
     * Comando RECHAZAR
     * Descripcion: 
     * </pre>
     */
    public final static String cmdRECHAZAR = "RECHAZAR";

    /** <pre>
     * Comando ECHARCLA
     * Descripcion: 
     * </pre>
     */
    public final static String cmdECHARCLA = "ECHARCLA";

    /** <pre>
     * Comando ACTGNEWS
     * Descripcion: 
     * </pre>
     */
    public final static String cmdACTGNEWS = "ACTGNEWS";

    /** <pre>
     * Comando 1HRINFO<
     * Descripcion: 
     * </pre>
     */
    public final static String cmd1HRINFO = "1HRINFO<";

    /** <pre>
     * Comando SOLICITUD
     * Descripcion: 
     * </pre>
     */
    public final static String cmdSOLICITUD = "SOLICITUD";

    /** <pre>
     * Comando CLANDETAILS
     * Descripcion: 
     * </pre>
     */
    public final static String cmdCLANDETAILS = "CLANDETAILS";

    /////////////////////////// ADMINISTRADORES /////////////////////////
    // CONSEJEROS - CONSEJEROS - CONSEJEROS - CONSEJEROS - CONSEJEROS - 
       
    /** <pre>
     * Comando /PANELGM 
     * Descripcion: solicita abrir el panel de control de GM.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      /PANELGM 
     * </pre>
     */
    public final static String cmdPANELGM = "/PANELGM";
    
    /** <pre>
     * Comando LISTUSU 
     * Descripcion: solicita la lista de usuarios.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      LISTUSU 
     * </pre>
     */
    public final static String cmdLISTUSU = "LISTUSU";
        
    /** <pre>
     * Comando /REM
     * Descripcion: registrar un comentario.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      /REM un comentario sobre lo que hice...
     * </pre>
     */
    public final static String cmdREM = "/REM ";
    
    /** <pre>
     * Comando /HORA
     * Descripcion: muestra la hora actual del server.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      /HORA
     * </pre>
     */
    public final static String cmdHORA = "/HORA";
    
    /** <pre>
     * Comando /DONDE 
     * Descripcion: muestra donde esta un personaje.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      /DONDE fulano
     * </pre>
     */
    public final static String cmdDONDE  = "/DONDE ";
    
    /** <pre>
     * Comando /NENE 
     * Descripcion: muestra la cantidad de NPCs hostiles en un mapa.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      /NENE nro_mapa
     * </pre>
     */
    public final static String cmdNENE = "/NENE ";
    
    /** <pre>
     * Comando /TELEPLOC
     * Descripcion: (que hace??? - fixme )
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      /TELEPLOC
     * </pre>
     */
    public final static String cmdTELEPLOC = "/TELEPLOC";
    
    /** <pre>
     * Comando /TELEP 
     * Descripcion: Teleportar un usuario (o a si mismo) a algun lugar.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      /TELEP usuario nro_mapa x y
     * </pre>
     */
    public final static String cmdTELEP = "/TELEP ";
    
    /** <pre>
     * Comando /SHOW SOS
     * Descripcion: mostrar ayuda.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      /SHOW SOS
     * </pre>
     */
    public final static String cmdSHOWSOS = "/SHOW SOS";
    
    /** <pre>
     * Comando SOSDONE
     * Descripcion: dejar de mostrar ayuda.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      SOSDONE
     * </pre>
     */
    public final static String cmdSOSDONE = "SOSDONE";
    
    /** <pre>
     * Comando /IRA 
     * Descripcion: ir a donde se encuentra un usuario.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      /IRA usuario
     * </pre>
     */
    public final static String cmdIRA = "/IRA ";
    
    /** <pre>
     * Comando /INVISIBLE
     * Descripcion: volverse invisible.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      /INVISIBLE
     * </pre>
     */
    public final static String cmdINVISIBLE = "/INVISIBLE";

    /** <pre>
     * Comando /GMSG 
     * Descripcion: enviar un mensaje a todos los GMs.
     * Rol minimo: CONSEJERO.
     * Ejemplo:
     *      /GMSG mensaje a otros GMs
     * </pre>
     */
    public final static String cmdGMSG = "/GMSG ";
    

    /** <pre>
     * Comando /INFO 
     * Descripcion: informacion sobre el usuario.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /INFO usuario
     * </pre>
     */
    public final static String cmdINFOUSER = "/INFO ";
    
    /** <pre>
     * Comando /INV
     * Descripcion: inventario del usuario.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /INV usuario
     * </pre>
     */
    public final static String cmdINV = "/INV ";

    /** <pre>
     * Comando /BOV
     * Descripcion: bóveda del usuario.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /BOV usuario
     * </pre>
     */
    public final static String cmdBOV = "/BOV ";

    /** <pre>
     * Comando /SKILLS 
     * Descripcion: skills del usuario.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /SKILLS usuario
     * </pre>
     */
    public final static String cmdSKILLS = "/SKILLS ";

    /** <pre>
     * Comando /REVIVIR 
     * Descripcion: revivir usuario.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /REVIVIR usuario
     * </pre>
     */
    public final static String cmdREVIVIR = "/REVIVIR ";

    /** <pre>
     * Comando /ONLINEGM
     * Descripcion: GMs online.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /ONLINEGM
     * </pre>
     */
    public final static String cmdONLINEGM = "/ONLINEGM";
    

    /** <pre>
     * Comando /CARCEL
     * Descripcion: dar carcel a un usuario.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /CARCEL minutos usuario
     * </pre>
     */
    public final static String cmdCARCEL = "/CARCEL ";

    /** <pre>
     * Comando /PERDON
     * Descripcion: dar perdón a un usuario.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /PERDON usuario
     * </pre>
     */
    public final static String cmdPERDON = "/PERDON ";
    
    /** <pre>
     * Comando /ECHAR 
     * Descripcion: echar (desconectar) un usuario.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /ECHAR usuario
     * </pre>
     */
    public final static String cmdECHAR = "/ECHAR ";

    /** <pre>
     * Comando /BAN 
     * Descripcion: inhabilitar el usuario.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /BAN usuario
     * </pre>
     */
    public final static String cmdBAN = "/BAN ";

    /** <pre>
     * Comando /UNBAN 
     * Descripcion: rehabilitar al usuario.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /UNBAN usuario
     * </pre>
     */
    public final static String cmdUNBAN = "/UNBAN ";

    /** <pre>
     * Comando /SEGUIR
     * Descripcion: hacer que nos siga un Npc.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /SEGUIR usuario
     * </pre>
     */
    public final static String cmdSEGUIR = "/SEGUIR";

    /** <pre>
     * Comando /SUM 
     * Descripcion: traer un usuario adonde esta el GM.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /SUM usuario
     * </pre>
     */
    public final static String cmdSUM = "/SUM ";

    /** <pre>
     * Comando /CC
     * Descripcion: crear una criatura
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /CC
     * </pre>
     */
    public final static String cmdCC = "/CC";

    /** <pre>
     * Comando SPA 
     * Descripcion: spawn una criatura.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      SPA
     * </pre>
     */
    public final static String cmdSPA = "SPA";
    
    /** <pre>
     * Comando /RESETINV 
     * Descripcion: resetea el inventario del Npc.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /RESETINV
     * </pre>
     */
    public final static String cmdRESETINV = "/RESETINV";
    
    /** <pre>
     * Comando /LIMPIAR 
     * Descripcion: limpiar mundo.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /LIMPIAR
     * </pre>
     */
    public final static String cmdLIMPIAR = "/LIMPIAR";
    
    /** <pre>
     * Comando /TRABAJANDO 
     * Descripcion: Lista de usuarios trabajando.
     * Rol minimo: SEMIDIOS.
     * </pre>
     */
    public final static String cmdTRABAJANDO = "/TRABAJANDO";

    /** <pre>
     * Comando /ONLINEMAP 
     * Descripcion: Lista de usuarios en el mapa actual.
     * Rol minimo: SEMIDIOS.
     * </pre>
     */
    public final static String cmdONLINEMAP = "/ONLINEMAP";

    /** <pre>
     * Comando /RMSG
     * Descripcion: mensaje del servidor.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /RMSG mensaje
     * </pre>
     */
    public final static String cmdRMSG = "/RMSG ";
    
    /** <pre>
     * Comando /IP2NICK
     * Descripcion: IP del nick.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /IP2NICK 127.0.0.1
     * </pre>
     */
    public final static String cmdIP2NICK = "/IP2NICK ";

    /** <pre>
     * Comando /NICK2IP
     * Descripcion: nick del IP.
     * Rol minimo: SEMIDIOS.
     * Ejemplo:
     *      /NICK2IP alguien
     * </pre>
     */
    public final static String cmdNICK2IP = "/NICK2IP ";
    
    /** <pre>
     * Comando /BANIP
     * Descripcion: ban de IP.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /BANIP 192.168.0.1
     * </pre>
     */
    public final static String cmdBANIP = "/BANIP ";
    
    /** <pre>
     * Comando /UNBANIP
     * Descripcion: unban de IP.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /UNBANIP 192.168.0.1
     * </pre>
     */
    public final static String cmdUNBANIP = "/UNBANIP ";
    
    /** <pre>
     * Comando /CONDEN
     * Descripcion: condenar a un usuario (volverlo criminal).
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /CONDEN usuario
     * </pre>
     */
    public final static String cmdCONDEN = "/CONDEN ";
    
    /** <pre>
     * Comando /SMSG
     * Descripcion: mensaje del sistema.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /SMSG un mensaje
     * </pre>
     */
    public final static String cmdSMSG = "/SMSG ";
    
    /** <pre>
     * Comando /LLUVIA
     * Descripcion: iniciar o detener la lluvia.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /LLUVIA
     * </pre>
     */
    public final static String cmdLLUVIA = "/LLUVIA";

    /** <pre>
     * Comando /APAGAR
     * Descripcion: apagado rápido (y sucio) del servidor.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /APAGAR
     * </pre>
     */
    public final static String cmdAPAGAR = "/APAGAR";

    /** <pre>
     * Comando /GRABAR
     * Descripcion: guardar todos los usuarios conectados.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /GRABAR
     * </pre>
     */
    public final static String cmdGRABAR = "/GRABAR";

    /** <pre>
     * Comando /DOBACKUP
     * Descripcion: hacer un backup de todo el mundo.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /DOBACKUP
     * </pre>
     */
    public final static String cmdDOBACKUP = "/DOBACKUP";

    /** <pre>
     * Comando /PASSDAY
     * Descripcion: 
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /PASSDAY
     * </pre>
     */
    public final static String cmdPASSDAY = "/PASSDAY";

    /** <pre>
     * Comando /ACC
     * Descripcion: crear un npc
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /ACC 2
     * </pre>
     */
    public final static String cmdACC = "/ACC ";

    /** <pre>
     * Comando /RACC
     * Descripcion: crear un npc
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /RACC 2
     * </pre>
     */
    public final static String cmdRACC = "/RACC ";

    /** <pre>
     * Comando /MATA
     * Descripcion: quitar el npc donde se hizo clic (target npc).
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /MATA
     * </pre>
     */
    public final static String cmdMATA = "/MATA";

    /** <pre>
     * Comando /CT
     * Descripcion: Crear un teleport
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /CT dest_mapa dest_x dest_y
     * </pre>
     */
    public final static String cmdCT = "/CT ";

    /** <pre>
     * Comando /DT
     * Descripcion: destruir un teleport, usa el ultimo clic
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /DT
     * </pre>
     */
    public final static String cmdDT = "/DT";

    /** <pre>
     * Comando /AI1
     * Descripcion: cambiar armadura imperial 1
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /AI1 indiceArmadura
     * </pre>
     */
    public final static String cmdAI1 = "/AI1";
    
    /** <pre>
     * Comando /AI2
     * Descripcion: cambiar armadura imperial 2
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /AI2 indiceArmadura
     * </pre>
     */
    public final static String cmdAI2 = "/AI2";
    
    /** <pre>
     * Comando /AI3
     * Descripcion: cambiar armadura imperial 3
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /AI3 indiceArmadura
     * </pre>
     */
    public final static String cmdAI3 = "/AI3";
    
    /** <pre>
     * Comando /AI4
     * Descripcion: cambiar armadura imperial 4
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /AI4 indiceArmadura
     * </pre>
     */
    public final static String cmdAI4 = "/AI4";
    
    /** <pre>
     * Comando /AI5
     * Descripcion: cambiar armadura imperial 5
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /AI5 indiceArmadura
     * </pre>
     */
    public final static String cmdAI5 = "/AI5";
    
    /** <pre>
     * Comando /AC1
     * Descripcion: cambiar armadura caos 1
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /AC1 indiceArmadura
     * </pre>
     */
    public final static String cmdAC1 = "/AC1";
    
    /** <pre>
     * Comando /AC2
     * Descripcion: cambiar armadura caos 2
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /AC2 indiceArmadura
     * </pre>
     */
    public final static String cmdAC2 = "/AC2";
    
    /** <pre>
     * Comando /AC3
     * Descripcion: cambiar armadura caos 3
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /AC3 indiceArmadura
     * </pre>
     */
    public final static String cmdAC3 = "/AC3";
    
    /** <pre>
     * Comando /AC4
     * Descripcion: cambiar armadura caos 4
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /AC4 indiceArmadura
     * </pre>
     */
    public final static String cmdAC4 = "/AC4";
    
    /** <pre>
     * Comando /AC5
     * Descripcion: cambiar armadura caos 5
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /AC5 indiceArmadura
     * </pre>
     */
    public final static String cmdAC5 = "/AC5";

    /** <pre>
     * Comando /MASCOTAS
     * Descripcion: informa estado de las mascotas
     * Rol minimo: DIOS.
     * COMANDO ESPECIFICO DE AOJ - PARA DEBUG -
     * Ejemplo:
     *      /MASCOTAS usuario
     * </pre>
     */
    public final static String cmdMASCOTAS = "/MASCOTAS";

    /** <pre>
     * Comando /NAVE
     * Descripcion: comando para depurar la navegación.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /NAVE
     * </pre>
     */
    public final static String cmdNAVE = "/NAVE";

    /** <pre>
     * Comando /BORRAR SOS
     * Descripcion: comando para borrar todos los pedidos /GM pendientes.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /BORRAR SOS
     * </pre>
     */
    public final static String cmdBORRARSOS = "/BORRAR SOS";

    /** <pre>
     * Comando /ECHARTODOSPJS
     * Descripcion: comando para echar a todos los pjs no privilegiados.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /ECHARTODOSPJS
     * </pre>
     */
    public final static String cmdEcharTodosPjs = "/ECHARTODOSPJS";

    /** <pre>
     * Comando /SHOW INT
     * Descripcion: comando para abrir la ventana de config de intervalos en el server.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /SHOW INT
     * </pre>
     */
    public final static String cmdShowInt = "/SHOW INT";

    /** <pre>
     * Comando /VERSION
     * Descripcion: devuelve información sobre la versión del servidor.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /VERSION
     * </pre>
     */
    public final static String cmdVERSION = "/VERSION";

    /** <pre>
     * Comando /MOTDCAMBIA
     * Descripcion: pedir cambiar el mensaje del dia.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      /MOTDCAMBIA
     * </pre>
     */
    public final static String cmdMOTDCAMBIA = "/MOTDCAMBIA";

    /** <pre>
     * Comando ZMOTD
     * Descripcion: guardar cambios en el mensaje del dia.
     * Rol minimo: DIOS.
     * Ejemplo:
     *      ZMOTDNuevo Mensaje<CRLF>Otra Linea
     * 	    donde <CRLF> son los caracteres <CR> y <LF> que separan las lineas.
     * </pre>
     */
    public final static String cmdZMOTD = "ZMOTD";
    
    /** <pre>
     * Comando /MASSDEST
     * Descripcion: Quita todos los objetos del area.
     * Rol minimo: DIOS.
     * Ejemplo: /MASSDEST
     * </pre>
     */
    public final static String cmdMASSDEST = "/MASSDEST";
    
    /** <pre>
     * Comando /TRIGGER
     * Descripcion: Consulta o cambia el trigger de la posicion actual.
     * Rol minimo: DIOS.
     * Ejemplo: /TRIGGER 1
     * </pre>
     */
    public final static String cmdTRIGGER = "/TRIGGER";
    
    /** <pre>
     * Comando /DEST
     * Descripcion: Destruir el objeto de la posición actual.
     * Rol minimo: DIOS.
     * Ejemplo: /DEST
     * </pre>
     */
    public final static String cmdDEST = "/DEST";
    
    /** <pre>
     * Comando /BLOQ
     * Descripcion: Bloquear la posición actual.
     * Rol minimo: DIOS.
     * Ejemplo: /BLOQ
     * </pre>
     */
    public final static String cmdBLOQ = "/BLOQ";
    
    /** <pre>
     * Comando /MASSKILL
     * Descripcion: Quitar todos los NPCs del area.
     * Rol minimo: DIOS.
     * Ejemplo: /MASSKILL
     * </pre>
     */
    public final static String cmdMASSKILL = "/MASSKILL";
	
    /** <pre>
     * Comando /CI
     * Descripcion: Crear un item en el mapa.
     * Rol minimo: DIOS.
     * Ejemplo: /CI 123
     * </pre>
     */
    public final static String cmdCI = "/CI ";
	
    /** <pre>
     * Comando /GUARDAMAPA
     * Descripcion: Guarda el mapa actual.
     * Rol minimo: DIOS.
     * Ejemplo: /GUARDAMAPA
     * </pre>
     */
    public final static String cmdGUARDAMAPA = "/GUARDAMAPA";	
    
	
    /** <pre>
     * Comando /MOD
     * Descripcion: Modificar un caracter.
     * Rol minimo: DIOS.
     * Ejemplos:
     *   /MOD gorlok ORO 5000 
     *   /MOD gorlok EXP 100000 
     *   /MOD gorlok BODY 1 
     *   /MOD gorlok HEAD 2 
     *   /MOD gorlok CRI 100 
     *   /MOD gorlok CIU 100 
     *   /MOD gorlok LEVEL 35 
     * </pre>
     */
    public final static String cmdMOD = "/MOD ";
    
    /** <pre>
     * Comando /MODMAPINFO
     * Descripcion: Modificar info del mapa.
     * Rol minimo: DIOS.
     * Ejemplos:
     *   /MODMAPINFO PK 0  
     *   /MODMAPINFO BACKUP 1  
     * </pre>
     */
    public final static String cmdMODMAPINFO = "/MODMAPINFO ";
    
    /** <pre>
     * Comando /DEBUG
     * Descripcion: Usos varios, para Dioses. Comando especial de AOJava.
     * Rol minimo: DIOS.
     * Ejemplos:
     *   /DEBUG
     *   /DEBUG ON  
     *   /DEBUG OFF  
     * </pre>
     */
    public final static String cmdDEBUG = "/DEBUG";
    
}
