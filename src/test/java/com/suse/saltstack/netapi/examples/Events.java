package com.suse.saltstack.netapi.examples;

import com.suse.saltstack.netapi.AuthModule;
import com.suse.saltstack.netapi.client.SaltStackClient;
import com.suse.saltstack.netapi.config.ClientConfig;
import com.suse.saltstack.netapi.datatypes.Event;
import com.suse.saltstack.netapi.datatypes.Token;
import com.suse.saltstack.netapi.event.EventListener;
import com.suse.saltstack.netapi.event.EventStream;

import java.net.URI;

import javax.websocket.CloseReason;

/**
 * Example code listening for events on salt's event bus.
 */
public class Events {

    private static final String SALT_API_URL = "http://localhost:8000";
    private static final String USER = "saltdev";
    private static final String PASSWORD = "saltdev";

    public static void main(String[] args) {
        // Init client and set the timeout to infinite
        SaltStackClient client = new SaltStackClient(URI.create(SALT_API_URL));
        client.getConfig().put(ClientConfig.SOCKET_TIMEOUT, 0);

        try {
            // Get a login token
            Token token = client.login(USER, PASSWORD, AuthModule.AUTO);
            System.out.println("Token: " + token.getToken());

            // Init the event stream with a basic listener implementation
            EventStream eventStream = client.events(new EventListener() {
                @Override
                public void notify(Event e) {
                    System.out.println("Tag  -> " + e.getTag());
                    System.out.println("Data -> " + e.getData());
                }

                @Override
                public void eventStreamClosed(CloseReason closeReason) {
                    System.out.println("Event stream closed: " +
                            closeReason.getReasonPhrase());
                }
            });

            // Wait for events and close the event stream after 30 seconds
            System.out.println("-- Waiting for events --");
            Thread.sleep(30000);
            eventStream.close();
            System.out.println("-- Stop waiting for events --");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }
}
