/**
* Copyright 2022 Francesca Scozzari <francesca.scozzari@unich.it> and
*                Gianluca Amato <gianluca.amato@unich.it>
*
* JGMPBenchmarks is a set of benchmarks for JGMP. JGMPBenchmarks is free software: you can
* redistribute it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* JGMPBenchmarks is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of a MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
* details.
*
* You should have received a copy of the GNU General Public License along with
* JGMPBenchmarks. If not, see <http://www.gnu.org/licenses/>.
*/
package it.unich.jgmpbenchmarks;
import java.math.BigInteger;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import it.unich.jgmp.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 1)
@Measurement(iterations = 1)

/**
 * Benchmarks for JGMP using prime numbers
 */
public class JGMPPrimeBenchmark {

    @Param({ "10", "100", "1000"})
    public int prime ;

    @Benchmark
    public MPZ nextProbablePrimeMPZ() {
        return nextProbablePrimeMPZ(prime);
    }

    @Benchmark
    public BigInteger nextProbablePrimeBigInteger() {
        return nextProbablePrimeBigInteger(prime);
    }

    /**
     * Benchmarks for JGMP using prime numbers
     * @param args not used.
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
        .build();

        new Runner(opt).run();
    }
    
    public static BigInteger nextProbablePrimeBigInteger(int x) {
        BigInteger b = BigInteger.valueOf(2);
        b = b.pow(x);
        for (int i=1; i<= 100; i++) {
            b = b.nextProbablePrime();
        }
        return b;
    }

    public static MPZ nextProbablePrimeMPZ(int x) {
        MPZ m = new MPZ(1);
        m.mul2ExpAssign(m, x);
        for (int i=1; i<= 100; i++) {
            m.nextprimeAssign(m);
        }
        return m;
    }
}
