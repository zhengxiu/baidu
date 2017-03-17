package com.data.baidu;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

public class Sample {
	 private static final String serverURL = "http://vop.baidu.com/server_api";
	    private static String token = "";

	    private static  String testFile="ttee";
	    //put your own params here
	    private static final String apiKey = "AH0sGz9WA4OmoP3kl3goFz2e";
	    private static final String secretKey = "0ee09d50592d9a4b7b22a453feee2eaa";
	    private static final String cuid = "9287073";

	    public static void main(String[] args) throws Exception {
			File fs=new File(testFile);
			File[] ff=fs.listFiles();
			for(File f:ff) {
				System.out.println(f.getName().substring(0,f.getName().length()-3)+"txt");
				getToken();
				method1(f);
				String response=method2(f);
				File fl=new File("results\\"+f.getName().substring(0,f.getName().length()-3)+"txt");
				BufferedWriter bw=new BufferedWriter((new FileWriter(fl)));
				//System.out.println(new JSONObject(response.toString()).getString("result"));
				System.out.println(new JSONObject(response.toString()).get("result"));

				bw.write(new JSONObject(response).get("result").toString() + "\r\n");
				bw.close();
			}
			File fle=new File("results");
			File[] files=fle.listFiles();
			for(File fd:files){
				String pfile=fd.toString().replace("results","segresult");
				File ds=new File(pfile);
				Seg.segm(fd, ds);
			}
			File fles=new File("segresult");
			File[] filess=fles.listFiles();
			BufferedWriter cc=new BufferedWriter(new FileWriter(new File("allData\\allResult.txt")));
			String gg=null;
			for(File xd:filess){
				BufferedReader bb=new BufferedReader(new FileReader(xd));
				while((gg=bb.readLine())!=null){
					cc.write(gg);
				}

			}
            cc.close();
	    }
	    //token获取
	private static void getToken() throws Exception {
		String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" +
				"&client_id=" + apiKey + "&client_secret=" + secretKey;
		HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();
		token = new JSONObject(printResponse(conn)).getString("access_token");

		//System.out.println(token);
	}
	    //上传文件数据
	    private static void method1(File f) throws Exception {

	    		 HttpURLConnection conn = (HttpURLConnection) new URL(serverURL).openConnection();

	 	        // construct params
	 	        JSONObject params = new JSONObject();
	 	        params.put("format", "pcm");
				params.put("rate", 8000);
				params.put("channel", "1");
				params.put("token", token);
				params.put("cuid", cuid);
				params.put("len", f.length());
				params.put("speech", DatatypeConverter.printBase64Binary(loadFile(f)));
	 	        // add request header
	 	        conn.setRequestMethod("POST");
	 	        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
	 	        conn.setDoInput(true);
	 	        conn.setDoOutput(true);

	    }
	    //返回结果
	    private static String  method2(File f) throws Exception {

	    		 HttpURLConnection conn = (HttpURLConnection) new URL(serverURL
	 	                + "?cuid=" + cuid + "&token=" + token).openConnection();

	 	        // add request header
	 	        conn.setRequestMethod("POST");
	 	        conn.setRequestProperty("Content-Type", "audio/pcm; rate=8000");

	 	        conn.setDoInput(true);
	 	        conn.setDoOutput(true);

	 	        // send request
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

			wr.write(loadFile(f));
			wr.flush();
			wr.close();
			return printResponse(conn);




	    }

	    private static String printResponse(HttpURLConnection conn) throws Exception {
	        if (conn.getResponseCode() != 200) {
	            // request error
	            return "";
	        }
	        InputStream is = conn.getInputStream();
	        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	        String line;
	        StringBuffer response = new StringBuffer();
	        while ((line = rd.readLine()) != null) {
	            response.append(line);
	            response.append('\r');
	        }

	       rd.close();

			//System.out.println(new JSONObject(response.toString()).toString(4));
	        //System.out.println(new JSONObject(response.toString()).get("result"));
	        return response.toString();
			//return new JSONObject(response.toString()).getString("result");
	    }

	    private static byte[] loadFile(File file) throws IOException {
	        InputStream is = new FileInputStream(file);

	        long length = file.length();
	        byte[] bytes = new byte[(int) length];

	        int offset = 0;
	        int numRead = 0;
	        while (offset < bytes.length
	                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
	            offset += numRead;
	        }

	        if (offset < bytes.length) {
	            is.close();
	            throw new IOException("Could not completely read file " + file.getName());
	        }

	        is.close();
	        return bytes;
	    }

}
