package com.suse.saltstack.netapi.calls;

import com.google.gson.reflect.TypeToken;
import com.suse.saltstack.netapi.AuthModule;
import com.suse.saltstack.netapi.client.SaltStackClient;
import com.suse.saltstack.netapi.datatypes.target.Target;
import com.suse.saltstack.netapi.exception.SaltStackException;
import com.suse.saltstack.netapi.results.Result;

import static com.suse.saltstack.netapi.utils.ClientUtils.parameterizedType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class representing a function call of a salt execution module.
 *
 * @param <R> the return type of the called function
 */
public class LocalCall<R> implements Call<R> {

    private final String functionName;
    private final Optional<List<?>> arg;
    private final Optional<Map<String, ?>> kwarg;
    private final TypeToken<R> returnType;

    public LocalCall(String functionName, Optional<List<?>> arg,
            Optional<Map<String, ?>> kwarg, TypeToken<R> returnType) {
        this.functionName = functionName;
        this.arg = arg;
        this.kwarg = kwarg;
        this.returnType = returnType;
    }

    public TypeToken<R> getReturnType() {
        return returnType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getPayload() {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("fun", functionName);
        arg.ifPresent(arg -> payload.put("arg", arg));
        kwarg.ifPresent(kwarg -> payload.put("kwarg", kwarg));
        return payload;
    }

    /**
     * Calls a execution module function on the given target asynchronously and
     * returns information about the scheduled job that can be used to query the result.
     * Authentication is done with the token therefore you have to login prior
     * to using this function.
     *
     * @param client connection instance with Saltstack
     * @param target the target for the function
     * @return information about the scheduled job
     * @throws SaltStackException if anything goes wrong
     */
    public LocalAsyncResult<R> callAsync(final SaltStackClient client, Target<?> target)
            throws SaltStackException {
        Map<String, Object> customArgs = new HashMap<>();
        customArgs.putAll(getPayload());
        customArgs.put("tgt", target.getTarget());
        customArgs.put("expr_form", target.getType());

        Result<List<LocalAsyncResult<R>>> wrapper = client.call(
                this, Client.LOCAL_ASYNC, "/",
                Optional.of(customArgs),
                new TypeToken<Result<List<LocalAsyncResult<R>>>>(){});
        LocalAsyncResult<R> result = wrapper.getResult().get(0);
        result.setType(getReturnType());
        return result;
    }

    /**
     * Calls a execution module function on the given target asynchronously and
     * returns information about the scheduled job that can be used to query the result.
     * Authentication is done with the given credentials no session token is created.
     *
     * @param client connection instance with Saltstack
     * @param target the target for the function
     * @param username username for authentication
     * @param password password for authentication
     * @param authModule authentication module to use
     * @return information about the scheduled job
     * @throws SaltStackException if anything goes wrong
     */
    public LocalAsyncResult<R> callAsync(final SaltStackClient client, Target<?> target,
            String username, String password, AuthModule authModule)
            throws SaltStackException {
        Map<String, Object> customArgs = new HashMap<>();
        customArgs.putAll(getPayload());
        customArgs.put("username", username);
        customArgs.put("password", password);
        customArgs.put("eauth", authModule.getValue());
        customArgs.put("tgt", target.getTarget());
        customArgs.put("expr_form", target.getType());

        Result<List<LocalAsyncResult<R>>> wrapper = client.call(
                this, Client.LOCAL_ASYNC, "/run",
                Optional.of(customArgs),
                new TypeToken<Result<List<LocalAsyncResult<R>>>>(){});
        LocalAsyncResult<R> result = wrapper.getResult().get(0);
        result.setType(getReturnType());
        return result;
    }

    /**
     * Calls a execution module function on the given target and synchronously
     * waits for the result. Authentication is done with the token therefore you
     * have to login prior to using this function.
     *
     * @param client connection instance with Saltstack
     * @param target the target for the function
     * @return a map containing the results with the minion name as key
     * @throws SaltStackException if anything goes wrong
     */
    public Map<String, R> callSync(final SaltStackClient client, Target<?> target)
            throws SaltStackException {
        Map<String, Object> customArgs = new HashMap<>();
        customArgs.put("tgt", target.getTarget());
        customArgs.put("expr_form", target.getType());

        Type type = parameterizedType(null, Map.class, String.class,
                getReturnType().getType());
        Type listType = parameterizedType(null, List.class, type);
        Type wrapperType = parameterizedType(null, Result.class, listType);

        @SuppressWarnings("unchecked")
        Result<List<Map<String, R>>> wrapper = client.call(this, Client.LOCAL, "/",
                Optional.of(customArgs),
                (TypeToken<Result<List<Map<String, R>>>>) TypeToken.get(wrapperType));
        return wrapper.getResult().get(0);
    }

    /**
     * Calls a execution module function on the given target and synchronously
     * waits for the result. Authentication is done with the given credentials
     * no session token is created.
     *
     * @param client connection instance with Saltstack
     * @param target the target for the function
     * @param username username for authentication
     * @param password password for authentication
     * @param authModule authentication module to use
     * @return a map containing the results with the minion name as key
     * @throws SaltStackException if anything goes wrong
     */
    public Map<String, R> callSync(final SaltStackClient client, Target<?> target,
            String username, String password, AuthModule authModule)
            throws SaltStackException {
        Map<String, Object> customArgs = new HashMap<>();
        customArgs.putAll(getPayload());
        customArgs.put("username", username);
        customArgs.put("password", password);
        customArgs.put("eauth", authModule.getValue());
        customArgs.put("tgt", target.getTarget());
        customArgs.put("expr_form", target.getType());

        Type mapType = parameterizedType(null, Map.class, String.class,
                getReturnType().getType());
        Type listType = parameterizedType(null, List.class, mapType);
        Type wrapperType = parameterizedType(null, Result.class, listType);

        @SuppressWarnings("unchecked")
        Result<List<Map<String, R>>> wrapper = client.call(this, Client.LOCAL, "/run",
                Optional.of(customArgs),
                (TypeToken<Result<List<Map<String, R>>>>) TypeToken.get(wrapperType));
        return wrapper.getResult().get(0);
    }
}
