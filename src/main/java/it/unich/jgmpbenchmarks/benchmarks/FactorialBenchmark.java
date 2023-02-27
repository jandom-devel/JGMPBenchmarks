
/**
 * Copyright 2022, 2023 Francesca Scozzari <francesca.scozzari@unich.it> and
 *                      Gianluca Amato <gianluca.amato@unich.it>
 *
 * JGMPBenchmarks is a set of benchmarks for JGMP. JGMPBenchmarks is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * JGMPBenchmarks is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of a MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * JGMPBenchmarks. If not, see <http://www.gnu.org/licenses/>.
 */
package it.unich.jgmpbenchmarks.benchmarks;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import org.apfloat.Apint;
import org.jscience.mathematics.number.LargeInteger;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import it.unich.jgmp.MPZ;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = { "-Xms2G", "-Xmx2G" })
@Warmup(iterations = 1)
@Measurement(iterations = 1)

/**
 * Benchmarks for JGMP: computing the factorial of a number.
 */
public class FactorialBenchmark {

    @Param({ "1000", "10000", "100000" })
    public int fact;

    @Benchmark
    public MPZ factorialMPZfast() {
        return MPZ.facUi(fact);
    }

    @Benchmark
    public MPZ factorialMPZ() {
        return factorialMPZ(fact);
    }

    @Benchmark
    public MPZ factorialMPZImmutable() {
        return factorialMPZImmutable(fact);
    }

    @Benchmark
    public BigInteger factorialBigInteger() {
        return factorialBigInteger(fact);
    }

    @Benchmark
    public LargeInteger factorialLargeInteger() {
        return factorialLargeInteger(fact);
    }

    @Benchmark
    public Apint factorialApint() {
        return factorialApint(fact);
    }

    public static void main(String[] args) throws RunnerException {
        String res = "265252859812191058636308480000000";
        if (!factorialBigInteger(30).equals(new BigInteger(res)))
            throw new Error("Invalid BigInteger result");
        if (!factorialMPZ(30).equals(new MPZ(res)))
            throw new Error("Invalid MPZ result");
        if (!factorialMPZImmutable(30).equals(new MPZ(res)))
            throw new Error("Invalid MPZ Immutable result");
        if (!factorialLargeInteger(30).equals(LargeInteger.valueOf(res)))
            throw new Error("Invalid LargeInteger result");
        if (!factorialApint(30).equals(new Apint(res)))
            throw new Error("Invalid Apint result");
        Options opt = new OptionsBuilder().include("FactorialBenchmark").build();
        new Runner(opt).run();
    }

    /* JGMP */
    public static MPZ factorialMPZ(int x) {
        MPZ f = new MPZ(1);
        while (x >= 1) {
            f.mulAssign(f, x);
            x -= 1;
        }
        return f;
    }

    /* JGMP */
    public static MPZ factorialMPZImmutable(int x) {
        MPZ f = new MPZ(1);
        while (x >= 1) {
            f = f.mul(x);
            x -= 1;
        }
        return f;
    }

    /* BigInteger */
    public static BigInteger factorialBigInteger(int x) {
        BigInteger f = BigInteger.ONE;
        while (x >= 1) {
            f = f.multiply(BigInteger.valueOf(x));
            x -= 1;
        }
        return f;
    }

    /* JScience */
    public static LargeInteger factorialLargeInteger(int x) {
        LargeInteger f = LargeInteger.ONE;
        while (x >= 1) {
            f = f.times(x);
            x -= 1;
        }
        return f;
    }

    /* Apfloat */
    public static Apint factorialApint(int x) {
        Apint f = Apint.ONE;
        while (x >= 1) {
            f = f.multiply(new Apint(x));
            x -= 1;
        }
        return f;
    }

}
