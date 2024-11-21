package org.hyperledger.besu.nativelib.constantine;

import com.google.common.base.Stopwatch;
import com.google.common.io.CharStreams;
import org.apache.tuweni.bytes.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ConstantineEIP196PairingCheckFFMTest {

    @Parameterized.Parameter(0)
    public String input;
    @Parameterized.Parameter(1)
    public String expectedResult;
    @Parameterized.Parameter(2)
    public String expectedGasUsed;
    @Parameterized.Parameter(3)
    public String notes;

    @Parameterized.Parameters
    public static Iterable<String[]> parameters() throws IOException {
        return CharStreams.readLines(
                        new InputStreamReader(
                                ConstantineEIP196PairingCheckFFMTest.class.getResourceAsStream("/eip196_pairing.csv"), UTF_8))
                .stream()
                .map(line -> line.split(",", 4))
                .collect(Collectors.toList());
    }

    // Use ThreadLocal to create per-thread memory segments
    private static final ThreadLocal<Arena> arena = ThreadLocal.withInitial(Arena::ofConfined);
    // TODO: revisit this, surely static memory segments are going to be a problem
    private static MemorySegment inputSegment;
    private static MemorySegment outputSegment;
    private static final Linker linker = Linker.nativeLinker();
    private static final SymbolLookup lookup = SymbolLookup.loaderLookup(); // Lookup symbols from loaded libraries
    // Define a method handle for FFM native function
    private static MethodHandle pairingHandle;

    /**
     * Attempt to enable constantine
     *
     * @return true if the native library was enabled.
     */
    static {
        try {
            System.load("/Users/garyschulte/dev/besu-native/constantine/build/darwin-aarch64/lib/libconstantineBindings.dylib");
            // Use FFM to link and call the native function
            pairingHandle = linker.downcallHandle(
                SymbolLookup.loaderLookup().find("bn254_pairingCheck").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            outputSegment = arena.get().allocateArray(ValueLayout.JAVA_BYTE, 32);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    @Test
    public void shouldCalculate() {
        if ("input".equals(input)) {
            return;
        }

        Optional<byte[]> result = Optional.empty();
        Stopwatch timer = Stopwatch.createStarted();
            for (int i=0; i < 100; i++) {
            result = useFFM();
            // result = useJNA();
        }
        System.err.println("time taken for 1000x eip196 pairing: " + timer);

        Bytes expectedComputation = expectedResult == null ? null : Bytes.fromHexString(expectedResult);

        if (result.isEmpty()) {
            assertNotNull("Notes should not be empty", notes);
//            assertNotEquals("Status should not be success", 0, status);
//            assertArrayEquals("Result should be empty", new byte[32], result);
        } else {
            Bytes actualComputation = Bytes.wrap(result.get());
            assertEquals("Computed result should match expected result", expectedComputation, actualComputation);
            assertTrue("Notes should be empty", notes.isEmpty());
        }
    }

    Optional<byte[]> useJNA() {
        byte[] inputBytes = Bytes.fromHexString(this.input).toArrayUnsafe();
        byte[] result = new byte[32];

        int res = LibConstantineEIP196.bn254_pairingCheck(result, result.length, inputBytes, inputBytes.length);
        return Optional.of(result).filter(zz -> res == 0);
    }

    Optional<byte[]> useFFM() {
        byte[] inputBytes = Bytes.fromHexString(this.input).toArrayUnsafe();
        inputSegment = arena.get().allocateArray(ValueLayout.JAVA_BYTE, inputBytes.length);
        inputSegment.copyFrom(MemorySegment.ofArray(inputBytes));
        try {
            var ret = (int) pairingHandle.invoke(outputSegment, 32, inputSegment, inputBytes.length);
            if (ret == 0) {
                return Optional.of(outputSegment.toArray(ValueLayout.JAVA_BYTE));
            }
        } catch(Throwable ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }
}
