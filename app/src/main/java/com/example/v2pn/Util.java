package com.example.v2pn;

public class Util {
    // public static void myLog(android.content.Context context, String message) {
    //     try {
    //         java.io.File testFile = new java.io.File(context.getExternalFilesDir(null) + "/myLog.txt");
    //         // /sdcard/Android/data/com.example.v2pn/files/myLog.txt
    //         java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(testFile, true));
    //         writer.write(new java.sql.Timestamp(new java.util.Date().getTime()) + ": " + message + "\n");
    //         writer.close();
    //     } catch (Exception ignored) {
    //     }
    // }

    public static void variableWrite(android.content.Context context, String fileName, String message) {
        try {
            java.io.File testFile = new java.io.File(context.getApplicationInfo().dataDir + "/" + fileName);
            // /data/user/0/com.example.v2pn/variable.txt
            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(testFile, false));
            writer.write(message + "\n");
            writer.close();
        } catch (Exception ignored) {
        }
    }

    public static String variableRead(android.content.Context context, String fileName) {
        String str = null;
        try {
            java.io.File file = new java.io.File(context.getApplicationInfo().dataDir + "/" + fileName);
            // /data/user/0/com.example.v2pn/variable.txt
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            str = reader.lines().collect(java.util.stream.Collectors.joining(System.lineSeparator()));
            reader.close();
            // Util.myLog(context, fileName + ": " + str);
        } catch (Exception ignored) {
        }
        return str;
    }

    public static String listFiles(String path) {
        String s = "";
        String[] items;
        java.io.File f = new java.io.File(path);
        items = f.list();
        // return items[0];
        for (String item : items) {
            s = s + item + " ";
        }
        return s;
    }

    public static void killAll(android.content.Context context) {
        try {
            // Runtime.getRuntime().exec("pkill -f com.example.v2pn:v2pnProcess");
            String v2pnPid = Util.variableRead(context, "v2pnPid.txt");
            Runtime.getRuntime().exec("kill -9 " + v2pnPid);
            Util.variableWrite(context, "running.txt", "false");
            android.util.Log.d("v2pn", "show Stop Service toast");
            android.widget.Toast.makeText(context, "Stop Service", android.widget.Toast.LENGTH_SHORT).show();
        } catch (Exception ignore) {
        }
    }

    // public static void testConnection() {
    // try {
    // java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new
    // java.net.URL("https", "www.google.com", "/generate_204").openConnection(new
    // java.net.Proxy(java.net.Proxy.Type.HTTP, new
    // java.net.InetSocketAddress("127.0.0.1", 1081)));
    // conn.setConnectTimeout(30000);
    // conn.setReadTimeout(30000);
    // conn.setRequestProperty("Connection", "close");
    // conn.setInstanceFollowRedirects(false);
    // conn.setUseCaches(false);

    // long start = android.os.SystemClock.elapsedRealtime();
    // int code = conn.getResponseCode();
    // long elapsed = android.os.SystemClock.elapsedRealtime() - start;
    // } catch (Exception ignored) {
    // }
    // }

}
