package crawler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Utiles {


	public final static long ZERO = 0;



	public static String formatoYYYYMMddHHmm(String data, boolean finalDoDia){
		String diaMesAno [] = data.split("/");
		String dia = diaMesAno[0];
		String mes = diaMesAno[1];
		String ano = diaMesAno[2];

		if(finalDoDia){
			data = ano+mes+dia+"2359";
		}else{
			data = ano+mes+dia+"0000";
		}

		return data;

	}

	public static long dataToTimestamp(String data, boolean finalDoDia) throws DataToTimesTamp{

		long unixtime = ZERO;

		if(!data.isEmpty()){
			data = formatoYYYYMMddHHmm(data,finalDoDia);
			DateFormat dfm = new SimpleDateFormat("yyyyMMddHHmm");  

			dfm.setTimeZone(TimeZone.getTimeZone("GMT"));//Specify your timezone 

			try {
				unixtime = dfm.parse(data).getTime();
			} catch (ParseException e) {
				throw new DataToTimesTamp("Impossivel converter a data: "+data+"para o formato timestamp");
			}  
			unixtime=unixtime/1000;
		}


		return unixtime;

	}

	public static String encondeConsulta(String consulta){

		try {
			return consulta = URLEncoder.encode(consulta,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			System.out.println("Tipo de condificacao nao suportada pela consulta.");
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static String timesTampData(long unixTimestamp){
		//1386106407
		java.sql.Timestamp stamp = new java.sql.Timestamp((long)unixTimestamp*1000); 
		Date date = new Date(stamp.getTime()); 
		return date.toGMTString();
	}

}
