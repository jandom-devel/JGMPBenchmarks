/**
 * Copyright 2022, 2023 Francesca Scozzari <francesca.scozzari@unich.it> and Gianluca Amato
 * <gianluca.amato@unich.it>
 *
 * JGMPBenchmarks is a set of benchmarks for JGMP. JGMPBenchmarks is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * JGMPBenchmarks is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of a MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with JGMPBenchmarks. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package it.unich.jgmpbenchmarks;

import java.util.concurrent.TimeUnit;

import org.apache.commons.numbers.fraction.BigFraction;
import org.apfloat.Apint;
import org.apfloat.Aprational;
import org.jscience.mathematics.number.Rational;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import it.unich.jgmp.MPQ;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = { "-Xms2G", "-Xmx2G" })
@Warmup(iterations = 1)
@Measurement(iterations = 1)

public class ContinuedFractionBenchmark {

    @Param({ "1", "10", "100", "1000" })
    public int steps;

    @Benchmark
    public MPQ continuedFractionMPQ() {
        return continuedFractionMPQ(steps);
    }

    @Benchmark
    public Rational continuedFractionRational() {
        return continuedFractionRational(steps);
    }

    @Benchmark
    public BigFraction continuedFractionBigFraction() {
        return continuedFractionBigFraction(steps);
    }

    @Benchmark
    public Aprational continuedFractionAprational() {
        return continuedFractionAprational(steps);
    }
    /**
     * Benchmarks for JGMP using factorial
     *
     * @param args not used.
     */
    public static void main(String[] args) throws RunnerException {
        MPQ valMPQ = continuedFractionMPQ(3);
        if (!valMPQ.equals(new MPQ(1321, 420)))
            throw new Error("Invalid MPQ result");
        Rational valRational = continuedFractionRational(3);
        if (!valRational.equals(Rational.valueOf(1321, 420)))
            throw new Error("Invalid Rational result");
        BigFraction valBigFraction = continuedFractionBigFraction(3);
        if (!valBigFraction.equals(BigFraction.of(1321, 420)))
            throw new Error("Invalid BigFraction result");
        Aprational valAprational = continuedFractionAprational(3);
            if (!valAprational.equals(new Aprational(new Apint(1321), new Apint(420))))
                throw new Error("Invalid Aprational result");

        Options opt = new OptionsBuilder().include(ContinuedFractionBenchmark.class.getSimpleName()).build();
        new Runner(opt).run();
    }

    public static MPQ continuedFractionMPQ(int steps) {
        MPQ value = new MPQ();
        MPQ numerator = new MPQ();
        MPQ six = new MPQ(6);
        while (steps >= 1) {
            value.addAssign(six);
            numerator.set(2 * steps - 1);
            numerator.mulAssign(numerator);
            value.divAssign(numerator, value);
            steps -= 1;
        }
        value.addAssign(new MPQ(3));
        return value;
    }

    public static Rational continuedFractionRational(int steps) {
        Rational value = Rational.ZERO;
        Rational six = Rational.valueOf(6, 1);
        while (steps >= 1) {
            value = value.plus(six);
            Rational numerator = Rational.valueOf(2 * steps - 1, 1);
            numerator = numerator.times(numerator);
            value = numerator.divide(value);
            steps -= 1;
        }
        value = value.plus(Rational.valueOf(3, 1));
        return value;
    }

    public static BigFraction continuedFractionBigFraction(int steps) {
        BigFraction value = BigFraction.ZERO;
        while (steps >= 1) {
            value = value.add(6);
            BigFraction numerator = BigFraction.of(2 * steps - 1);
            numerator = numerator.multiply(2 * steps - 1);
            value = numerator.divide(value);
            steps -= 1;
        }
        value = value.add(3);
        return value;
    }

    public static Aprational continuedFractionAprational(int steps) {
        Aprational value = Aprational.ZERO;
        Aprational six = new Apint(6);
        while (steps >= 1) {
            value = value.add(six);
            Aprational numerator = new Aprational(2 * steps - 1);
            numerator = numerator.multiply(numerator);
            value = numerator.divide(value);
            steps -= 1;
        }
        value = value.add(new Apint(3));
        return value;
    }

}
