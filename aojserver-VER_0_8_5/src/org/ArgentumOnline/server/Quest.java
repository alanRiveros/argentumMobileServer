/**
 * Quest.java
 *
 * Created on 21 de marzo de 2004, 12:19
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

import org.ArgentumOnline.server.util.*;
import java.util.*;
import java.util.logging.Level;

/**
 *
 * @author  Pablo Fernando Lillia
 */
public class Quest implements Constants {
    
    short nroQuest;
    
    boolean DaObj = false;
    boolean DaExp = false;
    boolean DaOro = false;

    short Obj = 0;
    int Exp = 0;
    int Oro = 0;
    
    short CriaturaIndex = 0;

    final static int CANT_NPCS = 5;
    WorldPos Coordenadas[] = new WorldPos[CANT_NPCS];
    String Pista[] = new String[CANT_NPCS];
    
    short Objetivo = 0;
    short NPCs = 0;
    short Usuarios = 0;
    short Criminales = 0;
    short Ciudadanos = 0;
    short AmigoNpc = 0;

    /** Matar npcs */
    public final static short OBJETIVO_MATAR_NPCS = 1; 
    /** Matar usuarios (Neutral), crimis (Armada) o ciudadanos (Caos). */
    public final static short OBJETIVO_MATAR_USUARIOS = 2;
    /** Encontrar un npc */
    public final static short OBJETIVO_ENCONTRAR_NPC = 3; 
    /** Matar a un npc espec�fico */
    public final static short OBJETIVO_MATAR_UN_NPC = 4;

    AojServer server;
    
    /** Creates a new instance of Quest */
    public Quest(AojServer server, short nroQuest) {
    	this.server = server;
        this.nroQuest = nroQuest;
    }
    
    public short getNroQuest() {
        return this.nroQuest;
    }
    
    @Override
	public String toString() {
        return "Quest(nro=" + this.nroQuest + ",objetivo=" + this.Objetivo + ")";
    }
    
    public void load(IniFile ini) {
        String seccion = "Quest" + this.nroQuest;
        for (short j = 0; j < CANT_NPCS; j++) {
            String s = ini.getString(seccion, "Coordenadas" + (j+1));
            if (s.length() == 0) {
                break;
            }
            try {
                StringTokenizer st = new StringTokenizer(s, "-");
                short mapa = Short.parseShort(st.nextToken());
                short x = Short.parseShort(st.nextToken());
                short y = Short.parseShort(st.nextToken());
                this.Coordenadas[j] = new WorldPos(mapa, x, y);
                this.Pista[j] = s;
            } catch (Exception e) {
                Log.serverLogger().log(Level.SEVERE, "Error en cargarQuests(): quest=" + this.nroQuest + " npc=#" + (j+1), e);
            }
        }                            
        this.CriaturaIndex = ini.getShort(seccion, "CriaturaIndex");
        this.DaExp = (ini.getShort(seccion, "DaExp") == 1);
        this.DaObj = (ini.getShort(seccion, "DaObj") == 1);
        this.DaOro = (ini.getShort(seccion, "DaOro") == 1);
        this.Obj = ini.getShort(seccion, "Obj");
        this.Exp = ini.getInt(seccion, "Exp");
        this.Oro = ini.getInt(seccion, "Oro");
        this.Objetivo = ini.getShort(seccion, "Objetivo");
        this.NPCs = ini.getShort(seccion, "Npcs");
        this.Usuarios = ini.getShort(seccion, "Usuarios");
        this.Ciudadanos = ini.getShort(seccion, "Ciudadanos");
        this.Criminales = ini.getShort(seccion, "Criminales");
        this.AmigoNpc = ini.getShort(seccion, "AmigoNpc");
    }
    
