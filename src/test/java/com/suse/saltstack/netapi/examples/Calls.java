package com.suse.saltstack.netapi.examples;

import com.suse.saltstack.netapi.AuthModule;
import com.suse.saltstack.netapi.calls.WheelResult;
import com.suse.saltstack.netapi.calls.modules.Grains;
import com.suse.saltstack.netapi.calls.modules.Test;
import com.suse.saltstack.netapi.calls.wheel.Key;
import com.suse.saltstack.netapi.client.SaltStackClient;
import com.suse.saltstack.netapi.datatypes.target.Glob;
import com.suse.saltstack.netapi.datatypes.target.MinionList;
import com.suse.saltstack.netapi.datatypes.target.Target;
import com.suse.saltstack.netapi.exception.SaltStackException;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Example code calling salt modules using the generic interface.
 */
public class Calls {

    private static final String SALT_API_URL = "http://localhost:8000";
    private static final String USER = "saltdev";
    private static final String PASSWORD = "saltdev";

    public static void main(String[] args) throws SaltStackException {
        // Init the client
        SaltStackClient client = new SaltStackClient(URI.create(SALT_API_URL));

        // Ping all minions using a glob matcher
        Target<String> globTarget = new Glob("*");
        Map<String, Boolean> results = client.callSync(
                Test.ping(), globTarget, USER, PASSWORD, AuthModule.AUTO);

        System.out.println("--> Ping results:\n");
        results.keySet().forEach(
                key -> System.out.println(key + " -> " + results.get(key)));

        // Get the grains from a list of minions
        Target<List<String>> minionList = new MinionList("minion1", "minion2");
        Map<String, Map<String, Object>> grains = client.callSync(
                Grains.items(true), minionList, USER, PASSWORD, AuthModule.AUTO);

        for (String minion : grains.keySet()) {
            System.out.println("\n--> Listing grains for '" + minion + "':\n");
            Map<String, Object> minionGrains = grains.get(minion);
            minionGrains.keySet().forEach(
                    key -> System.out.println(key + ": " + minionGrains.get(key)));
        }

        // Call a wheel function: list accepted and pending minion keys
        WheelResult<Key.Names> keysResult = client.callSync(
                Key.listAll(), USER, PASSWORD, AuthModule.AUTO);
        Key.Names keys = keysResult.getData().getResult();

        System.out.println("\n--> Accepted minion keys:\n");
        keys.getMinions().forEach(minion -> System.out.println(minion));

        System.out.println("\n--> Pending minion keys:\n");
        keys.getUnacceptedMinions().forEach(minion -> System.out.println(minion));
    }
}
