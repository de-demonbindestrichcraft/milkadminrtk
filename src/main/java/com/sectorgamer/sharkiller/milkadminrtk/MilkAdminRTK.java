package com.sectorgamer.sharkiller.milkadminrtk;

import com.drdanick.McRKit.module.Module;
import com.drdanick.McRKit.module.ModuleLoader;
import com.drdanick.McRKit.module.ModuleMetadata;
import com.drdanick.McRKit.ToolkitEvent;
import com.sectorgamer.sharkiller.milkadminrtk.Config.Configuration;
import com.sectorgamer.sharkiller.milkadminrtk.Config.GenerallyFileManager;
import com.sectorgamer.sharkiller.milkadminrtk.Config.WerrisMilkAdminConfig;
import com.sectorgamer.sharkiller.milkadminrtk.Config.WerrisRemoteToolkitConfig;
import com.sectorgamer.sharkiller.milkadminrtk.RTK.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

//This class defines a skeleton module that will be enabled when the server is stopped, and disabled when the server is started.
public class MilkAdminRTK extends Module implements RTKListener {

    private static final Logger LOG = Logger.getLogger("milkAdmin");
    RTKInterface api = null;
    String PluginDir = "plugins/milkAdmin";
    String userRTK, passRTK, hostRTK;
    int portRTK;
    Configuration Settings;
    private WebServer server;
    private WerrisMilkAdminConfig werrisMilkAdminConfig;
    WerrisRemoteToolkitConfig werrisRemoteToolkitConfig;

    public MilkAdminRTK(ModuleMetadata meta, ModuleLoader moduleLoader, ClassLoader cLoader) {
        super(meta, moduleLoader, cLoader, ToolkitEvent.ON_SERVER_HOLD, ToolkitEvent.ON_SERVER_RESTART);
        //the last two parameters define the events where the plugin is enabled and disabled respectively.
        //fix formatting on logger
        Logger rootlog = Logger.getLogger("");
        for (Handler h : rootlog.getHandlers()) { //remove all handlers
            h.setFormatter(new McSodFormatter());
        }
        Settings = null;
        server = null;
        werrisMilkAdminConfig = null;
        werrisRemoteToolkitConfig = null;
        init();
    }

    protected void onEnable() {
        LOG.info("[milkAdminRTK] Module enabled!");
        server = new WebServer(this, LOG);
    }
    
        public File getLoggedinBackupFile() {
        return new File(PluginDir + File.separator + "loggedin_backup.ini");
    }

    public Map<String, String> getFileContent(File file) {
        try {
            if (file.exists()) {
                List<String> AllgemeinFileReader = GenerallyFileManager.AllgemeinFileReader(file);
                Map<String, String> input = new HashMap<String, String>();
                for (String value : AllgemeinFileReader) {

                    if (!value.contains("=")) {
                        continue;
                    }
                    String[] split = value.split("=");
                    input.put(split[0], split[1]);
                }
                return input;
            }
        } catch (IOException ex) {
            return null;
        }
        return null;
    }

    public void backupLoggedIn() {
            File loggedin = new File(PluginDir + File.separator + "loggedin.ini");
            Map<String,String> logins = getFileContent(loggedin);
            if(logins == null)
            {
                return;
            }
           GenerallyFileManager.FileWrite(logins, getLoggedinBackupFile(), "=");
    }

    public void onRTKStringReceived(String s) {
        if (s.equals("RTK_TIMEOUT")) {
            LOG.info("[milkAdminRTK] RTK not response to the user '" + userRTK + "' in the port '" + portRTK + "' bad configuration?");
        } else {
            LOG.info("[milkAdminRTK] From RTK: " + s);
        }
    }

    protected void onDisable() {
        LOG.info("[milkAdminRTK] Module disabled!");
        try {
            server.stopServer();
        } catch (IOException e) {
            LOG.severe("[milkAdminRTK] Error closing milkAdmin Server");
            e.printStackTrace();
        }
    }

    private void initOldRTKModule() {
        try {
            Settings = new Configuration(new File(PluginDir + File.separator + "settings.yml"));
            userRTK = Settings.getString("RTK.Username", "user");
            passRTK = Settings.getString("RTK.Passwort", "pass");
            hostRTK = Settings.getString("RTK.Host", "localhost");
            portRTK = Settings.getInt("RTK.Port", 25561);
            if (api == null) {
                api = RTKInterface.createRTKInterface(portRTK, hostRTK, userRTK, passRTK);
            }
        } catch (RTKInterfaceException e) {
            e.printStackTrace();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[milkAdminRTK] Error instantiating milkAdmin Module", e);


        }
    }

    private void initRTKModule() {
        try {
            if (werrisRemoteToolkitConfig == null) {
                werrisRemoteToolkitConfig = new WerrisRemoteToolkitConfig();
            } else {
                werrisRemoteToolkitConfig.update();
            }
            if (werrisMilkAdminConfig == null) {
                werrisMilkAdminConfig = new WerrisMilkAdminConfig();
            } else {
                werrisMilkAdminConfig.update();
            }
            if (Settings == null) {
                Settings = new Configuration(new File(PluginDir + File.separator + "settings.yml"));
            }
            userRTK = werrisMilkAdminConfig.getString("rtk.username");
            passRTK = werrisMilkAdminConfig.getString("rtk.password");
            hostRTK = werrisRemoteToolkitConfig.getString("remote-bind-address");
            portRTK = werrisRemoteToolkitConfig.getInt("remote-control-port");
            if (api == null) {
                api = RTKInterface.createRTKInterface(portRTK, hostRTK, userRTK, passRTK);
                api.registerRTKListener(this);
            }
        } catch (RTKInterfaceException e) {
            e.printStackTrace();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "[milkAdminRTK] Error instantiating milkAdmin Module", e);

        }
    }

    public void init() {
        initRTKModule();
        if (api == null) {
            throw new NullPointerException("ddddd");
        }
    }

    public void deInitRTKMoudule() {
        werrisRemoteToolkitConfig = null;
        Settings = null;
        userRTK = null;
        passRTK = null;
        hostRTK = null;
        portRTK = 0;
        api = null;
    }

    public void deinit() {
        deInitRTKMoudule();
        server = null;
    }
}