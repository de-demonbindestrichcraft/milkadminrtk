/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sectorgamer.sharkiller.milkadminrtk.Config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ABC
 */
public final class WerrisRemoteToolkitConfig {

    private Map<String, String> properties;
    private boolean isRTKInstalled;

    public WerrisRemoteToolkitConfig() {
        properties = new HashMap<String, String>();
        update();
    }

    public void update() {
        properties.clear();
        File remoteProperties = new File("toolkit/remote.properties");
        if (!remoteProperties.exists()) {
            isRTKInstalled = false;
        } else {
            isRTKInstalled = true;
        }
        String message_playback_count = GenerallyFileManager.FileReadLine("message-playback-count", remoteProperties, "=");
        String remoteBindAddress = GenerallyFileManager.FileReadLine("remote-bind-address", remoteProperties, "=");
        String auth_salt = GenerallyFileManager.FileReadLine("auth-salt", remoteProperties, "=");
        String shell_password_mask = GenerallyFileManager.FileReadLine("shell-password-mask", remoteProperties, "=");
        String telnet_enabled = GenerallyFileManager.FileReadLine("telnet-enabled", remoteProperties, "=");
        String remoteControlPort = GenerallyFileManager.FileReadLine("remote-control-port", remoteProperties, "=");
        String shell_input_echo = GenerallyFileManager.FileReadLine("shell-input-echo", remoteProperties, "=");
        properties.put("message-playback-count", message_playback_count);
        properties.put("remote-bind-address", remoteBindAddress);
        properties.put("shell-password-mask", shell_password_mask);
        properties.put("telnet-enabled", telnet_enabled);
        properties.put("remote-control-port", remoteControlPort);
        properties.put("shell-input-echo", shell_input_echo);
    }

    public synchronized Map<String, String> getProperties() {
        return new HashMap<String, String>(properties);
    }

    public String getString(String key) {
        return properties.get(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(properties.get(key));
    }

    public boolean isRTKInstalled() {
        return isRTKInstalled;
    }
}
