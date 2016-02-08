package com.suse.salt.netapi.calls.modules;

import com.suse.salt.netapi.calls.LocalCall;

import com.google.gson.reflect.TypeToken;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * salt.modules.cmdmod
 */
public class Cmd {

    private Cmd() { }

    public static LocalCall<String> run(String cmd) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("cmd", cmd);
        return new LocalCall<>("cmd.run", Optional.empty(), Optional.of(args),
                new TypeToken<String>(){});
    }
}
