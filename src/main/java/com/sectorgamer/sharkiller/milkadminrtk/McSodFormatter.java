/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sectorgamer.sharkiller.milkadminrtk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author ABC
 */
public class McSodFormatter extends Formatter {

    SimpleDateFormat dformat;

    public McSodFormatter() {
        dformat = new SimpleDateFormat("HH:mm:ss");
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder buf = new StringBuilder();
        buf.append(dformat.format(new Date(record.getMillis()))).append(" [").append(record.getLevel().getName()).append("] ").append(this.formatMessage(record)).append('\n');
        if (record.getThrown() != null) {
            buf.append('\t').append(record.getThrown().toString()).append('\n');
        }
        return buf.toString();
    }
}