    public void hacerQuest(Client cliente, Npc npc) {
        try {
            if (cliente.esNewbie()) {
                cliente.enviarHabla(COLOR_BLANCO, "Los newbies no pueden realizar estas quests!", npc.getId());
                return;
            }
            if (cliente.getQuest().m_enQuest) {
                cliente.enviarHabla(COLOR_BLANCO, "Debes terminar primero la quest que est�s realizando para empezar otra!", npc.getId());
                return;
            }
            if (cliente.getQuest().m_nroQuest >= this.server.getQuestCount()) {
                cliente.enviarHabla(COLOR_BLANCO, "Ya has realizados todas las quest!", npc.getId());
                return;
            }
            cliente.nextQuest();
            Quest quest = cliente.getQuest().getQuest();
            int azar = 0;
            switch (quest.Objetivo) {
                case OBJETIVO_MATAR_NPCS:
                    cliente.enviarHabla(COLOR_BLANCO, "Debes matar " + this.NPCs + " npcs para recibir tu recompensa!", npc.getId());
                    break;
                case OBJETIVO_MATAR_USUARIOS:
                    if (!cliente.getFaccion().ArmadaReal && !cliente.getFaccion().FuerzasCaos) {
                        cliente.enviarHabla(COLOR_BLANCO, "Debes matar " + this.Usuarios + " usuarios para recibir tu recompensa!", npc.getId());
                    } else if (cliente.getFaccion().ArmadaReal) {
                        cliente.enviarHabla(COLOR_BLANCO, "Debes matar " + this.Criminales + " criminales para recibir tu recompensa!", npc.getId());
                    } else if (cliente.getFaccion().FuerzasCaos) {
                        cliente.enviarHabla(COLOR_BLANCO, "Debes matar " + this.Ciudadanos + " ciudadanos para recibir tu recompensa!", npc.getId());
                    }
                    break;
                case OBJETIVO_ENCONTRAR_NPC:
                    azar = Util.Azar(1, CANT_NPCS);
                    if (this.Coordenadas[azar-1] == null) {
                        return;
                    }                        
                    cliente.enviarHabla(COLOR_BLANCO, "Debes encontrar a mi amigo npc para recibir tu recompensa! Pistas: Se puede encontrar en lugares caracter�sticos del juego, pero apres�rate! Si alguien que est� haciendo este mismo tipo de quest, y lo encuentra antes, puedes perderlo. En tal caso deber�s volver y hacerme clic, y poner /MERINDO", npc.getId());
                    //Npc amigoNpc = 
                    Npc.spawnNpc(this.AmigoNpc, this.Coordenadas[azar-1], true, false);
                    break;
                case OBJETIVO_MATAR_UN_NPC:
                    azar = Util.Azar(1, CANT_NPCS);
                    if (this.Coordenadas[azar-1] == null) {
                        return;
                    }                        
                    cliente.enviarHabla(COLOR_BLANCO, "Debes matar al npc que se encuentra en las coordenadas " + this.Pista[azar-1] + " para recibir tu recompensa! PD: Si no te apresuras, y lo mata otro usuario, perder�s esta quest. En tal caso deber�s volver y hacerme clic, y poner /MERINDO", npc.getId());
                    //Npc criaturaNpc = 
                    Npc.spawnNpc(this.CriaturaIndex, this.Coordenadas[azar-1], true, false);
                    break;
            }
        } catch (Exception e) {
            Log.serverLogger().log(Level.SEVERE, cliente.getNick() + " Error en HacerQuest!", e);
        }
    }

