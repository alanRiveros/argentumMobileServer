/**    
 * AojServer.java
 *
 * Created on 6 de septiembre de 2003, 19:05
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
package org.ArgentumOnline.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;

import org.ArgentumOnline.server.guilds.GuildManager;
import org.ArgentumOnline.server.protocol.ClientMessage;
import org.ArgentumOnline.server.util.Feedback;
import org.ArgentumOnline.server.util.FontType;
import org.ArgentumOnline.server.util.IniFile;
import org.ArgentumOnline.server.util.Log;
import org.ArgentumOnline.server.util.Util;

import static org.ArgentumOnline.server.protocol.ClientMessage.*;

/** 
 * AOJava main class.
 * @author gorlok
 */
public class AojServer implements Constants {
    
    private ServerSocketChannel server;
    private Selector selector;
    private final static int BUFFER_SIZE = 1024;
    private ByteBuffer serverBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    
    private HashMap<SocketChannel, Client> m_clientSockets = new HashMap<SocketChannel, Client>();
    private HashMap<Short, Client> m_clientes = new HashMap<Short, Client>();
    private HashMap<Integer, Npc> 	m_npcs       = new HashMap<Integer, Npc>();
    private HashMap<String, Forum> 	m_foros       = new HashMap<String, Forum>();
    
    private List<Client> m_clientes_eliminar = new LinkedList<Client>();
    private List<Short> m_npcs_muertos = new Vector<Short>();
    
    private List<ObjectInfo> m_objetos  = new Vector<ObjectInfo>();
    private List<Spell> 	m_hechizos = new Vector<Spell>();
    private List<Map> 		m_mapas = new Vector<Map>();    
    private List<Quest> 	m_quests = new Vector<Quest>();
    
    private List<GmRequest> m_pedidosAyudaGM = new Vector<GmRequest>();
    private List<String> m_bannedIPs = new Vector<String>();
    
    private List<String> m_dioses = new Vector<String>();
    private List<String> m_semidioses = new Vector<String>();
    private List<String> m_consejeros = new Vector<String>();
    private List<String> m_motd = new Vector<String>();

    private List<String> m_nombresInvalidos = new Vector<String>();
    
    private List<WorldPos> m_trashCollector = new Vector<WorldPos>();
    
    private short [] m_armasHerrero;
    private short [] m_armadurasHerrero;
    private short [] m_objCarpintero;
    private short [] m_spawnList;
    private String [] m_spawnListNames;
    
    boolean m_corriendo = false;
    boolean m_haciendoBackup = false;
    
    short id = 0;
    
    boolean m_lloviendo = false;
    
    private WorldPos[] m_ciudades;
    
    private long startTime = 0;
    
    public static WorldPos WP_PRISION = new WorldPos((short) 66, (short) 75, (short) 47);
    public static WorldPos WP_LIBERTAD = new WorldPos((short) 66, (short) 75, (short) 65);
    
    private boolean m_showDebug = false;
    
    private Properties prop = null;
    
    private GuildManager m_guildMngr;
    
    private static AojServer m_instance = null;
    
    /** Creates a new instance of Server */
    private AojServer() {
    	this.m_guildMngr = new GuildManager(this);
    }

    public static AojServer getInstance() {
        if (m_instance == null) {
			m_instance = new AojServer();
		}
        return m_instance;
    }
    
    public GuildManager getGuildMngr() {
    	return this.m_guildMngr;
    }
    
    public Properties getConfig() {
    	return this.prop;
    }
    
    public Collection<Client> getClientes() {
    	return this.m_clientes.values();
    }
    
    public Collection<Npc> getNpcs() {
    	return this.m_npcs.values();
    }
    
    public long runningTimeInSecs() {
        return (getMillis() - this.startTime) / 1000;
    }
    
    public short getNextId() {
        return ++this.id;
    }
    
    public boolean estaLloviendo() {
        return this.m_lloviendo;
    }
    
    public List<GmRequest> getPedidosAyudaGM() {
        return this.m_pedidosAyudaGM;
    }
    
    public boolean estaHaciendoBackup() {
        return this.m_haciendoBackup;
    }
    
    public List<WorldPos> getTrashCollector() {
        return this.m_trashCollector;
    }
    
    public Quest getQuest(int n) {
        return this.m_quests.get(n - 1);
    }
    
    public int getQuestCount() {
        return this.m_quests.size();
    }
    
    public short[] getSpawnList() {
        return this.m_spawnList;
    }
    
    public String[] getSpawnListNames() {
        return this.m_spawnListNames;
    }
    
    public short [] getArmasHerrero() {
        return this.m_armasHerrero;
    }
    
    public short [] getArmadurasHerrero() {
        return this.m_armadurasHerrero;
    }
    
    public short [] getObjCarpintero() {
        return this.m_objCarpintero;
    }
    
    public List<String> getBannedIPs() {
        return this.m_bannedIPs;
    }
    
    public boolean isShowDebug() {
    	return this.m_showDebug;
    }
    
    public void setShowDebug(boolean value) {
    	this.m_showDebug = value;
    }
    
    public List<String> getUsuariosConectados() {
        Vector<String> usuarios = new Vector<String>();
        for (Client cli: getClientes()) {
            if (!"".equals(cli.getNick()) && !cli.esGM()) {
                usuarios.add(cli.getNick());
            }
        }
        return usuarios;
    }
    
    public void echarPjsNoPrivilegiados() {
        for (Client cli: getClientes()) {
            if (!"".equals(cli.getNick()) && !cli.esGM() && cli.isLogged()) {
            	cli.enviarMensaje("Servidor> Conexiones temporalmente cerradas por mantenimiento.", FontType.SERVER);
                cli.doSALIR();
            }
        }
    }
	
    public List<String> getUsuariosTrabajando() {
        Vector<String> usuarios = new Vector<String>();
        for (Client cli: getClientes()) {
            if (!"".equals(cli.getNick()) && cli.estaTrabajando()) {
                usuarios.add(cli.getNick());
            }
        }
        return usuarios;
    }
    
    public void shutdown() {
        this.m_corriendo = false;
    }
    
    public List<String> getUsuarioConIP(String ip) {
        Vector<String> usuarios = new Vector<String>();
        for (Client cli: getClientes()) {
            if (!"".equals(cli.getNick()) && cli.getIP().equals(ip)) {
                usuarios.add(cli.getNick());
            }
        }
        return usuarios;
    }
    
