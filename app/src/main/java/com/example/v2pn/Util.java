package com.example.v2pn;

public class Util {
    // public static void myLog(android.content.Context context, String message) {
    // try {
    // java.io.File testFile = new java.io.File(context.getExternalFilesDir(null) +
    // "/myLog.txt");
    // // /sdcard/Android/data/com.example.v2pn/files/myLog.txt
    // java.io.BufferedWriter writer = new java.io.BufferedWriter(new
    // java.io.FileWriter(testFile, true));
    // writer.write(new java.sql.Timestamp(new java.util.Date().getTime()) + ": " +
    // message + "\n");
    // writer.close();
    // } catch (Exception ignored) {
    // }
    // }

    public static void saveToMediaStore(android.content.Context context, String fileName, String message) {
        // If you reinstall the app, MediaStore will not recognize the previous-created
        // file any more
        android.net.Uri contentUri = android.provider.MediaStore.Files.getContentUri("external");
        String selection = android.provider.MediaStore.MediaColumns.RELATIVE_PATH + "=?";
        String[] selectionArgs = new String[] { android.os.Environment.DIRECTORY_DOWNLOADS + "/com.example.v2pn/" };
        android.database.Cursor cursor = context.getContentResolver().query(contentUri, null,
                selection,
                selectionArgs, null);
        android.net.Uri uri = null;
        if (cursor.getCount() == 0) {
            android.util.Log.d("v2pn", "cursor.getCount() == 0");
            try {
                java.io.OutputStream outputStream = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    android.content.ContentValues values = new android.content.ContentValues();
                    values.put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                    values.put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "text/plain");
                    values.put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH,
                            android.os.Environment.DIRECTORY_DOWNLOADS + "/com.example.v2pn/");
                    uri = context.getContentResolver().insert(
                            android.provider.MediaStore.Files.getContentUri("external"),
                            values);
                    outputStream = context.getContentResolver().openOutputStream(uri);
                } else {
                    android.util.Log.d("v2pn", "ERROR: NO MediaStore");
                }
                try {
                    outputStream.write(message.getBytes());
                } finally {
                    outputStream.close();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            android.util.Log.d("v2pn", "uri: " + uri);
        } else {
            android.util.Log.d("v2pn", "cursor.getCount() != 0");
            while (cursor.moveToNext()) {
                String file = cursor.getString(
                        cursor.getColumnIndex(android.provider.MediaStore.MediaColumns.DISPLAY_NAME));
                android.util.Log.d("v2pn", "file: " + file);
                if (file.equals(fileName + ".txt")) {
                    android.util.Log.d("v2pn", file + " exists");
                    long id = cursor
                            .getLong(cursor.getColumnIndex(android.provider.MediaStore.MediaColumns._ID));
                    uri = android.content.ContentUris.withAppendedId(contentUri, id);
                    break;
                }
            }
            android.util.Log.d("v2pn", "uri: " + uri);
            try {
                java.io.OutputStream outputStream = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    outputStream = context.getContentResolver().openOutputStream(uri, "wa");
                } else {
                    android.util.Log.d("v2pn", "ERROR: NO MediaStore");
                }
                try {
                    outputStream.write(message.getBytes());
                } finally {
                    outputStream.close();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

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
