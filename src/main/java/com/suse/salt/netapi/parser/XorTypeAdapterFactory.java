package com.suse.salt.netapi.parser;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.suse.salt.netapi.errors.FunctionNotAvailable;
import com.suse.salt.netapi.errors.GenericSaltError;
import com.suse.salt.netapi.errors.ModuleNotSupported;
import com.suse.salt.netapi.errors.SaltError;
import com.suse.salt.netapi.utils.Xor;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TypeAdaptorFactory creating TypeAdapters for Xor
 */
public class XorTypeAdapterFactory implements TypeAdapterFactory {

    private static final Pattern FN_UNAVAILABLE =
            Pattern.compile("'([^']+)' is not available.");
    private static final Pattern MODULE_NOT_SUPPORTED =
            Pattern.compile("'([^']+)' __virtual__ returned False");

    @Override
    @SuppressWarnings("unchecked")
    public <A> TypeAdapter<A> create(Gson gson, TypeToken<A> typeToken) {
        Type type = typeToken.getType();
        boolean isXor = typeToken.getRawType() == Xor.class;
        boolean isParameterized = type instanceof ParameterizedType;
        if (isXor && isParameterized) {
            Type rightType = ((ParameterizedType) type).getActualTypeArguments()[1];
            TypeAdapter<?> elementAdapter = gson.getAdapter(TypeToken.get(rightType));
            return (TypeAdapter<A>) errorAdapter(elementAdapter);
        } else {
            return null;
        }
    }

    private <R> TypeAdapter<Xor<SaltError, R>> errorAdapter(TypeAdapter<R> innerAdapter) {
        return new TypeAdapter<Xor<SaltError, R>>() {
            @Override
            public Xor<SaltError, R> read(JsonReader in) throws IOException {
                JsonElement json = TypeAdapters.JSON_ELEMENT.read(in);
                try {
                    R value = innerAdapter.fromJsonTree(json);
                    return Xor.right(value);
                } catch (Throwable e) {
                    if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                        String string = json.getAsJsonPrimitive().getAsString();
                        Matcher fnuMatcher = FN_UNAVAILABLE.matcher(string);
                        Matcher mnsMatcher = MODULE_NOT_SUPPORTED.matcher(string);
                        if (fnuMatcher.find()) {
                            String fn = fnuMatcher.group(1);
                            return Xor.left(new FunctionNotAvailable(fn));
                        } else if (mnsMatcher.find()) {
                            String module = mnsMatcher.group(1);
                            return Xor.left(new ModuleNotSupported(module));
                        } else {
                            return Xor.left(new GenericSaltError(json, e));
                        }
                    } else {
                        return Xor.left(new GenericSaltError(json, e));
                    }
                }
            }

            @Override
            public void write(JsonWriter out, Xor<SaltError, R> xor) throws IOException {
                throw new JsonParseException("Writing Xor is not supported");
            }
        };
    }
}
