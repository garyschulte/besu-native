package org.hyperledger.besu.nativelib.constantine;

import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bls12381_g1add;
import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bls12381_g1msm;
import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bls12381_g1mul;
import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bls12381_g2add;
import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bls12381_g2msm;
import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bls12381_g2mul;
import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bls12381_mapFp2ToG2;
import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bls12381_mapFpToG1;
import static org.hyperledger.besu.nativelib.constantine.LibConstantineBindings.bls12381_pairingCheck;

public class LibConstantineEIP2537 {

    public static byte[] g1add(byte[] inputs) {
        byte[] result = new byte[128];
        int status = bls12381_g1add(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("eth_evm_bls12381_g1add failed with status: " + status);
        }
        return result;
    }

    public static byte[] g2add(byte[] inputs) {
        byte[] result = new byte[256];
        int status = bls12381_g2add(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("eth_evm_bls12381_g2add failed with status: " + status);
        }
        return result;
    }

    public static byte[] g1mul(byte[] inputs) {
        byte[] result = new byte[128];
        int status = bls12381_g1mul(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("eth_evm_bls12381_g1mul failed with status: " + status);
        }
        return result;
    }

    public static byte[] g2mul(byte[] inputs) {
        byte[] result = new byte[256];
        int status = bls12381_g2mul(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("eth_evm_bls12381_g2mul failed with status: " + status);
        }
        return result;
    }

    public static byte[] g1msm(byte[] inputs) {
        byte[] result = new byte[128];
        int status = bls12381_g1msm(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("eth_evm_bls12381_g1msm failed with status: " + status);
        }
        return result;
    }

    public static byte[] g2msm(byte[] inputs) {
        byte[] result = new byte[256];
        int status = bls12381_g2msm(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("eth_evm_bls12381_g2msm failed with status: " + status);
        }
        return result;
    }

    public static byte[] pairingCheck(byte[] inputs) {
        byte[] result = new byte[32];
        int status = bls12381_pairingCheck(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("eth_evm_bls12381_pairingcheck failed with status: " + status);
        }
        return result;
    }

    public static byte[] mapFpToG1(byte[] inputs) {
        byte[] result = new byte[128];
        int status = bls12381_mapFpToG1(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("eth_evm_bls12381_map_fp_to_g1 failed with status: " + status);
        }
        return result;
    }

    public static byte[] mapFp2ToG2(byte[] inputs) {
        byte[] result = new byte[256];
        int status = bls12381_mapFp2ToG2(result, result.length, inputs, inputs.length);
        if (status != 0) {
            throw new RuntimeException("eth_evm_bls12381_map_fp2_to_g2 failed with status: " + status);
        }
        return result;
    }
}
