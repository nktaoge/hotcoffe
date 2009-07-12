package br.com.goals.template;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

public class Conversor {
	public static String HTMLEntityEncode(String s) {
		StringBuffer buf = new StringBuffer();
		int len = (s == null ? -1 : s.length());

		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
				buf.append(c);
			} else {
				buf.append("&#" + (int) c + ";");
			}
		}
		return buf.toString();
	}

	/**
	 * Converte dd/mm/yyyy para sql date
	 * 
	 * @param val
	 * @return Date
	 * @throws Exception
	 */
	public static Date converteDDMMYYYY(String val) throws IllegalArgumentException {
		String a[] = val.split("/");
		if (a.length >= 3) {
			return Date.valueOf(a[2] + "-" + a[1] + "-" + a[0]);
		}
		throw new IllegalArgumentException("Data inválida");
		// return Date.valueOf("1900-01-01");
	}

	/**
	 * Converte Date para string dd/mm/yyyy
	 * 
	 * @param date
	 *            data java.sql.Date
	 * @return String
	 */
	public static String converteDDMMYYYY(Date date) {
		SimpleDateFormat formatador1 = new SimpleDateFormat("dd/MM/yyyy");
		return formatador1.format(date);
	}
	/**
	 * Converte Date para string dd/mm/yyyy
	 * 
	 * @param date
	 *            data java.sql.Date
	 * @return String
	 */
	public static String converteDDMMYYYY(java.util.Date date) {
		SimpleDateFormat formatador1 = new SimpleDateFormat("dd/MM/yyyy");
		return formatador1.format(date);
	}
	/**
	 * Converte de YYYY-MM-DD para string dd/mm/yyyy
	 * 
	 * @param date
	 *            data java.sql.Date
	 * @return String N/A caso algum erro ocorra
	 */
	public static String deYYYYMMDDparaDDMMYYYY(String val) {
		try {
			String b[] = val.split(" ");
			String a[] = b[0].split("-");
			if (a.length >= 3) {
				return a[2] + "/" + a[1] + "/" + a[0];
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return "N/A";
	}

	/**
	 * Formata a moeda, esta função arredonda o valor dos centavos
	 * @param preco
	 * @return Formato 1.000,00
	 */
	public static String converteMoeda(Double preco) {
		DecimalFormat nf = new DecimalFormat("###,##0.00"); 
		DecimalFormatSymbols newSymbols = new DecimalFormatSymbols();
		newSymbols.setDecimalSeparator(',');
		newSymbols.setGroupingSeparator('.');
		nf.setDecimalFormatSymbols(newSymbols );
		return nf.format(preco);  
	}
}
