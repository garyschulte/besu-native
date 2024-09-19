package org.hyperledger.besu.nativelib.constantine;

import com.sun.jna.Native;

public class LibConstantineBindings {

  public static final boolean ENABLED;

  static {
    boolean enabled;
    try {
      Native.register(LibConstantineBindings.class, "constantinebindings");
      enabled = true;
    } catch (final Throwable t) {
      t.printStackTrace();
      enabled = false;
    }
    ENABLED = enabled;
  }

  // bls12-381
  public static native int bls12381_g1add(byte[] r, int r_len, byte[] inputs, int inputs_len);
  public static native int bls12381_g2add(byte[] r, int r_len, byte[] inputs, int inputs_len);
  public static native int bls12381_g1mul(byte[] r, int r_len, byte[] inputs, int inputs_len);
  public static native int bls12381_g2mul(byte[] r, int r_len, byte[] inputs, int inputs_len);
  public static native int bls12381_g1msm(byte[] r, int r_len, byte[] inputs, int inputs_len);
  public static native int bls12381_g2msm(byte[] r, int r_len, byte[] inputs, int inputs_len);
  public static native int bls12381_pairingCheck(byte[] r, int r_len, byte[] inputs, int inputs_len);
  public static native int bls12381_mapFpToG1(byte[] r, int r_len, byte[] inputs, int inputs_len);
  public static native int bls12381_mapFp2ToG2(byte[] r, int r_len, byte[] inputs, int inputs_len);

  // bn254
  public static native int bn254_g1add(byte[] r, int r_len, byte[] inputs, int inputs_len);
  public static native int bn254_g1mul(byte[] r, int r_len, byte[] inputs, int inputs_len);
  public static native int bn254_pairingCheck(byte[] r, int r_len, byte[] inputs, int inputs_len);

}
