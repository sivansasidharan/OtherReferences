package com.sample.edgar.arelle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestCallEdgar {

	public static void main(String[] args) {

		String restAPIstr = "http://localhost:8090/rest/xbrl/"
				+ "C:/Edgar_Test_Data/Xbrl/0001026608-14-000011-xbrl/acu-20131231.xml"
				+ "/facts?media=json";
		try {
			URL url = new URL(restAPIstr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			if (conn.getResponseCode() != 200) {
				throw new IOException(conn.getResponseMessage());
			} else {
				System.out.println("Connection Established "
						+ conn.getResponseCode());
			}

			// Buffer the result into a string
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
				System.out.println(line);
			}
			rd.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}