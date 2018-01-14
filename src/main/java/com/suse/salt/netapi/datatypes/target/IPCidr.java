package com.suse.salt.netapi.datatypes.target;

/**
 * Target for specifying minions by IP CIDR.
 */
public class IPCidr extends AbstractTarget<String> implements Target<String> {

    /**
     * Default constructor.
     */
    public IPCidr(String cidr) { super(cidr); }

    /**
     * {@inheritDoc}
     */
    @Override
    public TargetType getType() { return TargetType.IPCIDR; }
}
