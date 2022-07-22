# v2pn
v2pn, a very simple but usable Android vpn app based on https://github.com/v2fly/v2ray-core and https://github.com/xjasonlyu/tun2socks

It has http server api with https://github.com/NanoHttpd/nanohttpd

installation:

1. use gomobile to generate .aar from https://github.com/garymaxallen/vt
2. put .aar in app/libs/vpn folder
3. gradlew.bat assembleDebug/installDebug

I'm an android amateur/rookie, only care about usability.

So this app is very simple, only for personal use.

It doesn't have fancy GUI settings.

Change settings with curl Termux(https://github.com/termux/termux-app)

### Get allowed apps
curl http://localhost:8080/v2pn/allowedlist

### Get global vpn status
curl http://localhost:8080/v2pn/globalvpn

### Get vpn running status
curl http://localhost:8080/v2pn/running

### upload v2ray config.json file, must upload before connect to vpn
curl http://localhost:8080/v2pn/fileupload -F "filename=@config.json"

### add app to allowed list
curl http://localhost:8080/v2pn/allowedlist -d "com.termux"

### set vpn global or not 
curl http://localhost:8080/v2pn/globalvpn -d "true"
curl http://localhost:8080/v2pn/globalvpn -d "false"
