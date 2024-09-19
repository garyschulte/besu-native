package org.hyperledger.besu.nativelib.constantine;

import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bn254_g1add;
import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bn254_g1mul;
import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bn254_pairingCheck;

public class LibConstantineEIP196 {

    public static byte[] add(byte[] inputs) {
        byte[] result = new byte[64];
        int status = bn254_g1add(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("ctt_eth_evm_bn254_g1add failed with status: " + status);
        }
        return result;
    }

    public static byte[] mul(byte[] inputs) {
        byte[] result = new byte[64];
        int status = bn254_g1mul(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("ctt_eth_evm_bn254_g1mul failed with status: " + status);
        }
        return result;
    }

    public static byte[] pairingCheck(byte[] inputs) {
        byte[] result = new byte[32];
        int status = bn254_pairingCheck(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("ctt_eth_evm_bn254_pairingCheck failed with status: " + status);
        }
        return result;
    }
}
