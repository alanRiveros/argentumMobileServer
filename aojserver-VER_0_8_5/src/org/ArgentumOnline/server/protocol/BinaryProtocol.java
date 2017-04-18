/**
 * BinaryProtocol.java
 * 
 * Created 01/feb/2007
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

import java.nio.ByteBuffer;


/**
 * @author gorlok
 *
 */
public class BinaryProtocol extends Protocol {
	
	@Override
	public boolean decodeData(byte[] data, int length) {

		System.out.println("decodeData(): length=" + length);
		for (int i = 0; i < length && i < 10; i++) {
			System.out.println("decodeData(): data[" + i + "]=" + data[i]);
		}
		int msg_offset = 0;
		while (msg_offset < length) {
			System.out.println("DECODIFICANDO");
			if (length < msg_offset + 3) {
				System.out.println("decodeData(): Mensaje incompleto");
				return false;
			}
			short msg_len = (short) ((data[0] << 8) + data[1]);
			if (length < msg_offset + msg_len + 2) {
				System.out.println("decodeData(): Mensaje con datos incompleto");
				return false;
			}
			byte msg_id = data[2];
			byte[] msg_data = new byte[msg_len - 1];
			for (int i = 0; i < msg_len - 1; i++) {
				msg_data[i] = data[msg_offset + 3 + i];
			}
			processCmd(msg_id, msg_data);
			msg_offset += 2;
			msg_offset += msg_len;
		}
		if (msg_offset == length) {
			System.out.println("decode ok");
			return true;
		}
		System.out.println("decode failed");
		return false;
	}
	
	private void processCmd(byte msg_id, byte[] data) {
		switch (msg_id) {
		case 1: // GIVEMEVALCODE
            this.cliente.enviarValCode();
			break;
		case 40: // OLOGIN
			System.out.println("OLOGIN !!!");
			String str = new String(data);
			String[] args = str.split(",");
			// Conectar
			System.out.println("cantidad de argumentos: " + args.length);
			String nick = args[0];
			String passwd = args[1];
			System.out.println("OLOGIN - nick = " + nick);
			System.out.println("OLOGIN - pass = " + passwd);
	        this.cliente.connectUser(nick, passwd);
	        break;		
		default:
			System.out.println("processCmd(): Mensaje desconocido - code: " + msg_id);
		}
	}

	@Override
	public void encodeData(ByteBuffer buf, ClientMessage msg, Object... params) {
		int start_pos = buf.position();
		buf.putShort((short) 0); // Aquí guardaré el tamaño del paquete, que todavía no conozco.
		buf.put(msg.binCode()); // Guardo el ID del paquete, 1 byte.
		if (msg.binCode() == 0) {
			System.out.println("encodeData(): mensaje SIN CODIGO CONOCIDO!!: " + msg.strMessage());
		}
		System.out.println("encodeData(): tipo paq=" + msg.binCode());
		// Guardar los valores de los parámetros, codificando según su tipo/clase.
		if (params != null) {
			for (Object element : params) {
				if (element.getClass().getName().equals("java.lang.Short")) {
					System.out.println("encodeData(): <short>");
					buf.putShort((Short) element);
				} else if (element.getClass().getName().equals("java.lang.Integer")) {
					System.out.println("encodeData(): <int>");
					buf.putInt((Integer) element);
				} else if (element.getClass().getName().equals("java.lang.Long")) {
					System.out.println("encodeData(): <long>");
					buf.putLong((Long) element);
				} else if (element.getClass().getName().equals("java.lang.Char")) {
					System.out.println("encodeData(): <char>");
					buf.putChar((Character) element);
				} else if (element.getClass().getName().equals("java.lang.String")) {
					System.out.println("encodeData(): <string>");
					buf.put( ((String) element).getBytes() );
				} else {
					System.out.println("encodeData(): tipo de dato desconocido - " + element.getClass().getName());
				}
			}
		}
		short len = (short) (buf.position() - start_pos - 2);
		System.out.println("encodeData(): length=" + len);
		buf.putShort(start_pos, len); // Corrijo el tamaño del paquete, que ahora si conozco.
	}

}
