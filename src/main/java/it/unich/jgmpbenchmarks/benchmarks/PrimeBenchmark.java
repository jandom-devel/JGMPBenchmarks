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

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import it.unich.jgmp.MPZ;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 5, jvmArgs = { "-Xms2G", "-Xmx2G" })
@Warmup(iterations = 5)
@Measurement(iterations = 5)

/**
 * Benchmarks for JGMP: pseudo-prime number generation.
 */
public class PrimeBenchmark {

    @Param({ "10", "100", "1000" })
    public int prime;

    @Benchmark
    public MPZ nextProbablePrimeMPZ() {
        return nextProbablePrimeMPZ(prime);
    }

    @Benchmark
    public MPZ nextProbablePrimeMPZImmutable() {
        return nextProbablePrimeMPZImmutable(prime);
    }

    @Benchmark
    public BigInteger nextProbablePrimeBigInteger() {
        return nextProbablePrimeBigInteger(prime);
    }

    @Benchmark
    public us.altio.gmp4j.BigInteger nextProbablePrimeGMP4J() {
        return nextProbablePrimeGMP4J(prime);
    }

    public static void main(String[] args) throws RunnerException {
        String res = "1267650600228229401496703213077";
        if (!nextProbablePrimeBigInteger(100).equals(new BigInteger(res)))
            throw new Error("Invalid BigInteger result");
        if (!nextProbablePrimeMPZ(100).equals(new MPZ(res)))
            throw new Error("Invalid MPZ result");
        if (!nextProbablePrimeMPZImmutable(100).equals(new MPZ(res)))
            throw new Error("Invalid MPZ Immutable result");

        OptionsBuilder ob = new OptionsBuilder();
        ob.include("PrimeBenchmark");
        try {
            if (!nextProbablePrimeGMP4J(100).equals(new us.altio.gmp4j.BigInteger(res)))
                throw new Error("Invalid GMP4J result");
        } catch (LinkageError e) {
            System.err.println("Cannot launch GMP4J benchmarks: " + e);
            ob.exclude("FactorialBenchmark\\.nextProbablePrimeGMP4J");
        }
        new Runner(ob.build()).run();
    }

    /* BigInteger */
    public static BigInteger nextProbablePrimeBigInteger(int x) {
        BigInteger b = BigInteger.valueOf(2);
        b = b.pow(x);
        for (int i = 1; i <= 100; i++) {
            b = b.nextProbablePrime();
        }
        return b;
    }

    /* GMP4J */
    public static us.altio.gmp4j.BigInteger nextProbablePrimeGMP4J(int x) {
        us.altio.gmp4j.BigInteger b = us.altio.gmp4j.BigInteger.valueOf(2);
        b = b.pow(x);
        for (int i = 1; i <= 100; i++) {
            b = b.nextProbablePrime();
        }
        return b;
    }

    /* JGMP */
    public static MPZ nextProbablePrimeMPZ(int x) {
        MPZ m = new MPZ(1);
        m.mul2ExpAssign(m, x);
        for (int i = 1; i <= 100; i++) {
            m.nextprimeAssign(m);
        }
        return m;
    }

    /* JGMP */
    public static MPZ nextProbablePrimeMPZImmutable(int x) {
        MPZ m = new MPZ(1);
        m = m.mul2Exp(x);
        for (int i = 1; i <= 100; i++) {
            m = m.nextprime();
        }
        return m;
    }

}
