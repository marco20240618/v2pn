package com.example.v2pn;

public class MyQSTileService extends android.service.quicksettings.TileService {

    @Override
    public void onClick() {
        super.onClick();
        android.util.Log.d("v2pn", "MyQSTileService onClick(): ");
        if (getQsTile().getState() == android.service.quicksettings.Tile.STATE_INACTIVE) {
            try {
                android.util.Log.d("v2pn", "MyQSTileService onClick() ACTIVE: ");
                android.content.Intent v2pn_intent = new android.content.Intent(this, V2pnService.class);
                startForegroundService(v2pn_intent);
                getQsTile().setState(android.service.quicksettings.Tile.STATE_ACTIVE);
                getQsTile().updateTile();
            } catch (Exception e) {
                android.util.Log.d("v2pn", "onClick() Exception: " + e.toString());
            }
        } else if (getQsTile().getState() == android.service.quicksettings.Tile.STATE_ACTIVE) {
            android.util.Log.d("v2pn", "MyQSTileService onClick() INACTIVE: ");
            Util.killAll(this);
            Util.variableWrite(this, "running.txt", "false");
            getQsTile().setState(android.service.quicksettings.Tile.STATE_INACTIVE);
            getQsTile().updateTile();
        } else {
            android.util.Log.d("v2pn", "MyQSTileService state error");
        }
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        android.util.Log.d("v2pn", "onStartListening()");
        try {
            String pid = Util.variableRead(this, "v2pnPid.txt");
            java.io.File file = new java.io.File("/proc/" + pid);
            if (file.exists()) {
                android.util.Log.d("v2pn", "v2pnPid pid file path exists: " + file.getPath());
            }
            if ("true".equals(Util.variableRead(this, "running.txt")) && file.exists()) {
                getQsTile().setState(android.service.quicksettings.Tile.STATE_ACTIVE);
                getQsTile().updateTile();
                android.util.Log.d("v2pn", "running is true");
            } else if ("false".equals(Util.variableRead(this, "running.txt"))) {
                getQsTile().setState(android.service.quicksettings.Tile.STATE_INACTIVE);
                getQsTile().updateTile();
                android.util.Log.d("v2pn", "running is false");
            } else {
                android.util.Log.d("v2pn", "read running error");
                getQsTile().setState(android.service.quicksettings.Tile.STATE_INACTIVE);
                getQsTile().updateTile();
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onDestroy() {
        android.util.Log.d("v2pn", "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        android.util.Log.d("v2pn", "onTileAdded()");
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        android.util.Log.d("v2pn", "onTileRemoved()");
        super.onTileRemoved();
    }

    @Override
    public void onStopListening() {
        android.util.Log.d("v2pn", "onStopListening()");
        super.onStopListening();
    }
}
