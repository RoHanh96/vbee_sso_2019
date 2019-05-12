package com.vbee.springbootmongodbnewspapersrestapi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Ultils {
	public static boolean ping(String address, int port, int timeout) {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(address, port), timeout);
			socket.close();
			socket = null;
			System.gc();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
