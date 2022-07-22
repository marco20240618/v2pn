package com.example.v2pn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoFileUpload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

// https://www.baeldung.com/nanohttpd
public class MyHttpd extends NanoHTTPD {
    android.content.Context context;

    public MyHttpd(android.content.Context context) throws IOException {
        super(8080);
        this.context = context;
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (session.getMethod() == Method.GET) {
            // curl.exe http://localhost:8080/v2pn/allowedlist
            if (session.getUri().equals("/v2pn/allowedlist")) {
                android.util.Log.d("v2pn", "GET /v2pn/allowedlist");
                try {
                    java.io.File file = new java.io.File(
                            context.getApplicationInfo().dataDir + "/" + "allowedlist.txt");
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                    java.util.HashSet<String> myHashSet2 = new java.util.HashSet<String>();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        myHashSet2.add(line);
                    }
                    reader.close();
                    java.util.Iterator<String> iterator = myHashSet2.iterator();
                    String response = "";
                    while (iterator.hasNext()) {
                        response += iterator.next() + "\n";
                    }
                    return newFixedLengthResponse(response);
                } catch (Exception e) {
                }
            }

            if (session.getUri().equals("/v2pn/globalvpn")) {
                android.util.Log.d("v2pn", "GET /v2pn/globalvpn");
                try {
                    java.io.File file = new java.io.File(
                            context.getApplicationInfo().dataDir + "/" + "globalvpn.txt");
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                    java.util.HashSet<String> myHashSet2 = new java.util.HashSet<String>();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        myHashSet2.add(line);
                    }
                    reader.close();
                    java.util.Iterator<String> iterator = myHashSet2.iterator();
                    String response = "";
                    while (iterator.hasNext()) {
                        response += iterator.next() + "\n";
                    }
                    return newFixedLengthResponse(response);
                } catch (Exception e) {
                }
            }

            if (session.getUri().equals("/v2pn/running")) {
                android.util.Log.d("v2pn", "GET /v2pn/running");
                try {
                    java.io.File file = new java.io.File(
                            context.getApplicationInfo().dataDir + "/" + "running.txt");
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                    java.util.HashSet<String> myHashSet2 = new java.util.HashSet<String>();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        myHashSet2.add(line);
                    }
                    reader.close();
                    java.util.Iterator<String> iterator = myHashSet2.iterator();
                    String response = "";
                    while (iterator.hasNext()) {
                        response += iterator.next() + "\n";
                    }
                    return newFixedLengthResponse(response);
                } catch (Exception e) {
                }
            }

            if (session.getUri().equals("/v2pn/filelist")) {
                android.util.Log.d("v2pn", "GET /v2pn/filelist");
                try {
                    String fileList = "";
                    String[] items;
                    java.io.File f = new java.io.File(context.getApplicationInfo().dataDir + "/");
                    items = f.list();
                    for (String item : items) {
                        fileList = fileList + item + "\n";
                    }
                    return newFixedLengthResponse(fileList);
                } catch (Exception e) {
                }
            }

        }

        if (session.getMethod() == Method.POST) {
            if (session.getUri().equals("/v2pn/fileupload")) {
                android.util.Log.d("v2pn", "POST /v2pn/fileupload");
                // curl.exe -F "filename=@_viminfo" http://localhost:8080/v2pn/fileupload
                try {
                    List<FileItem> files = new NanoFileUpload(new DiskFileItemFactory()).parseRequest(session);
                    int uploadedCount = 0;
                    for (FileItem file : files) {
                        try {
                            String fileName = file.getName();
                            byte[] fileContent = file.get();
                            // /data/user/0/com.example.v2pn
                            Files.write(Paths.get(context.getApplicationInfo().dataDir + "/" + fileName), fileContent);
                            uploadedCount++;
                        } catch (Exception exception) {
                            android.util.Log.d("v2pn", "file upload exception: " + exception);
                        }
                    }
                    return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT,
                            "Uploaded files " + uploadedCount + " out of " + files.size());
                } catch (FileUploadException e) {
                    throw new IllegalArgumentException("Could not handle files from API request",
                            e);
                }
            }

            if (session.getUri().equals("/v2pn/allowedlist")) {
                android.util.Log.d("v2pn", "POST /v2pn/allowedlist");
                // curl.exe -X POST -d "com.gggggaaaaaaaaa"
                // http://localhost:8080/v2pn/allowedlist
                try {
                    session.parseBody(new java.util.HashMap<>());
                    String requestBody = session.getQueryParameterString();
                    java.io.BufferedWriter writer = new java.io.BufferedWriter(
                            new java.io.FileWriter(context.getApplicationInfo().dataDir + "/" + "allowedlist.txt",
                                    true));
                    writer.write(requestBody);
                    writer.newLine();
                    writer.close();
                    return newFixedLengthResponse("POST /v2pn/allowedlist " + requestBody + " succeed");
                } catch (IOException | ResponseException e) {
                    // handle
                }
            }

            if (session.getUri().equals("/v2pn/globalvpn")) {
                android.util.Log.d("v2pn", "POST /v2pn/globalvpn");
                // curl.exe -X POST -d "true"
                // http://localhost:8080/v2pn/globalvpn
                try {
                    session.parseBody(new java.util.HashMap<>());
                    String requestBody = session.getQueryParameterString();
                    java.io.BufferedWriter writer = new java.io.BufferedWriter(
                            new java.io.FileWriter(context.getApplicationInfo().dataDir + "/" + "globalvpn.txt",
                                    false));
                    writer.write(requestBody);
                    writer.newLine();
                    writer.close();
                    return newFixedLengthResponse("POST /v2pn/globalvpn " + requestBody + " succeed");
                } catch (IOException | ResponseException e) {
                    // handle
                }
            }
        }

        // Cross-Origin Resource Sharing
        // Response response = newFixedLengthResponse("Hello world");
        // response.addHeader("Access-Control-Allow-Origin", "*");
        // return response;

        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                "The requested resource does not exist");
    }
}