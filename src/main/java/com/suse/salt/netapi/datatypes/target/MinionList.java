package com.suse.salt.netapi.datatypes.target;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Target for specifying a list of minions.
 */
public class MinionList extends AbstractTarget<List<String>> implements Target<List<String>>, SSHTarget<List<String>> {

    /**
     * Constructor taking a list of minions as strings.
     *
     * @param targets as a list of strings
     */
    public MinionList(List<String> targets) {
        super(targets);
    }

    /**
     * Constructor taking an optional list of strings.
     *
     * @param targets as strings
     */
    public MinionList(String... targets) {
        super(Arrays.asList(targets));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TargetType getType() {
        return TargetType.LIST;
    }
}
