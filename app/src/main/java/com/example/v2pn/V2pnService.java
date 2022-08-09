package com.example.v2pn;

public class V2pnService extends android.net.VpnService {
    @Override
    public void onDestroy() {
        android.util.Log.d("v2pn", "onDestroy()");
        Util.killAll(this);
        super.onDestroy();
    }

    @Override
    public void onRevoke() {
        android.util.Log.d("v2pn", "onRevoke()");
        Util.killAll(this);
        super.onRevoke();
    }

    @Override
    public int onStartCommand(android.content.Intent intent, int flags, int startId) {
        try {
            android.util.Log.d("v2pn", "onStartCommand()");
            android.util.Log.d("v2pn", "V2pnService process id: " + String.valueOf(android.os.Process.myPid()));
            Util.variableWrite(getApplicationContext(), "v2pnPid.txt", String.valueOf(android.os.Process.myPid()));
            // notification keeps QSTileService run in background.
            ((android.app.NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(new android.app.NotificationChannel("my channel id",
                            "v2pn notification channel", android.app.NotificationManager.IMPORTANCE_NONE));
            startForeground(2,
                    new androidx.core.app.NotificationCompat.Builder(this, "my channel id").setOngoing(true).build());

            android.net.VpnService.Builder builder = new android.net.VpnService.Builder()
                    .setMtu(1500)
                    .addAddress("26.26.26.1", 24)
                    .addDnsServer("8.8.8.8")
                    .addRoute("0.0.0.0", 0);
            try {
                if ("true".equals(Util.variableRead(this, "globalvpn.txt"))) {
                    builder.addDisallowedApplication("com.example.v2pn");
                    android.util.Log.d("v2pn",
                            this.getClass().getSimpleName() + "globalvpn is true: "
                                    + Util.variableRead(this, "globalvpn.txt"));
                } else {
                    addAllowed(builder);
                    android.util.Log.d("v2pn",
                            this.getClass().getSimpleName() + "globalvpn is false: "
                                    + Util.variableRead(this, "globalvpn.txt"));
                }
            } catch (Exception e) {
                addAllowed(builder);
                android.util.Log.d("v2pn", this.getClass().getSimpleName() + "Exception: " + e.toString());
                android.util.Log.d("v2pn",
                        this.getClass().getSimpleName() + "globalvpn is false: "
                                + Util.variableRead(this, "globalvpn.txt"));
            }
            android.util.Log.d("v2pn", getApplicationInfo().nativeLibraryDir + ": "
                    + Util.listFiles(this.getApplicationInfo().nativeLibraryDir));

            android.util.Log.d("v2pn", "change config.json log path");
            try {
                // input the original file content to the StringBuffer "input"
                java.io.BufferedReader file = new java.io.BufferedReader(
                        new java.io.FileReader(getApplicationInfo().dataDir + "/config.json"));
                StringBuffer inputBuffer = new StringBuffer();
                String line;
                while ((line = file.readLine()) != null) {
                    if (line.trim().equals("\"access\": \"/dev/null\",")) {
                        line = "\"access\": \"" + getApplicationContext().getExternalFilesDir(null)
                                + "/v2rayAccessLog.txt" + "\",";
                    }
                    if (line.trim().equals("\"error\": \"/dev/null\",")) {
                        line = "\"error\": \"" + getApplicationContext().getExternalFilesDir(null)
                                + "/v2rayErrorLog.txt" + "\",";
                    }
                    inputBuffer.append(line);
                    inputBuffer.append('\n');
                }
                file.close();
                // write the new string with the replaced line OVER the same file
                java.io.FileOutputStream fileOut = new java.io.FileOutputStream(
                        getApplicationInfo().dataDir + "/config.json");
                fileOut.write(inputBuffer.toString().getBytes());
                fileOut.close();
            } catch (Exception e) {
                android.util.Log.d("v2pn", "Problem change config.json log path.");
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        android.util.Log.d("v2pn", "starting v2ray");
                        java.io.File f = new java.io.File(getApplicationInfo().dataDir + "/config.json");
                        if (f.exists() && !f.isDirectory()) {
                            android.util.Log.d("v2pn", getApplicationInfo().dataDir + "/config.json" + " exists");
                        }
                        v2pn.V2pn.v2ray_start(getApplicationInfo().dataDir);
                        // v2pn(getApplicationInfo().dataDir + "/config.json",
                        // getApplicationContext().getExternalFilesDir(null) + "/v2rayTestLog.txt");
                    } catch (Exception e) {
                        android.util.Log.d("v2pn", "v2ray Exception: " + e.toString());
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        android.util.Log.d("v2pn", "starting tun2socks");
                        android.os.ParcelFileDescriptor mInterface = builder.establish();
                        v2pn.V2pn.tun2socks_start("socks5://127.0.0.1:1080", mInterface.getFd(),
                                getApplicationContext().getExternalFilesDir(null).getAbsolutePath());
                    } catch (Exception e) {
                        android.util.Log.d("v2pn", "tun2socks Exception: " + e.toString());
                    }
                }
            }).start();
            Util.variableWrite(getApplicationContext(), "running.txt", "true");
        } catch (Exception e) {
            android.util.Log.d("v2pn", "onStartCommand(): " + e.toString());
        } finally {
        }
        android.util.Log.d("v2pn", this.getClass().getSimpleName() + "show Start Service toast");
        android.widget.Toast.makeText(getApplicationContext(), "Start Service", android.widget.Toast.LENGTH_SHORT)
                .show();
        return START_STICKY;
    }

    public static void replaceLines() {

    }

    public void addAllowed(android.net.VpnService.Builder builder) {
        try {
            // java.util.List<android.content.pm.ApplicationInfo> packages =
            // getPackageManager()
            // .getInstalledApplications(android.content.pm.PackageManager.GET_META_DATA);
            // for (android.content.pm.ApplicationInfo packageInfo : packages) {
            // // v2pn is the tag, logcat -s v2pn
            // if (packageInfo.packageName.startsWith("com.google") ||
            // packageInfo.packageName.startsWith("com.microsoft")){
            // android.util.Log.d("v2pn", "com.google :" + packageInfo.packageName);
            // builder.addAllowedApplication("packageInfo.packageName");
            // }
            // }
            try {
                java.io.File file = new java.io.File(
                        this.getApplicationInfo().dataDir + "/" + "allowedlist.txt");
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                java.util.HashSet<String> myHashSet2 = new java.util.HashSet<String>();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    myHashSet2.add(line);
                }
                reader.close();
                java.util.Iterator<String> iterator = myHashSet2.iterator();
                while (iterator.hasNext()) {
                    String pckg = iterator.next();
                    if (isPackageExisted(pckg)) {
                        builder.addAllowedApplication(pckg);
                        android.util.Log.d("v2pn", "addAllowedApplication :" + pckg);
                    }
                }
            } catch (Exception e) {
            }

            if (isPackageExisted("com.android.vending")) {
                builder.addAllowedApplication("com.android.vending");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "com.android.vending");
            }
            if (isPackageExisted("com.google.android.gms")) {
                builder.addAllowedApplication("com.google.android.gms");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "com.google.android.gms");
            }

            if (isPackageExisted("com.android.chrome")) {
                builder.addAllowedApplication("com.android.chrome");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "com.android.chrome");
            }

            if (isPackageExisted("com.mgoogle.android.gms")) {
                builder.addAllowedApplication("com.mgoogle.android.gms");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "com.android.chrome");
            }

            if (isPackageExisted("com.vanced.android.youtube")) {
                builder.addAllowedApplication("com.vanced.android.youtube");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "com.vanced.android.youtube");
            }

            if (isPackageExisted("com.microsoft.amp.apps.bingnews")) {
                builder.addAllowedApplication("com.microsoft.amp.apps.bingnews");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "com.microsoft.amp.apps.bingnews");
            }

            if (isPackageExisted("com.google.android.youtube")) {
                builder.addAllowedApplication("com.google.android.youtube");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "com.google.android.youtube");
            }

            if (isPackageExisted("com.google.android.youtube.tv")) {
                builder.addAllowedApplication("com.google.android.youtube.tv");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "com.google.android.youtube.tv");
            }

            if (isPackageExisted("com.teamsmart.videomanager.tv")) {
                builder.addAllowedApplication("com.teamsmart.videomanager.tv");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "com.teamsmart.videomanager.tv");
            }

            if (isPackageExisted("org.schabi.newpipe")) {
                builder.addAllowedApplication("org.schabi.newpipe");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "org.schabi.newpipe");
            }

            if (isPackageExisted("org.mozilla.firefox")) {
                builder.addAllowedApplication("org.mozilla.firefox");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "org.mozilla.firefox");
            }

            if (isPackageExisted("com.google.android.googlequicksearchbox")) {
                builder.addAllowedApplication("com.google.android.googlequicksearchbox");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "com.google.android.googlequicksearchbox");
            }

            if (isPackageExisted("com.google.android.apps.googleassistant")) {
                builder.addAllowedApplication("com.google.android.apps.googleassistant");
                android.util.Log.d("v2pn", "addAllowedApplication :" + "com.google.android.apps.googleassistant");
            }

            // if (isPackageExisted("com.termux")) {
            // builder.addAllowedApplication("com.termux");
            // }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isPackageExisted(String targetPackage) {
        try {
            getPackageManager().getPackageInfo(targetPackage, android.content.pm.PackageManager.GET_META_DATA);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }
}