    public List<String> getMOTD() {
    	return this.m_motd;
    }
    
    public void setMOTD(List<String> motd) {
    	this.m_motd.clear();
    	this.m_motd.addAll(motd);
    }
    
    public List<String> getGMsOnline() {
        Vector<String> usuarios = new Vector<String>();
        for (Client cli: getClientes()) {
            if (cli.getNick() != null && cli.getNick().length() > 0 && cli.esGM()) {
                usuarios.add(cli.getNick());
            }
        }
        return usuarios;
    }
    
    /** Start point for the server
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean loadBackup = !(args.length > 0 && args[0].equalsIgnoreCase("reset"));
        try {
            Log.start();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return;
        }
        if (loadBackup) {
			Log.serverLogger().info("Arrancando usando el backup");
		} else {
			Log.serverLogger().info("Arrancando sin usar backup");
		}
        AojServer.getInstance().run(loadBackup);
        Log.stop();
    }
    
    
    /** Inicializa el socket del servidor. */
    private void initServerSocket() 
    throws java.io.IOException {
        /* FIXME
        'Misc
        CrcSubKey = val(GetVar(IniPath & "Server.ini", "INIT", "CrcSubKey"))
        ServerIp = GetVar(IniPath & "Server.ini", "INIT", "ServerIp")
        Temporal = InStr(1, ServerIp, ".")
        Temporal1 = (Mid(ServerIp, 1, Temporal - 1) And &H7F) * 16777216
        ServerIp = Mid(ServerIp, Temporal + 1, Len(ServerIp))
        Temporal = InStr(1, ServerIp, ".")
        Temporal1 = Temporal1 + Mid(ServerIp, 1, Temporal - 1) * 65536
        ServerIp = Mid(ServerIp, Temporal + 1, Len(ServerIp))
        Temporal = InStr(1, ServerIp, ".")
        Temporal1 = Temporal1 + Mid(ServerIp, 1, Temporal - 1) * 256
        ServerIp = Mid(ServerIp, Temporal + 1, Len(ServerIp))
        MixedKey = (Temporal1 + ServerIp) Xor &H65F64B42
        */
        this.server = ServerSocketChannel.open();
        this.server.configureBlocking(false);
        //server.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), SERVER_PORT));
        this.server.socket().bind(new InetSocketAddress(SERVER_PORT));
        Log.serverLogger().info("Escuchando en el puerto " + SERVER_PORT);
        this.selector = Selector.open();
        this.server.register(this.selector, SelectionKey.OP_ACCEPT);
    }
    
    /** Acepta una conexi�n nueva. */
    private void acceptConnection() 
    throws java.io.IOException {
        // get client socket channel.
        SocketChannel clientSocket = this.server.accept();
        // Non blocking I/O
        clientSocket.configureBlocking(false);
        // recording to the selector (reading)
        clientSocket.register(this.selector, SelectionKey.OP_READ);
        Client cliente = new Client(clientSocket, this);
        this.m_clientSockets.put(clientSocket, cliente);
        this.m_clientes.put(cliente.getId(), cliente);
        Log.serverLogger().fine("NUEVA CONEXION");
    }
    
    /** Cerrar una conexi�n. */
    public void closeConnection(Client cliente) 
    throws java.io.IOException {
        Log.serverLogger().fine("cerrando conexion");
        this.m_clientSockets.remove(cliente);
        this.m_clientes_eliminar.add(cliente);
        cliente.socketChannel.close();
    }
    
    /** Lee datos de una conexi�n existente. */
    private void readConnection(SocketChannel clientSocket) 
    throws java.io.IOException {
        Client cliente = this.m_clientSockets.get(clientSocket);
        Log.serverLogger().fine("Recibiendo del cliente: " + cliente);
        // Read bytes coming from the client.
        this.serverBuffer.clear();
        try {
            clientSocket.read(this.serverBuffer);
        } catch (Exception e) {
            cliente.doSALIR();
            return;
        }
        // process the message.
        this.serverBuffer.flip();
        
        if (!cliente.getProtocol().decodeData(this.serverBuffer.array(), this.serverBuffer.limit())) {
            cliente.doSALIR();
        }
    }
    
    public long getMillis() {
    	// 1 s  = 1.000 ms
    	// 1 ms = 1.000 us
    	// 1 us = 1.000 ns
    	// 1 ms = 1.000.000 ns
        return System.nanoTime() / 1000000;
    }
    
    /*
    public void setupCleanShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                AOServer.getInstance().shutdown();
            }
        });
    }
    */
    
    /** Main loop of the game. */
    public void run(boolean loadBackup) {
        this.prop = Util.getProperties(this, "config.properties");    	
        loadAllData(loadBackup);
        try {
            initServerSocket();
            // Main loop.
            this.startTime = getMillis();
            long lastNpcAI = this.startTime;
            long lastFX = this.startTime;
            long lastGameTimer = this.startTime;
            long lastNpcAtacaTimer = this.startTime;
            long lastTimerOculto = this.startTime;
            long lastLluviaTimer = this.startTime;
            long lastEventTimer = this.startTime;
            long lastPiqueteTimer = this.startTime;
            long lastPurgarPenas = this.startTime;
            long lastAutoSaveTimer = this.startTime;
            long lastPasarSegundoTimer = this.startTime;
            this.m_corriendo = true;
            //setupCleanShutdown();
            while (this.m_corriendo) {
                // Wainting for events...
                this.selector.select(40); // milisegundos
                Set<SelectionKey> keys = this.selector.selectedKeys();
                for (SelectionKey key: keys) {
                    // if isAcceptable, then a client required a connection.
                    if (key.isAcceptable()) {
                        Log.serverLogger().fine("nueva conexion");
                        acceptConnection();
                        continue;
                    }
                    // if isReadable, then the server is ready to read
                    if (key.isReadable()) {
                        Log.serverLogger().fine("leer de conexion");
                        readConnection((SocketChannel) key.channel());
                        continue;
                    }
                }
                keys.clear();
                long now = getMillis();
                if ((now - lastNpcAI) > 400) {
                    doAI();
                    lastNpcAI = now;
                }
                if ((now - lastFX) > 200) {
                    FX_Timer();
                    lastFX = now;
                }
                if ((now - lastGameTimer) > 40) { // 40 ??
                    gameTimer();
                    lastGameTimer = now;
                }
                if ((now - lastNpcAtacaTimer) > 2000) { // 4000 ??
                    npcAtacaTimer();
                    lastNpcAtacaTimer = now;
                }
                if ((now - lastTimerOculto) > 500) { // 3000 ??
                    timerOculto();
                    lastTimerOculto = now;
                }
                if ((now - lastLluviaTimer) > 1500) { // 60000 ??
                    lluviaTimer();
                    lastLluviaTimer = now;
                }
                if ((now - lastEventTimer) > 60000) {
                    lluviaEvent();
                    lastEventTimer = now;
                }
                if ((now - lastPiqueteTimer) > 6000) {
                    piqueteTimer();
                    lastPiqueteTimer = now;
                }
                if ((now - lastPurgarPenas) > 60000) {
                    purgarPenas();
                    lastPurgarPenas = now;
                }
                if ((now - lastAutoSaveTimer) > 60000) {
                    autoSaveTimer();
                    lastAutoSaveTimer = now;
                }
                if ((now - lastPasarSegundoTimer) > 1000) { // 1 vez x segundo
                    pasarSegundo();
                    lastPasarSegundoTimer = now;
                }
                eliminarClientes();
            }
        } catch (UnknownHostException ex) {
            Log.serverLogger().log(Level.SEVERE, "AOServer run loop", ex);
        } catch (java.net.BindException ex) {
            Log.serverLogger().log(Level.SEVERE, "El puerto ya est� en uso. �El servidor ya est� corriendo?", ex);
        } catch (IOException ex) {
            Log.serverLogger().log(Level.SEVERE, "AOServer run loop", ex);
        } finally {
            // Clean exit
            doBackup();
            guardarUsuarios();
            Log.serverLogger().info("Server apagado");
        }
    }
    
