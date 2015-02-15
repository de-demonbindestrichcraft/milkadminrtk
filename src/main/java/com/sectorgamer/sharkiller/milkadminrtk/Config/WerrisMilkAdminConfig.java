/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sectorgamer.sharkiller.milkadminrtk.Config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ABC
 */
public final class WerrisMilkAdminConfig {

    private Map<String, String> properties;
    private boolean UsingRTK;

    public WerrisMilkAdminConfig() throws IOException {
        properties = new HashMap<String, String>();
        update();
    }

    public synchronized void update() throws IOException {
        properties.clear();

        String UsingRTK = GenerallyFileManager.FileReadLine("    UsingRTK", new File("plugins/milkAdmin/settings.yml"), ": ").trim().replaceAll("'", "");
        String user = GenerallyFileManager.FileReadLine("    Username", new File("plugins/milkAdmin/settings.yml"), ": ").trim().replaceAll("'", "");
        String pass = GenerallyFileManager.FileReadLine("    Password", new File("plugins/milkAdmin/settings.yml"), ": ").trim().replaceAll("'", "");
        this.UsingRTK = Boolean.parseBoolean(UsingRTK);
        properties.put("rtk.username", user.replaceAll("'", ""));
        properties.put("rtk.password", pass.replaceAll("'", ""));
    }

    public synchronized Map<String, String> getProperties() {
        return new HashMap<String, String>(properties);
    }

    public String getString(String key) {
        return properties.get(key);
    }

    public boolean UsingRTK() {
        return UsingRTK;
    }
}
