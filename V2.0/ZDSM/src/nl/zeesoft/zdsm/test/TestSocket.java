package nl.zeesoft.zdsm.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestSocket {

	public static void main(String[] args) {
		Socket s = null;
		try {
			s = new Socket("portal-c03.nl.eu.abnamro.com",9997);
			PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
			
			//writer.write("GET http://portal-c03.nl.eu.abnamro.com:9997/session HTTP/1.1");
			//writer.write("GET http://portal-c03.nl.eu.abnamro.com:9997/session/loginchallenge?accessToolUsage=SOFTTOKEN&accountNumber=0605228647&appId=SIMPLE_BANKING&cardNumber=282 HTTP/1.1"); 
			//writer.write("GET http://portal-c03.nl.eu.abnamro.com:9997/salesrequests/savings/undefined/products?productId=1331 HTTP/1.1");
			writer.write("PUT http://portal-c03.nl.eu.abnamro.com:9997/session/loginResponse HTTP/1.1");
			writer.write("\n");
			writer.write("Host: portal-c03.nl.eu.abnamro.com:9997");
			writer.write("\n");
			
			
			//writer.write("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
			//writer.write("\n");
			//writer.write("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			//writer.write("\n");
			//writer.write("Accept-Language: nl,en-US;q=0.7,en;q=0.3");
			//writer.write("\n");
			//writer.write("Connection: keep-alive");
			//writer.write("\n");
			//writer.write("Upgrade-Insecure-Requests: 1");
			//writer.write("\n");
			//writer.write("Pragma: no-cache");
			//writer.write("\n");
			//writer.write("Cache-Control: no-cache");
			//writer.write("\n");
			writer.write("\n");
			writer.flush();
			
			System.out.println("Flushed, reading ...");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}