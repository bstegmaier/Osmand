package net.osmand.plus;

import android.content.Context;
import android.content.Intent;

import net.osmand.PlatformUtil;

import org.apache.commons.logging.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;


public class PebbleCommunicator {
    public final static String PEBBLE_ALERT = "PEBBLE_ALERT";
    private static final Log log = PlatformUtil.getLog(PebbleCommunicator.class);
    private static final UUID APP_UUID = UUID.fromString("5f2386f3-066e-4e40-acb6-38a7dd1c019c");


    public static void sendVoiceAlertToPebble(Context ctx, String bld) {
        final Intent i = new Intent("com.getpebble.action.SEND_NOTIFICATION");
        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("title", "Voice");
        data.put("body", bld.toString());
        final JSONObject jsonData = new JSONObject(data);
        final String notificationData = new JSONArray().put(jsonData).toString();
        i.putExtra("messageType", PEBBLE_ALERT);
        i.putExtra("sender", "OsmAnd");
        i.putExtra("notificationData", notificationData);
        ctx.sendBroadcast(i);
        log.info("Send message to pebble " + bld.toString());
    }

    public static boolean isWatchConnected(Context ctx) {
        boolean connected = PebbleKit.isWatchConnected(ctx);
        log.info("Pebble is " + (connected ? "connected" : "not connected"));
        return connected;
    }

    public static void startWatchApp(Context ctx) {
        log.info("Launching Pebble Watchapp..");
        PebbleKit.startAppOnPebble(ctx, APP_UUID);
    }

    public static void stopWatchApp(Context ctx) {
        log.info("Stopping Pebble Watchapp..");
        PebbleKit.closeAppOnPebble(ctx, APP_UUID);
    }

    public static void sendData(Context ctx, int message_type, int key1, int value1, int key2, String value2, int key3, int value3) {
        PebbleDictionary myDict = new PebbleDictionary();
        myDict.addInt16(Keys.MESSAGE_TYPE, (short)message_type);
        myDict.addInt16(key1, (short)value1);
        myDict.addString(key2, value2);
        myDict.addInt16(key3, (short)value3);
        PebbleKit.sendDataToPebble(ctx, APP_UUID, myDict);
    }

    public static void sendAlert(Context ctx, int alarmType, String text) {
        PebbleDictionary msg = new PebbleDictionary();
        msg.addInt16(Keys.MESSAGE_TYPE, (short) Keys.ALERT_MESSAGE);

        msg.addInt16(Keys.ALERT_TYPE, (short)alarmType);
        msg.addString(Keys.ALERT_TEXT, text);

        PebbleKit.sendDataToPebble(ctx, APP_UUID, msg);

    }

    public static void sendInstruction(Context ctx, int instructionType, int distance, String text) {
        PebbleDictionary msg = new PebbleDictionary();
        msg.addInt16(Keys.MESSAGE_TYPE, (short) Keys.INSTRUCTION_MESSAGE);

        msg.addInt16(Keys.INSTRUCTION_TYPE, (short)instructionType);
        msg.addInt16(Keys.INSTRUCTION_DISTANCE, (short)distance);
        msg.addString(Keys.INSTRUCTION_TEXT, text);

        PebbleKit.sendDataToPebble(ctx, APP_UUID, msg);
    }

    public static void sendNavigationInfo(Context ctx, String destination, int dist_to_dest, int time_to_dest) {
        PebbleDictionary msg = new PebbleDictionary();
        msg.addInt16(Keys.MESSAGE_TYPE, (short)Keys.NAVIGATION_INFO_MESSAGE);

        msg.addString(Keys.NAVIGATION_DESTINATION, destination);
        msg.addInt16(Keys.NAVIGATION_DIST_TO_DEST, (short)dist_to_dest);
        msg.addInt16(Keys.NAVIGATION_ETA, (short)time_to_dest);

        PebbleKit.sendDataToPebble(ctx, APP_UUID, msg);
    }
}
