package com.haothink.utils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;

/**
 * Http read the data operation tools
 */
public class HttpUtil {


    public static String postSoapService(String urlString, String soapActionString, byte[] soapData, int timeOut) {
        InputStream inputStream = null;
        ByteArrayOutputStream outStream = null;
        OutputStream out = null;
        HttpURLConnection httpConn = null;
        try {

            Map<String, String> map = new HashMap<String, String>();
            map.put("Content-Length", String.valueOf(soapData.length));
            map.put("Content-Type", "text/xml; charset=utf-8");
            map.put("soapActionString", soapActionString);
            httpConn = getConnection(urlString, "POST", timeOut, map);
            //读和写流都创建,写流必须在读流之前,并且写流先给服务器发消息,不然创建了读流后发送数据,服务器会收不到.
            out = httpConn.getOutputStream();
            out.write(soapData);
            out.flush();
            inputStream = httpConn.getInputStream();
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            if(inputStream != null){
                while ((length = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, length);
                }
            }

            byte[] fileData = outStream.toByteArray();
            return new String(fileData);
        } catch (Exception e) {
            if (httpConn != null) {
//                try {
////                    logger.error("请求失败！发生异常！url:" + urlString + "！请求状态码：" +
////                            httpConn.getResponseCode(), e);
//                } catch (IOException e1) {
//
//                }
            } else {

            }
        } finally {
            try {
                if(out != null){
                    out.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if(outStream != null) {
                    outStream.close();
                }

                if (httpConn != null) {
                    httpConn.disconnect();
                }
            } catch (IOException e) {

            }
        }

        return null;
    }

    /**
     * get submit
     * for a link address data to byte array way back
     * and give the response to record HTTP status
     */
    public static String get(String urlString, int timeOut, Map<String, String> headers) {
        BufferedReader reader = null;
        HttpURLConnection httpConn = null;
        try {
            httpConn = getConnection(urlString, "GET", timeOut, headers);
            httpConn.connect();
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        }  catch (SocketTimeoutException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            try {
                if (reader != null) {
                    reader.close();
                }

                if (httpConn !=  null) {
                    httpConn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * post submit
     * for a link address data to byte array way back
     * and give the response to record HTTP status
     *
     *  params query parameter
     */
    public static String post(String urlString, Map<String, String> params, int timeOut, Map<String, String> headers) {
        if(!checkData(urlString)) {
            return null;
        }

        BufferedReader reader = null;
        OutputStreamWriter out = null;
        HttpURLConnection httpConn = null;
        try {
            String content = buildRequestParams(params);
            httpConn = getConnection(urlString, "POST", timeOut, headers);
            httpConn.connect();
            //读和写流都创建,写流必须在读流之前,并且写流先给服务器发消息,不然创建了读流后发送数据,服务器会收不到.
            out = new OutputStreamWriter (httpConn.getOutputStream());
            out.write(content);
            out.flush();
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (SocketTimeoutException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close(out, reader, httpConn);
        }

        return null;
    }

    /**
     * post submit
     * for a link address data to byte array way back
     * and give the response to record HTTP status
     *
     *  params query parameter
     */
    public static String post(String urlString, String requestBody, int timeOut, Map<String, String> headers) {
        if(!checkData(urlString)) {
            return null;
        }

        BufferedReader reader = null;
        OutputStreamWriter out = null;
        HttpURLConnection httpConn = null;
        try {
            httpConn = getConnection(urlString, "POST", timeOut, headers);
            httpConn.connect();
            //读和写流都创建,写流必须在读流之前,并且写流先给服务器发消息,不然创建了读流后发送数据,服务器会收不到.
            out = new OutputStreamWriter (httpConn.getOutputStream());
            out.write(requestBody);
            out.flush();
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (SocketTimeoutException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close(out, reader, httpConn);
        }

        return null;
    }

    //Stitching parameters
    public static String buildRequestParams(Map<String, String> params) throws UnsupportedEncodingException
    {
        if (params == null || params.isEmpty()) {
            return null;
        }

        List<Map.Entry<String, String>> newParams = new ArrayList<Map.Entry<String, String>>(params.entrySet());
        StringBuilder query = new StringBuilder();
        boolean hasParam = false;
        for (Map.Entry<String, String> entry : newParams) {
            String name = entry.getKey();
            String value = entry.getValue();

            // 忽略参数名或参数值为空的参数
            if (areNotEmpty(name, value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }

                query.append(name).append("=").append(value);
            }

        }

        return query.toString();
    }

    private static HttpURLConnection getConnection(String urlString, String method, int timeOut) throws IOException
    {
        return getConnection(urlString, method, timeOut, null);
    }

    private static HttpURLConnection getConnection(String urlString, String method, int timeOut,
                                                   Map<String, String> headers) throws IOException
    {
        URL url = new URL(urlString);
        //set socket proyx
//        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 7070));
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                conn.setRequestProperty(key, headers.get(key));
            }
        }


        conn.setConnectTimeout(timeOut);    //设置连接超时为 timeOut s
        conn.setReadTimeout(timeOut);       //读取数据超时为 timeOut s
        return conn;
    }

    private static boolean areNotEmpty(String name, String value) {
        return !(name == null || value == null || name.equals("") || value.equals(""));
    }

    private static void close(OutputStreamWriter out, BufferedReader reader, HttpURLConnection httpConn) {
        try {
            if(out != null){
                out.close();
            }

            if(reader != null) {
                reader.close();
            }

            if (httpConn != null) {
                httpConn.disconnect();
            }
        } catch (IOException e) {
//            logger.error("close data stream error", e);
        }
    }


    //检查数据合法性
    public static boolean checkData(String urlString) {
        return urlString != null && urlString.startsWith("http://")||
                urlString != null && urlString.startsWith("https://");
    }
}