    private synchronized void eliminarClientes() {
    	// La eliminaci�n de m_clientes de la lista de m_clientes se hace aqu�
    	// para evitar modificaciones concurrentes al hashmap, entre otras cosas.
    	for (Client cliente: this.m_clientes_eliminar) {
            this.m_clientes.remove(cliente.getId());
    	}
    	this.m_clientes_eliminar.clear();
    }

    public static void showMemoryStatus(String msg) {
    	System.out.println("total=" + (int) (Runtime.getRuntime().totalMemory() / 1024) +
    			" KB free=" + (int) (Runtime.getRuntime().freeMemory() / 1024) +
    			//" KB max=" + (int) (Runtime.getRuntime().maxMemory() / 1024) +
				" KB [" + msg + "]");
    }
    
    /** Load maps / Cargar los m_mapas */
    private void loadMaps(boolean loadBackup) {
        this.m_mapas = new Vector<Map>();
        Map mapa;
        for (short i = 1; i <= CANT_MAPAS; i++) {
            mapa = new Map(i, this);
            mapa.load(loadBackup);
            this.m_mapas.add(mapa);
        }
    }
    
    /** Load objects / Cargar los m_objetos */
    private void loadObjects() {
        this.m_objetos = new Vector<ObjectInfo>();
        IniFile ini = new IniFile();
        try {
            ini.load(DATDIR + java.io.File.separator + "Obj.dat");
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        int cant = ini.getInt("INIT", "NumOBJs");
        
        for (short i = 1; i <= cant; i++) {
            ObjectInfo obj = new ObjectInfo();
            obj.load(ini, i);
            this.m_objetos.add(obj);
        }
    }
    
    /** Load spells / Cargar los hechizos */
    private void loadSpells() {
        this.m_hechizos = new Vector<Spell>();
        IniFile ini = new IniFile();
        try {
            ini.load(DATDIR + java.io.File.separator + "Hechizos.dat");
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        int cant = ini.getShort("INIT", "NumeroHechizos");
        
        for (int i = 0; i < cant; i++) {
            Spell hechizo = new Spell(i+1);
        	hechizo.load(ini);
            this.m_hechizos.add(hechizo);
        }
    }
    
    private void cargarQuests() {
        // Obtiene el numero de quest
        IniFile ini = new IniFile();
        try {
            ini.load(DATDIR + java.io.File.separator + "Quests.dat");
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        short cant = ini.getShort("INIT", "NumQuests");
        this.m_quests = new Vector<Quest>();        
        // Llena la lista
        for (short i = 1; i <= cant; i++) {
            Quest quest = new Quest(this, i);
            quest.load(ini);
            this.m_quests.add(quest);
        }
    }

    /** Load all initial data / Cargar todos los datos iniciales */
    private void loadAllData(boolean loadBackup) {
    	showMemoryStatus("loadAllData() start");
        loadObjects();
    	showMemoryStatus("loadObjects() ready");
        loadSpells();
    	showMemoryStatus("loadSpells ready");
        loadMaps(loadBackup);
    	showMemoryStatus("loadMaps ready");
        cargarQuests();
    	showMemoryStatus("cargarQuests ready");
        loadCiudades();
    	showMemoryStatus("loadCiudades ready");
        loadArmasHerreria();
    	showMemoryStatus("loadArmasHerreria ready");
        loadArmadurasHerreria();
    	showMemoryStatus("loadArmadurasHerreria ready");
        loadObjCarpintero();
    	showMemoryStatus("loadObjCarpintero ready");
        loadAdmins();
    	showMemoryStatus("loadAdmins ready");
        loadNombresInvalidos();
    	showMemoryStatus("loadNombresInvalidos ready");
        cargarSpawnList();
    	showMemoryStatus("cargarSpawnList ready");
        cargarApuestas();
    	showMemoryStatus("cargarApuestas ready");
        loadMOTD();
    	showMemoryStatus("loadMOTD ready");
        //System.gc(); // Para decirle a la VM que es buen momento para liberar algo de memoria.
    	showMemoryStatus("loadAllData() ended");
    }
    
    public Npc crearNPC(int npc_ind, boolean loadBackup) {
        Npc npc = new Npc(npc_ind, loadBackup, this);
        this.m_npcs.put(new Integer(npc.getId()), npc);
        return npc;
    }
    
    public void eliminarNPC(Npc npc) {
        this.m_npcs_muertos.add(npc.getId());
    }
    
    public Npc getNPC(int npcId) {
        return this.m_npcs.get(new Integer(npcId));
    }
    
    public Client getCliente(short id) {
        return this.m_clientes.get(id);
    }
    
    public ObjectInfo getInfoObjeto(int objid) {
        return this.m_objetos.get(objid - 1);
    }
    
    public Spell getHechizo(int spell) {
        return this.m_hechizos.get(spell - 1);
    }
    
    public Map getMapa(int mapa) {
        if (mapa > 0 && mapa <= this.m_mapas.size()) {
			return this.m_mapas.get(mapa - 1);
		}
		return null;
    }
    
    public Client getUsuario(String nombre) {
    	if ("".equals(nombre)) {
			return null;
		}
    	for (Client cliente: getClientes()) {
            if (cliente.getNick().equalsIgnoreCase(nombre)) {
                return cliente;
            }
        }
        return null;
    }
    
    public boolean usuarioYaConectado(Client cliente) {
    	for (Client c: getClientes()) {
            if (cliente != c && cliente.getNick().equalsIgnoreCase(c.getNick())) {
                return true;
            }
        }
        return false;
    }
    
    private void doAI() {
        // TIMER_AI_Timer()
        if (!this.m_haciendoBackup) {
            // Update NPCs
            Vector<Npc> npcs = new Vector<Npc>(getNpcs());
            for (Npc npc: npcs) {
                if (npc.estaActivo()) { // Nos aseguramos que sea INTELIGENTE!
                    if (npc.estaParalizado()) {
                        npc.efectoParalisisNpc();
                    } else {
                        // Usamos AI si hay algun user en el mapa
                        if (npc.getPos().isValid()) {
                            Map mapa = getMapa(npc.getPos().mapa);
                            if (mapa != null && mapa.getCantUsuarios() > 0) {
                                if (npc.m_movement != Npc.MOV_ESTATICO) {
                                    npc.doAI();
                                }
                            }
                        }
                    }
                }
            }
            for (Object element : this.m_npcs_muertos) {
                this.m_npcs.remove(element);
            }
            this.m_npcs_muertos.clear();
        }
    }

    private void cargarSpawnList() {
        // Public Sub CargarSpawnList()
        try {
            IniFile ini = new IniFile(DATDIR + File.separator + "Invokar.dat");
            short cant = ini.getShort("INIT", "NumNPCs");
            this.m_spawnList = new short[cant];
            this.m_spawnListNames = new String[cant];
            for (int i = 0; i < cant; i++) {
                this.m_spawnList[i] = ini.getShort("LIST", "NI" + (i+1));
                this.m_spawnListNames[i] = ini.getString("LIST", "NN" + (i+1));
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadAdmins() {
        try {
            // Limpiar las listas de admins.
            this.m_dioses.clear();
            this.m_semidioses.clear();
            this.m_consejeros.clear();
            // Cargar dioses:
            IniFile ini = new IniFile(DATDIR + java.io.File.separator + "Server.ini");
            short cant = ini.getShort("DIOSES", "Cant");
            for (int i = 1; i <= cant; i++) {
                String nombre = ini.getString("DIOSES", "Dios"+i, "").toUpperCase();
                if (!"".equals(nombre)) {
					this.m_dioses.add(nombre);
				}
            }
            // Cargar semidioses:
            cant = ini.getShort("SEMIDIOSES", "Cant");
            for (int i = 1; i <= cant; i++) {
                String nombre = ini.getString("SEMIDIOSES", "Semidios"+i, "").toUpperCase();
                if (!"".equals(nombre)) {
					this.m_semidioses.add(nombre);
				}
            }
            // Cargar consejeros:
            cant = ini.getShort("CONSEJEROS", "Cant");
            for (int i = 1; i <= cant; i++) {
                String nombre = ini.getString("CONSEJEROS", "Consejero"+i, "").toUpperCase();
                if (!"".equals(nombre)) {
					this.m_consejeros.add(nombre);
				}
            }
            // Armaduras Faccionarias:
            Factions.ArmaduraImperial1 = ini.getShort("INIT", "ArmaduraImperial1");
			Factions.ArmaduraImperial2 = ini.getShort("INIT", "ArmaduraImperial2");
			Factions.ArmaduraImperial3 = ini.getShort("INIT", "ArmaduraImperial3");
			Factions.TunicaMagoImperial = ini.getShort("INIT", "TunicaMagoImperial");
			Factions.TunicaMagoImperialEnanos = ini.getShort("INIT", "TunicaMagoImperialEnanos");
			Factions.ArmaduraCaos1 = ini.getShort("INIT", "ArmaduraCaos1");
			Factions.ArmaduraCaos2 = ini.getShort("INIT", "ArmaduraCaos2");
			Factions.ArmaduraCaos3 = ini.getShort("INIT", "ArmaduraCaos3");
			Factions.TunicaMagoCaos = ini.getShort("INIT", "TunicaMagoCaos");
			Factions.TunicaMagoCaosEnanos = ini.getShort("INIT", "TunicaMagoCaosEnanos");
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadNombresInvalidos() {
        this.m_nombresInvalidos.clear();
        try {
            BufferedReader f = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(DATDIR + File.separator + "NombresInvalidos.txt")));
            try {
                String str = f.readLine();
                if (str == null) {
					return;
				}
                str = str.trim().toUpperCase();
                if (!"".equals(str)) {
					this.m_nombresInvalidos.add(str);
				}
            } finally {
                f.close();
            }
        } catch (java.io.FileNotFoundException e) {
            Log.serverLogger().warning("Error abriendo archivo de nombres inv�lidos");
            e.printStackTrace();
        } catch (java.io.IOException e) {
            Log.serverLogger().warning("Error leyendo archivo de nombres inv�lidos");
            e.printStackTrace();
        }
    }
    
    public boolean esDios(String name) {
        return this.m_dioses.contains(name.toUpperCase());
    }
    
    public boolean esSemiDios(String name) {
        return this.m_semidioses.contains(name.toUpperCase());
    }
    
    public boolean esConsejero(String name) {
        return this.m_consejeros.contains(name.toUpperCase());
    }
    
    public boolean nombrePermitido(String nombre) {
        return (!this.m_nombresInvalidos.contains(nombre.toUpperCase()));
    }
    
    private void pasarSegundo() {
        Vector<Client> paraSalir = new Vector<Client>();
        for (Client cli: getClientes()) {
            if (cli.m_counters.Saliendo) {
                cli.m_counters.SalirCounter--;
                if (cli.m_counters.SalirCounter <= 0) {
                    paraSalir.add(cli);
                } else {
                	switch (cli.m_counters.SalirCounter) {
                		case 10:
                			cli.enviarMensaje("En " + cli.m_counters.SalirCounter +" segundos se cerrar� el juego...", FontType.INFO);
                			break;
                		case 3:
                			cli.enviarMensaje("Gracias por jugar Argentum Online. Vuelve pronto.", FontType.INFO);
                			break;
                	}
                }
            }
        }
        for (Client cli: paraSalir) {
            cli.doSALIR();
        }
    }
        
    public void guardarUsuarios() {
        this.m_haciendoBackup = true;
        try {
            enviarATodos(MSG_BKW);
            enviarATodos(MSG_TALK, "Servidor> Grabando Personajes" + FontType.SERVER);
            for (Client cli: getClientes()) {
                if (cli.isLogged()) {
                    cli.saveUser();
                }
            }
            enviarATodos(MSG_TALK, "Servidor> Personajes Grabados" + FontType.SERVER);
            enviarATodos(MSG_BKW);
        } finally {
            this.m_haciendoBackup = false;
        }
    }
    
 /*  fixme - fixme - fixme - fixme - fixme - fixme - fixme - 
    Sub InicializaEstadisticas()
        Dim Ta As Long
        Ta = GetTickCount()
        Call EstadisticasWeb.Inicializa(frmMain.hWnd)
        Call EstadisticasWeb.Informar(CANTIDAD_MAPAS, NumMaps)
        Call EstadisticasWeb.Informar(CANTIDAD_ONLINE, NumUsers)
        Call EstadisticasWeb.Informar(UPTIME_SERVER, (Ta - startTime) / 1000)
        Call EstadisticasWeb.Informar(RECORD_USUARIOS, recordusuarios)
    End Sub
 */
    
    private void loadCiudades() {
        try {
            IniFile ini = new IniFile(DATDIR + File.separator + "Ciudades.dat");
            this.m_ciudades = new WorldPos[4];
            this.m_ciudades[CIUDAD_NIX] = new WorldPos(ini.getShort("NIX", "MAPA"), ini.getShort("NIX", "X"), ini.getShort("NIX", "Y"));
            this.m_ciudades[CIUDAD_ULLA] = new WorldPos(ini.getShort("Ullathorpe", "MAPA"), ini.getShort("Ullathorpe", "X"), ini.getShort("Ullathorpe", "Y"));
            this.m_ciudades[CIUDAD_BANDER] = new WorldPos(ini.getShort("Banderbill", "MAPA"), ini.getShort("Banderbill", "X"), ini.getShort("Banderbill", "Y"));
            this.m_ciudades[CIUDAD_LINDOS] = new WorldPos(ini.getShort("Lindos", "MAPA"), ini.getShort("Lindos", "X"), ini.getShort("Lindos", "Y"));
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    public WorldPos getCiudadPos(short ciudad) {
        return this.m_ciudades[ciudad];
    }
    
    // -------> interval 200
    private void FX_Timer() {
    	for (Map mapa: this.m_mapas) {
            if ((Util.Azar(1, 150) < 12) && (mapa.getCantUsuarios() > 0)) {
                mapa.doFX();
            }
        }
    }
    
    // ---------> Interval 40
    private void gameTimer() {
        // <<<<<< Procesa eventos de los usuarios >>>>>>
    	for (Client cli: getClientes()) {
            // Conexion activa?
            if (cli != null && cli.getId() > 0) {
                cli.procesarEventos();
            }
        }
    }
    
    /// ---------> Interval 4000
    private void npcAtacaTimer() {
    	for (Npc npc: getNpcs()) {
            npc.setPuedeAtacar(true);
        }
    }
    
    // Interval = 3000
    private void timerOculto() {
    	for (Client cli: getClientes()) {
            if (cli != null && cli.getId() > 0) {
                if (cli.estaOculto()) {
					cli.doPermanecerOculto();
				}
            }
        }
    }
    
    //       Interval        =   60000
    private void lluviaTimer() {
        if (!this.m_lloviendo) {
			return;
		}
        for (Client cli: getClientes()) {
            if (cli != null && cli.getId() > 0) {
                cli.efectoLluvia();
            }
        }
    }
    
    public void enviarATodos(ClientMessage msg, Object... params) {
    	for (Client cli: getClientes()) {
            // Conexion activa?
            if (cli != null && cli.getId() > 0 && cli.isLogged()) {
                cli.enviar(msg, params);
            }
        }
    }
    
    public void enviarAAdmins(ClientMessage msg, Object... params) {
    	for (Client cli: getClientes()) {
            // Conexion activa?
            if (cli != null && cli.getId() > 0 && cli.esGM() && cli.isLogged()) {
                cli.enviar(msg, params);
            }
        }
    }
    
    public void enviarMensajeAAdmins(String msg, FontType fuente) {
    	for (Client cli: getClientes()) {
            // Conexion activa?
            if (cli != null && cli.getId() > 0 && cli.esGM() && cli.isLogged()) {
                cli.enviarMensaje(msg, fuente);
            }
        }
    }
    
    long minutosLloviendo = 0;
    long minutosSinLluvia = 0;
    
    public void iniciarLluvia() {
        this.m_lloviendo = true;
        this.minutosSinLluvia = 0;
        enviarATodos(MSG_LLU);
    }
    
    public void detenerLluvia() {
        this.m_lloviendo = false;
        this.minutosSinLluvia = 0;
        enviarATodos(MSG_LLU);
    }
    
    //       Interval        =   60000
    private void lluviaEvent() {
        // Private Sub tLluviaEvent_Timer()
        if (!this.m_lloviendo) {
            this.minutosSinLluvia++;
            if (this.minutosSinLluvia >= 15 && this.minutosSinLluvia < 1440) {
                if (Util.Azar(1, 100) <= 10) {
                    iniciarLluvia();
                }
            } else if (this.minutosSinLluvia >= 1440) {
                iniciarLluvia();
            }
        } else {
            this.minutosLloviendo++;
            if (this.minutosLloviendo >= 5) {
                detenerLluvia();
            } else {
                if (Util.Azar(1, 100) <= 7) {
                    detenerLluvia();
                }
            }
        }
    }
    
    int segundos = 0;
    //       Interval        =   6000
    public void piqueteTimer() {
        // Private Sub tPiqueteC_Timer()
        this.segundos += 6;
        for (Client cli: getClientes()) {
            if (cli != null && cli.getId() > 0 && cli.isLogged()) {
                Map mapa = getMapa(cli.getPos().mapa);
                if (mapa.getTrigger(cli.getPos().x, cli.getPos().y) == 5) {
                    cli.m_counters.PiqueteC++;
                    cli.enviarMensaje("Estas obstruyendo la via p�blica, mu�vete o ser�s encarcelado!!!", FontType.INFO);
                    if (cli.m_counters.PiqueteC > 23) {
                        cli.m_counters.PiqueteC = 0;
                        cli.encarcelar(3, null);
                    }
                } else {
                    if (cli.m_counters.PiqueteC > 0) {
						cli.m_counters.PiqueteC = 0;
					}
                }
                if (this.segundos >= 18) {
                    cli.m_counters.Pasos = 0;
                }
            }
        }
        if (this.segundos >= 18) {
			this.segundos = 0;
		}
    }
    
    public void purgarPenas() {
    	for (Client cli: getClientes()) {
            if (cli != null && cli.getId() > 0 && cli.getFlags().UserLogged) {
                if (cli.m_counters.Pena > 0) {
                    cli.m_counters.Pena--;
                    if (cli.m_counters.Pena < 1) {
                        cli.m_counters.Pena = 0;
                        cli.warpUser(WP_LIBERTAD.mapa, WP_LIBERTAD.x, WP_LIBERTAD.y, true);
                        cli.enviarMensaje("Has sido liberado!", FontType.INFO);
                    }
                }
            }
        }
    }
    
    private void checkIdleUser() {
    	for (Client cliente: getClientes()) {
            // Conexion activa?
            if (cliente != null && 
            		cliente.getId() > 0 && 
            		cliente.isLogged()) {
                // Actualiza el contador de inactividad
                cliente.m_counters.IdleCount++;
                if (cliente.m_counters.IdleCount >= IdleLimit) {
                    cliente.enviarError("Demasiado tiempo inactivo. Has sido desconectado.");
                    cliente.doSALIR();
                }
            }
        }
    }
    
    long minutos = 0;
    long minsRunning = 0;
    long minutosLatsClean = 0;
    long minsSocketReset  = 0;
    long minsPjesSave = 0;
    long horas = 0;
    DailyStats dayStats = new DailyStats();
    
    private void autoSaveTimer() {
        //Private Sub AutoSave_Timer()
        // fired every minute
        this.minsRunning++;
        if (this.minsRunning == 60) {
            this.horas++;
            if (this.horas == 24) {
                saveDayStats();
                this.dayStats.reset();
                getGuildMngr().dayElapsed();
                this.horas = 0;
            }
            this.minsRunning = 0;
        }
        this.minutos++;
        if (this.minutos >= IntervaloMinutosWs) {
            doBackup();
            ///////// fixme
            // aClon.VaciarColeccion();
            this.minutos = 0;
        }
        if (this.minutosLatsClean >= 15) {
            this.minutosLatsClean = 0;
            reSpawnOrigPosNpcs(); // respawn de los guardias en las pos originales
            limpiarMundo();
        } else {
            this.minutosLatsClean++;
        }
        purgarPenas();
        checkIdleUser();
        // <<<<<-------- Log the number of users online ------>>>
        Log.serverLogger().info("Usuarios en linea: " + getUsuariosConectados().size());
        // <<<<<-------- Log the number of users online ------>>>
    }

    private void loadArmasHerreria() {
        try {
            IniFile ini = new IniFile(DATDIR + File.separator + "ArmasHerrero.dat");
            short cant = ini.getShort("INIT", "NumArmas");
            this.m_armasHerrero = new short[cant];
            //Log.debug("ArmasHerreria cantidad=" + cant);
            for (int i = 0; i < cant; i++) {
                this.m_armasHerrero[i] = ini.getShort("Arma" + (i+1), "Index");
                //Log.debug("ArmasHerrero[" + i + "]=" + m_armasHerrero[i]);
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void loadArmadurasHerreria() {
        try {
            IniFile ini = new IniFile(DATDIR + File.separator + "ArmadurasHerrero.dat");
            short cant = ini.getShort("INIT", "NumArmaduras");
            //Log.debug("ArmadurasHerrero cantidad=" + cant);
            this.m_armadurasHerrero = new short[cant];
            for (int i = 0; i < cant; i++) {
                this.m_armadurasHerrero[i] = ini.getShort("Armadura" + (i+1), "Index");
                //Log.debug("ArmadurasHerrero[" + i + "]=" + m_armadurasHerrero[i]);
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void loadObjCarpintero() {
        try {
            IniFile ini = new IniFile(DATDIR + File.separator + "ObjCarpintero.dat");
            short cant = ini.getShort("INIT", "NumObjs");
            //Log.debug("ObjCarpintero cantidad=" + cant);
            this.m_objCarpintero = new short[cant];
            for (int i = 0; i < cant; i++) {
                this.m_objCarpintero[i] = ini.getShort("Obj" + (i+1), "Index");
                //Log.debug("ObjCarpintero[" + i + "]=" + m_objCarpintero[i]);
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    public String[] getHelp() {
        // Sub SendHelp(ByVal Index As Integer)
        String[] lineas = null;
        try {
            IniFile ini = new IniFile(DATDIR + File.separator + "Help.dat");
            short cant = ini.getShort("INIT", "NumLines");
            lineas = new String[cant];
            for (int i = 0; i < cant; i++) {
                lineas[i] = ini.getString("Help", "Line" + (i+1));
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return lineas;
    }

    public void enviarMensajeALosGMs(String msg) {
        for (Client cli: getClientes()) {
            if (cli.isLogged() && cli.esGM()) {
                cli.enviarMensaje(msg, FontType.TALKGM);
            }
        }
    }

    public void logBan(String bannedUser, String gm, String motivo) {
        /////////// FIXME
        /////////// FIXME
        /////////// FIXME
        // Sub LogBan(ByVal BannedIndex As Integer, ByVal UserIndex As Integer, ByVal motivo As String)
        System.out.println(" BAN: " + bannedUser + " by " + gm + " reason: " + motivo);
        //Call WriteVar(App.Path & "\logs\" & "BanDetail.dat", UserList(BannedIndex).Name, "BannedBy", UserList(UserIndex).Name)
        //Call WriteVar(App.Path & "\logs\" & "BanDetail.dat", UserList(BannedIndex).Name, "Reason", motivo)
        //'Log interno del servidor, lo usa para hacer un UNBAN general de toda la gente banned
        //Dim mifile As Integer
        //mifile = FreeFile
        //Open App.Path & "\logs\GenteBanned.log" For Append Shared As #mifile
        //Print #mifile, UserList(BannedIndex).Name
        //Close #mifile
    }
    
    public void unBan(String usuario) {
        /////////// FIXME
        /////////// FIXME
        /////////// FIXME
        // Public Function UnBan(ByVal Name As String) As Boolean
        System.out.println(" UNBAN: " + usuario);
        // Unban the character
        // Call WriteVar(App.Path & "\charfile\" & Name & ".chr", "FLAGS", "Ban", "0")
        // Remove it from the banned people database
        // Call WriteVar(App.Path & "\logs\" & "BanDetail.dat", Name, "BannedBy", "NOBODY")
        // Call WriteVar(App.Path & "\logs\" & "BanDetail.dat", Name, "Reason", "NOONE")
    }
  
    //--- fixme ------ fixme ------ fixme ------ fixme ------ fixme ------ fixme ---
    public void doBackup() {
        // Public Sub DoBackUp()
        this.m_haciendoBackup = true;
        enviarATodos(MSG_BKW);
        enviarATodos(MSG_TALK, "Servidor> Realizando WorldSave..." + FontType.SERVER);
        saveGuildsDB();
        limpiarMundo();
        worldSave();
        enviarATodos(MSG_TALK, "Servidor> WorldSave terminado." + FontType.SERVER);
        enviarATodos(MSG_BKW);
        /*********** FIXME 
        estadisticasWeb.Informar(EVENTO_NUEVO_CLAN, 0)
         ******************/
        this.m_haciendoBackup = false;
        Log.serverLogger().info("Backup completado con exito");
    }
    
    private void saveGuildsDB() {
        /////////// fixme - fixme - fixme
    }
    
    public void limpiarMundo(Client cli) {
    	int cant = limpiarMundo();
        if (cli != null) {
			cli.enviarMensaje("Servidor> Limpieza del mundo completa. Se eliminaron " + cant + " m_objetos.", FontType.SERVER);
		}
    }
    
    private int limpiarMundo() {
        //Sub LimpiarMundo()
    	int cant = 0;
    	for (WorldPos pos: this.m_trashCollector) {
            Map mapa = getMapa(pos.mapa);
            mapa.quitarObjeto(pos.x, pos.y);
            cant++;
        }
        this.m_trashCollector.clear();
        return cant;
    }
    
    Feedback m_feedback = new Feedback();
    
    private synchronized void worldSave() {
        // Sub WorldSave()
        //enviarATodos("||%%%% POR FAVOR ESPERE, INICIANDO WORLDSAVE %%%%" + FontType.INFO);
        // Hacer un respawn de los guardias en las pos originales.
        reSpawnOrigPosNpcs();
        // Ver cuantos m_mapas necesitan backup.
        int cant = 0;
        for (Map mapa: this.m_mapas) {
            if (mapa.m_backup) {
				cant++;
			}
        }
        // Guardar los m_mapas
        this.m_feedback.init("Guardando m_mapas modificados", cant);
        int i = 0;
        for (Map mapa: this.m_mapas) {
            if (mapa.m_backup) {
                mapa.saveMapData();
                this.m_feedback.step("Mapa " + (++i));
            }
        }
        this.m_feedback.finish();
        // Guardar los NPCs
        try {
            IniFile ini = new IniFile();
            for (Npc npc: getNpcs()) {
                if (npc.getBackup()) {
                    npc.backup(ini);
                }
            }
            // Guardar todo
            ini.store("worldBackup" + File.separator + "backNPCs.dat");
        } catch (Exception e) {
            Log.serverLogger().log(Level.SEVERE, "worldSave(): ERROR EN BACKUP NPCS", e);
        }
        //enviarATodos("||%%%% WORLDSAVE LISTO %%%%" + FontType.INFO);
    }

    
    private void reSpawnOrigPosNpcs() {
        // Sub ReSpawnOrigPosNpcs()
        List<Npc> spawnNPCs = new Vector<Npc>();
        for (Npc npc: getNpcs()) {
            if (npc.estaActivo()) {
                if (npc.getNumero() == GUARDIAS && npc.getOrig().isValid()) {
                    npc.quitarNPC(); // fixme, lo elimina del server??? revisar.
                    spawnNPCs.add(npc);
                } else if (npc.getContadores().TiempoExistencia > 0) {
                    npc.muereNpc(null);
                }
            }
        }
        for (Npc npc: spawnNPCs) {
            //npc.reSpawnNpc(npc.getOrig()); // TODO: PROBAR !!!
            npc.reSpawnNpc();
        }
    }    

    private void saveDayStats() {
        // Public Sub SaveDayStats()
        SimpleDateFormat df_dia = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat df_xml = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat df_hora = new SimpleDateFormat("HH:mm:ss");
        java.util.Date fecha = new java.util.Date();
        String dia = df_dia.format(fecha);
        String hora = df_hora.format(fecha);
        String filename = "logs" + File.separator + "stats-" + df_xml.format(fecha) + ".xml";
        BufferedWriter f = null;
        try {
            f = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true)));
            f.write("<stats>");
            f.write("<ao>");
            f.write("<dia>" + dia + "</dia>");
            f.write("<hora>" + hora + "</hora>");
            f.write("<segundos_total>" + this.dayStats.segundos + "</segundos_total>");
            f.write("<max_user>" + this.dayStats.maxUsuarios + "</max_user>");
            f.write("</ao>");
            f.write("</stats>\n");
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();    
        } finally {
            try { if (f != null) {
				f.close();
			} } 
            catch (java.io.IOException e) { e.printStackTrace(); }
        }
    }
    
    public void ponerMensajeForo(String foroId, String titulo, String texto) {
        Forum forum = this.m_foros.get(foroId);
        if (forum == null) {
            this.m_foros.put(foroId, (forum = new Forum(foroId)));
        }
        forum.addMessage(titulo, texto);
    }
    
    public void enviarMensajesForo(String foroId, Client cliente) {
        Forum forum = this.m_foros.get(foroId);
        if (forum == null) {
            this.m_foros.put(foroId, (forum = new Forum(foroId)));
        }
        // Enviar mensajes dejados en el foro:
        for (int i = 1; i <= forum.messageCount(); i++) {
            ForumMessage msg = forum.getMessage(i);
            cliente.enviar(MSG_FMSG, msg.getTitle(), msg.getBody());
        }
        // Enviar fin de foro:
        cliente.enviar(MSG_MFOR);
    }

    private void cargarApuestas() {
        /* fixme
        Apuestas.Ganancias = val(GetVar(DatPath & "apuestas.dat", "Main", "Ganancias"))
        Apuestas.Perdidas = val(GetVar(DatPath & "apuestas.dat", "Main", "Perdidas"))
        Apuestas.Jugadas = val(GetVar(DatPath & "apuestas.dat", "Main", "Jugadas"))
        */
    }

    private void loadMOTD() {
        try {
            String msg;
            this.m_motd.clear();
            IniFile ini = new IniFile(DATDIR + java.io.File.separator + "Motd.ini");
            short cant = ini.getShort("INIT", "NumLines");
            for (int i = 1; i <= cant; i++) {
                msg = ini.getString("MOTD", "Line"+i, "");
                this.m_motd.add(msg);
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    public void guardarMotd() {
        try {
            IniFile ini = new IniFile();
            ini.setValue("INIT", "NumLines", this.m_motd.size());
            int i = 0;
            for (Object element : this.m_motd) {
                ini.setValue("MOTD", "Line"+(++i), (String)element);
            }
            ini.store(DATDIR + java.io.File.separator + "Motd.ini");
        } catch (Exception e) {
            Log.serverLogger().log(Level.SEVERE, "ERROR EN guardarMOTD()", e);
        }
    }
    
/*
    Public Function MD5ok(ByVal md5formateado As String) As Boolean
        Dim i As Integer
        If MD5ClientesActivado = 1 Then
            For i = 0 To UBound(MD5s)
                If (md5formateado = MD5s(i)) Then
                    MD5ok = True
                    Exit Function
                End If
            Next i
            MD5ok = False
        Else
            MD5ok = True
        End If
    End Function

    Public Sub MD5sCarga()
        Dim LoopC As Integer
        MD5ClientesActivado = val(GetVar(IniPath & "Server.ini", "MD5Hush", "Activado"))
        If MD5ClientesActivado = 1 Then
            ReDim MD5s(val(GetVar(IniPath & "Server.ini", "MD5Hush", "MD5Aceptados")))
            For LoopC = 0 To UBound(MD5s)
                MD5s(LoopC) = GetVar(IniPath & "Server.ini", "MD5Hush", "MD5Aceptado" & (LoopC + 1))
                MD5s(LoopC) = txtOffset(hexMd52Asc(MD5s(LoopC)), 53)
            Next LoopC
        End If
    End Sub

    Public Sub BanIpAgrega(ByVal ip As String)
        BanIps.Add ip
        Call BanIpGuardar
    End Sub

    Public Function BanIpBuscar(ByVal ip As String) As Long
        Dim Dale As Boolean
        Dim LoopC As Long
        Dale = True
        LoopC = 1
        Do While LoopC <= BanIps.Count And Dale
            Dale = (BanIps.Item(LoopC) <> ip)
            LoopC = LoopC + 1
        Loop
        If Dale Then
            BanIpBuscar = 0
        Else
            BanIpBuscar = LoopC
        End If
    End Function

    Public Function BanIpQuita(ByVal ip As String) As Boolean
        On Error Resume Next
        Dim N As Long
        N = BanIpBuscar(ip)
        If N > 0 Then
            BanIps.Remove N
            BanIpQuita = True
        Else
            BanIpQuita = False
        End If
    End Function

    Public Sub BanIpGuardar()
        Dim ArchivoBanIp As String
        Dim ArchN As Long
        Dim LoopC As Long
        ArchivoBanIp = App.Path & "\Dat\BanIps.dat"
        ArchN = FreeFile()
        Open ArchivoBanIp For Output As #ArchN
        For LoopC = 1 To BanIps.Count
            Print #ArchN, BanIps.Item(LoopC)
        Next LoopC
        Close #ArchN
    End Sub

    Public Sub BanIpCargar()
        Dim ArchN As Long
        Dim Tmp As String
        Dim ArchivoBanIp As String
        ArchivoBanIp = App.Path & "\Dat\BanIps.dat"
        Do While BanIps.Count > 0
            BanIps.Remove 1
        Loop
        ArchN = FreeFile()
        Open ArchivoBanIp For Input As #ArchN
        Do While Not EOF(ArchN)
            Line Input #ArchN, Tmp
            BanIps.Add Tmp
        Loop
        Close #ArchN
    End Sub

    Public Sub ActualizaEstadisticasWeb()
        Static Andando As Boolean
        Static Contador As Long
        Dim Tmp As Boolean
        Contador = Contador + 1
        If Contador >= 10 Then
            Contador = 0
            Tmp = EstadisticasWeb.EstadisticasAndando()
            If Andando = False And Tmp = True Then
                Call InicializaEstadisticas
            End If
            Andando = Tmp
        End If
    End Sub

    Public Sub ActualizaStatsES()
        Static TUlt As Single
        Dim Transcurrido As Single
        Transcurrido = Timer - TUlt
        If Transcurrido >= 5 Then
            TUlt = Timer
            With TCPESStats
                .BytesEnviadosXSEG = CLng(.BytesEnviados / Transcurrido)
                .BytesRecibidosXSEG = CLng(.BytesRecibidos / Transcurrido)
                .BytesEnviados = 0
                .BytesRecibidos = 0
                If .BytesEnviadosXSEG > .BytesEnviadosXSEGMax Then
                    .BytesEnviadosXSEGMax = .BytesEnviadosXSEG
                    .BytesEnviadosXSEGCuando = CDate(Now)
                End If
                If .BytesRecibidosXSEG > .BytesRecibidosXSEGMax Then
                    .BytesRecibidosXSEGMax = .BytesRecibidosXSEG
                    .BytesRecibidosXSEGCuando = CDate(Now)
                End If
                If frmEstadisticas.Visible Then
                    Call frmEstadisticas.ActualizaStats
                End If
            End With
        End If
    End Sub

    Public Function UserDarPrivilegioLevel(ByVal Name As String) As Long
        If EsDios(Name) Then
            UserDarPrivilegioLevel = 3
        ElseIf EsSemiDios(Name) Then
            UserDarPrivilegioLevel = 2
        ElseIf EsConsejero(Name) Then
            UserDarPrivilegioLevel = 1
        Else
            UserDarPrivilegioLevel = 0
        End If
    End Function
 */
    
}

