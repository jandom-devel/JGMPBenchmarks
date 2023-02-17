/**
 * Copyright 2022, 2023 Francesca Scozzari <francesca.scozzari@unich.it> and
 *                      Gianluca Amato <gianluca.amato@unich.it>
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import it.unich.jgmp.MPF;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = { "-Xms2G", "-Xmx2G" })
@Warmup(iterations = 1)
@Measurement(iterations = 1)

/**
 * Benchmarks for JGMP computing Euler's number digits.
 */
public class EulerNumberDigitsBenchmark {

    @Param({ "10" })
    public int n;

    @Param({ "30" })
    public int scale;

    @Benchmark
    public MPF eulerMPZ() {
        return eulerMPZ(n, scale);
    }

    @Benchmark
    public BigDecimal eulerBigDecimal() {
        return eulerBigDecimal(n, scale);
    }

    /**
     * Benchmarks for JGMP computing Euler's number digits.
     *
     * @param args not used.
     */
    public static void main(String[] args) throws RunnerException {
        System.out.println(eulerBigDecimal(30, 39));
        System.out.println(eulerMPZ(30, 30));
        Options opt = new OptionsBuilder().build();

        new Runner(opt).run();
    }

    public static MPF eulerMPZ(int x, int scale) {
        MPF.setDefaultPrec(128);
        MPF one = new MPF(1);
        MPF fact = new MPF(1); // fact = n!
        MPF e = new MPF(1);
        for (int i = 1; i <= x; i++) {
            e.addAssign(e, one.div(fact));
            fact.mulUiAssign(fact, i + 1);
            //System.out.println(e+" "+n+" "+fact+" "+one.divide(fact,scale,RoundingMode.CEILING));
        }
        return e;
    }

    public static BigDecimal eulerBigDecimal(int x, int scale) {
        BigDecimal one = BigDecimal.ONE;
        BigDecimal n = BigDecimal.ONE;
        BigDecimal fact = BigDecimal.ONE; // fact = n!
        BigDecimal e = BigDecimal.ONE;
        for (int i = 1; i <= x; i++) {
            e = e.add(one.divide(fact, scale, RoundingMode.FLOOR));
            n = n.add(one);
            fact = fact.multiply(n);
            //System.out.println(e+" "+n+" "+fact+" "+one.divide(fact,scale,RoundingMode.CEILING));
        }
        return e;
    }

}