    public void recibirRecompensaQuest(Client cliente) {
        try {
            Npc npc = this.server.getNPC(cliente.getFlags().TargetNpc);
            if (cliente.esNewbie()) {
                cliente.hablar(COLOR_BLANCO, "Los newbies no pueden realizar estas quests!", npc.getId());
                return;
            }
            if (cliente.getQuest().m_nroQuest <= 0) {
                cliente.hablar(COLOR_BLANCO, "No has empezado ninguna Quest!", npc.getId());
                return;
            }
            if (cliente.getQuest().m_enQuest) {
                if (cliente.getQuest().m_recompensa == cliente.getQuest().m_nroQuest) {
                    cliente.hablar(COLOR_BLANCO, "Ya has recibido tu recompensa por esta Quest!", npc.getId());
                    return;
                }
            }
            Quest quest = cliente.getQuest().getQuest();
            switch (quest.Objetivo) {
                case OBJETIVO_MATAR_NPCS:
                    if (cliente.getEstads().NPCsMuertos >= quest.NPCs) {
                        if (quest.DaExp) {
                            cliente.getEstads().addExp(quest.Exp);
                            cliente.checkUserLevel();
                            cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Exp + " puntos de experiencia!", npc.getId());
                            cliente.getQuest().m_realizoQuest = false;
                            cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                            cliente.getQuest().m_enQuest = false;
                        }
                        if (quest.DaOro) {
                            cliente.getEstads().agregarOro(quest.Oro);
                            cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Oro + " monedas de oro!", npc.getId());
                            cliente.getQuest().m_realizoQuest = false;
                            cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                            cliente.getQuest().m_enQuest = false;
                        }
                        if (quest.DaObj) {
                            if (cliente.getInv().agregarItem(quest.Obj, 1) < 1) {
                                Map mapa = this.server.getMapa(cliente.getPos().mapa);
                                mapa.tirarItemAlPiso(cliente.getPos().x, cliente.getPos().y, new InventoryObject(quest.Obj, 1));
                            }
                            cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido un objeto!", npc.getId());
                            cliente.getQuest().m_realizoQuest = false;
                            cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                            cliente.getQuest().m_enQuest = false;
                        }
                    } else {
                        cliente.hablar(COLOR_BLANCO, "Todav�a no has completado el objetivo!", npc.getId());
                    }
                    break;
                case OBJETIVO_MATAR_USUARIOS:
                    if (!cliente.getFaccion().ArmadaReal && !cliente.getFaccion().FuerzasCaos) {
                        if (cliente.getFaccion().CiudadanosMatados + cliente.getFaccion().CriminalesMatados >= quest.Usuarios) {
                            if (quest.DaExp) {
                                cliente.getEstads().addExp(quest.Exp);
                                cliente.checkUserLevel();
                                cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Exp + " puntos de experiencia!", npc.getId());
                                cliente.getQuest().m_realizoQuest = false;
                                cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                                cliente.getQuest().m_enQuest = false;
                            }
                            if (quest.DaOro) {
                                cliente.getEstads().agregarOro(quest.Oro);
                                cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Oro + " monedas de oro!", npc.getId());
                                cliente.getQuest().m_realizoQuest = false;
                                cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                                cliente.getQuest().m_enQuest = false;
                            }
                            if (quest.DaObj) {
                                if (cliente.getInv().agregarItem(quest.Obj, 1) < 1) {
                                    Map mapa = this.server.getMapa(cliente.getPos().mapa);
                                    mapa.tirarItemAlPiso(cliente.getPos().x, cliente.getPos().y, new InventoryObject(quest.Obj, 1));
                                }
                                cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido un objeto!", npc.getId());
                                cliente.getQuest().m_realizoQuest = false;
                                cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                                cliente.getQuest().m_enQuest = false;
                            }
                        } else {
                            cliente.hablar(COLOR_BLANCO, "Todav�a no has completado el objetivo!", npc.getId());
                        }
                    } else if (cliente.getFaccion().ArmadaReal) {
                            if (cliente.getFaccion().CriminalesMatados >= quest.Criminales) {
                                if (quest.DaExp) {
                                    cliente.getEstads().addExp(quest.Exp);
                                    cliente.checkUserLevel();
                                    cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Exp + " puntos de experiencia!", npc.getId());
                                    cliente.getQuest().m_realizoQuest = false;
                                    cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                                    cliente.getQuest().m_enQuest = false;
                                }
                                if (quest.DaOro) {
                                    cliente.getEstads().agregarOro(quest.Oro);
                                    cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Oro + " monedas de oro!", npc.getId());
                                    cliente.getQuest().m_realizoQuest = false;
                                    cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                                    cliente.getQuest().m_enQuest = false;
                                }
                                if (quest.DaObj) {
                                    if (cliente.getInv().agregarItem(quest.Obj, 1) < 1) {
                                        Map mapa = this.server.getMapa(cliente.getPos().mapa);
                                        mapa.tirarItemAlPiso(cliente.getPos().x, cliente.getPos().y, new InventoryObject(quest.Obj, 1));
                                    }
                                    cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido un objeto!", npc.getId());
                                    cliente.getQuest().m_realizoQuest = false;
                                    cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                                    cliente.getQuest().m_enQuest = false;
                                }
                            } else {
                                cliente.hablar(COLOR_BLANCO, "Todav�a no has completado el objetivo!", npc.getId());
                            }
                    } else if (cliente.getFaccion().FuerzasCaos) {
                            if (cliente.getFaccion().CiudadanosMatados >= quest.Ciudadanos) {
                                if (quest.DaExp) {
                                    cliente.getEstads().addExp(quest.Exp);
                                    cliente.checkUserLevel();
                                    cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Exp + " puntos de experiencia!", npc.getId());
                                    cliente.getQuest().m_realizoQuest = false;
                                    cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                                    cliente.getQuest().m_enQuest = false;
                                }
                                if (quest.DaOro) {
                                    cliente.getEstads().agregarOro(quest.Oro);
                                    cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Oro + " monedas de oro!", npc.getId());
                                    cliente.getQuest().m_realizoQuest = false;
                                    cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                                    cliente.getQuest().m_enQuest = false;
                                }
                                if (quest.DaObj) {
                                    if (cliente.getInv().agregarItem(quest.Obj, 1) < 1) {
                                        Map mapa = this.server.getMapa(cliente.getPos().mapa);
                                        mapa.tirarItemAlPiso(cliente.getPos().x, cliente.getPos().y, new InventoryObject(quest.Obj, 1));
                                    }
                                    cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido un objeto!", npc.getId());
                                    cliente.getQuest().m_realizoQuest = false;
                                    cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                                    cliente.getQuest().m_enQuest = false;
                                }
                            } else {
                                cliente.hablar(COLOR_BLANCO, "Todav�a no has completado el objetivo!", npc.getId());
                            }
                    }
                    break;
                case OBJETIVO_ENCONTRAR_NPC:
                    if (cliente.getQuest().m_realizoQuest) {
                        if (quest.DaExp) {
                            cliente.getEstads().addExp(quest.Exp);
                            cliente.checkUserLevel();
                            cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Exp + " puntos de experiencia!", npc.getId());
                            cliente.getQuest().m_realizoQuest = false;
                            cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                            cliente.getQuest().m_enQuest = false;
                        }
                        if (quest.DaOro) {
                            cliente.getEstads().agregarOro(quest.Oro);
                            cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Oro + " monedas de oro!", npc.getId());
                            cliente.getQuest().m_realizoQuest = false;
                            cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                            cliente.getQuest().m_enQuest = false;
                        }
                        if (quest.DaObj) {
                            if (cliente.getInv().agregarItem(quest.Obj, 1) < 1) {
                                Map mapa = this.server.getMapa(cliente.getPos().mapa);
                                mapa.tirarItemAlPiso(cliente.getPos().x, cliente.getPos().y, new InventoryObject(quest.Obj, 1));
                            }
                            cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido un objeto!", npc.getId());
                            cliente.getQuest().m_realizoQuest = false;
                            cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                            cliente.getQuest().m_enQuest = false;
                        }
                    } else {
                        cliente.hablar(COLOR_BLANCO, "Todav�a no has logrado el objetivo!", npc.getId());
                    }
                    break;
                case OBJETIVO_MATAR_UN_NPC:
                    if (cliente.getQuest().m_realizoQuest) {
                        if (quest.DaExp) {
                            cliente.getEstads().addExp(quest.Exp);
                            cliente.checkUserLevel();
                            cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Exp + " puntos de experiencia!", npc.getId());
                            cliente.getQuest().m_realizoQuest = false;
                            cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                            cliente.getQuest().m_enQuest = false;
                        }
                        if (quest.DaOro) {
                            cliente.getEstads().agregarOro(quest.Oro);
                            cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido " + quest.Oro + " monedas de oro!", npc.getId());
                            cliente.getQuest().m_realizoQuest = false;
                            cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                            cliente.getQuest().m_enQuest = false;
                        }
                        if (quest.DaObj) {
                            if (cliente.getInv().agregarItem(quest.Obj, 1) < 1) {
                                Map mapa = this.server.getMapa(cliente.getPos().mapa);
                                mapa.tirarItemAlPiso(cliente.getPos().x, cliente.getPos().y, new InventoryObject(quest.Obj, 1));
                            }
                            cliente.hablar(COLOR_BLANCO, "Como recompensa has recibido un objeto!", npc.getId());
                            cliente.getQuest().m_realizoQuest = false;
                            cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
                            cliente.getQuest().m_enQuest = false;
                        }
                    } else {
                        cliente.hablar(COLOR_BLANCO, "Todav�a no has logrado el objetivo!", npc.getId());
                    }
                    break;
            }
            cliente.enviarEstadsUsuario();
        } catch (Exception e) {
            Log.serverLogger().log(Level.SEVERE, cliente.getNick() + " Error en RecibirRecompensaQuest!", e);
        }
    }
 
    public void checkNpcAmigo(Client cliente) {
        if (cliente.getQuest().m_enQuest) {
            Npc npc = this.server.getNPC(cliente.getFlags().TargetNpc);
            if (npc.getNPCtype() == Npc.NPCTYPE_AMIGOQUEST) {
                Quest quest = cliente.getQuest().getQuest();
                if (quest.Objetivo == 3) {
                    cliente.getQuest().m_realizoQuest = true;
                    cliente.hablar(COLOR_BLANCO, "Felicitaciones, me has encontrado, ahora debes volver con mi compa�ero por tu recompenza!", npc.getId());
                } else {
                    cliente.hablar(COLOR_BLANCO, "Yo correspondo a otra quest!", npc.getId());
                }
            } else {
                cliente.enviarMensaje("Este npc no es de la quest jajaja!", FontType.INFO);
            }
        } else {
            cliente.enviarMensaje("No has comenzado ninguna quest!", FontType.INFO);
        }
    }

 
    public void sendInfoQuest(Client cliente) {
        Npc npc = this.server.getNPC(cliente.getFlags().TargetNpc);
        if (cliente.esNewbie()) {
            cliente.hablar(COLOR_BLANCO, "Los newbies no pueden realizar estas quests!", npc.getId());
            return;
        }
        Quest quest = cliente.getQuest().getQuest();
        switch (quest.Objetivo) {
            case 1:
                cliente.hablar(COLOR_BLANCO, "Debes matar " + quest.NPCs + " npcs para recibir tu recompensa!", npc.getId());
                break;
            case 2:
                if (!cliente.getFaccion().ArmadaReal && !cliente.getFaccion().FuerzasCaos) {
                    cliente.hablar(COLOR_BLANCO, "Debes matar " + quest.Usuarios + " usuarios para recibir tu recompensa!", npc.getId());
                } else if (cliente.getFaccion().ArmadaReal) {
                    cliente.hablar(COLOR_BLANCO, "Debes matar " + quest.Criminales + " criminales para recibir tu recompensa!", npc.getId());
                } else if (cliente.getFaccion().FuerzasCaos) {
                    cliente.hablar(COLOR_BLANCO, "Debes matar " + quest.Ciudadanos + " ciudadanos para recibir tu recompensa!", npc.getId());
                }
                break;
            case 3:
                cliente.hablar(COLOR_BLANCO, "Debes encontrar a mi amigo npc para recibir tu recompensa! Pistas: Se pude encontrar en lugares caracter�sticos del juego, pero apres�rate porque si otro que est� haciendo este tipo de quest lo encuentra primero puedes perderlo, en tal caso deber�s clikearme y poner /MERINDO", npc.getId());
                break;
            case 4:
                cliente.hablar(COLOR_BLANCO, "Debes matar al npc que se encuentra en las coordenadas " + this.Pista + " para recibir tu recompensa! PD: Si no te apresuras y lo mata otro usuario perder�s esta quest, en tal caso deber�s clikearme y poner /MERINDO", npc.getId());
                break;
        }
    }

 
    public void userSeRinde(Client cliente) {
        Npc npc = this.server.getNPC(cliente.getFlags().TargetNpc);
        try {
            if (cliente.esNewbie()) {
                cliente.hablar(COLOR_BLANCO, "Los newbies no pueden realizar estas quests!", npc.getId());
                return;
            }
            cliente.getQuest().m_realizoQuest = false;
            cliente.getQuest().m_recompensa = cliente.getQuest().m_nroQuest;
            cliente.getQuest().m_enQuest = false;
            cliente.hablar(COLOR_BLANCO, "Te has rendido por lo tanto no has conseguido la recompensa, pero puedes continuar con la siguiente quest.", npc.getId());
        } catch (Exception e) {
            Log.serverLogger().log(Level.SEVERE, cliente.getNick() + " Error en UserSeRinde!", e);
        }
    }
    
}
