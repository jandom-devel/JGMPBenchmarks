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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatContext;
import org.jscience.mathematics.number.FloatingPoint;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.kframework.mpfr.BigFloat;
import org.kframework.mpfr.BinaryMathContext;

import it.unich.jgmp.MPF;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 5, jvmArgs = { "-Xms2G", "-Xmx2G" })
@Warmup(iterations = 5)
@Measurement(iterations = 5)

/**
 * Benchmarks for JGMP: using continued fractions for approximating pi with
 * floats.
 */
public class ContinuedFractionFloatBenchmark {

    final static double PRECISION_CONVERSION = 1 / Math.log10(2);

    @Param({ "1", "10", "100", "1000" })
    public int steps;

    @Param({ "1024" })
    public int prec;

    @Benchmark
    public BigDecimal continuedFractionBigDecimal() {
        return continuedFractionBigDecimal(steps, prec);
    }

    @Benchmark
    public MPF continuedFractionMPF() {
        return continuedFractionMPF(steps, prec);
    }

    @Benchmark
    public MPF continuedFractionMPFImmutable() {
        return continuedFractionMPFImmutable(steps, prec);
    }

    @Benchmark
    public FloatingPoint continuedFractionFloatingPoint() {
        return continuedFractionFloatingPoint(steps, prec);
    }

    @Benchmark
    public Apfloat continuedFractionApfloat() {
        return continuedFractionApfloat(steps, prec, 10);
    }

    @Benchmark
    public Apfloat continuedFractionApfloatBinary() {
        return continuedFractionApfloat(steps, prec, 2);
    }

    @Benchmark
    public BigFloat continuedFractionBigFloat() {
        return continuedFractionBigFloat(steps, prec);
    }

    public static void main(String[] args) throws RunnerException {
        String resBigFloat = "3.141592410971980674262588860216726437296e+00";
        if (!continuedFractionBigFloat(100, 128).toString().equals(resBigFloat))
            throw new Error("Invalid BigFloat result");
        String resBigDecimal = "3.14159241097198067426258886021672643729";
        if (!continuedFractionBigDecimal(100, 128).toString().equals(resBigDecimal))
            throw new Error("Invalid BigDecimal result");
        String resMPF = "3.141592410971980674262588860216726437293";
        if (!continuedFractionMPF(100, 128).toString().equals(resMPF))
            throw new Error("Invalid MPF result");
        if (!continuedFractionMPFImmutable(100, 128).toString().equals(resMPF))
            throw new Error("Invalid MPF Immutable result");
        String resFloatingPoint = "0.31415924109719806742625888602167264372E1";
        if (!continuedFractionFloatingPoint(100, 128).toString().equals(resFloatingPoint))
            throw new Error("Invalid FloatingPoint result");
        String resApfloat = "3.14159241097198067426258886021672643729";
        if (!continuedFractionApfloat(100, 128, 10).toString().equals(resApfloat))
            throw new Error("Invalid Apfloat result");
        if (!continuedFractionApfloat(100, 128, 2).toRadix(10).toString().equals(resApfloat))
            throw new Error("Invalid Apfloat binary result");
        Options opt = new OptionsBuilder().include("ContinuedFractionFloatBenchmark").build();
        new Runner(opt).run();
    }

    /* BigDecimal */
    public static BigDecimal continuedFractionBigDecimal(int steps, int prec) {
        MathContext mc = new MathContext((int) (prec / PRECISION_CONVERSION));
        BigDecimal value = BigDecimal.ZERO;
        BigDecimal six = BigDecimal.valueOf(6);
        while (steps >= 1) {
            value = value.add(six);
            BigDecimal numerator = BigDecimal.valueOf(2 * steps - 1);
            numerator = numerator.multiply(numerator);
            value = numerator.divide(value, mc);
            steps -= 1;
        }
        value = value.add(BigDecimal.valueOf(3));
        return value;
    }

    /* JGMP */
    public static MPF continuedFractionMPF(int steps, int prec) {
        MPF value = MPF.init2(prec);
        MPF numerator = new MPF();
        MPF six = new MPF(6);
        while (steps >= 1) {
            value.addAssign(six);
            numerator.set(2 * steps - 1);
            numerator.mulAssign(numerator);
            value.divAssign(numerator, value);
            steps -= 1;
        }
        value.addAssign(new MPF(3));
        return value;
    }

    /* JGMP */
    public static MPF continuedFractionMPFImmutable(int steps, int prec) {
        MPF.setDefaultPrec(prec);
        MPF value = new MPF();
        MPF six = new MPF(6);
        while (steps >= 1) {
            value.addAssign(six);
            MPF numerator = new MPF(2 * steps - 1);
            numerator = numerator.mul(numerator);
            value = numerator.div(value);
            steps -= 1;
        }
        return value.add(new MPF(3));
    }

    /* JScience */
    public static FloatingPoint continuedFractionFloatingPoint(int steps, int prec) {
        FloatingPoint.setDigits((int) (prec / PRECISION_CONVERSION));
        FloatingPoint value = FloatingPoint.ZERO;
        FloatingPoint six = FloatingPoint.valueOf(6);
        while (steps >= 1) {
            value = value.plus(six);
            FloatingPoint numerator = FloatingPoint.valueOf(2 * steps - 1);
            numerator = numerator.times(numerator);
            value = numerator.divide(value);
            steps -= 1;
        }
        value = value.plus(FloatingPoint.valueOf(3));
        return value;
    }

    /* Apfloat */
    public static Apfloat continuedFractionApfloat(int steps, int prec, int radix) {
        ApfloatContext.getContext().setDefaultRadix(radix);
        int precision = radix == 10 ? (int) (prec / PRECISION_CONVERSION) : prec;
        Apfloat value = new Apfloat(0);
        Apfloat six = new Apfloat(6);
        while (steps >= 1) {
            value = value.add(six);
            Apfloat numerator = new Apfloat(2 * steps - 1, precision);
            numerator = numerator.multiply(numerator);
            value = numerator.divide(value);
            steps -= 1;
        }
        value = value.add(new Apfloat(3));
        return value;
    }

    public static BigFloat continuedFractionBigFloat(int steps, int prec) {
        BigFloat value = BigFloat.zero(prec);
        BinaryMathContext c = new BinaryMathContext(prec, RoundingMode.HALF_EVEN);
        BigFloat six = new BigFloat(6, c);
        while (steps >= 1) {
            value = value.add(six, c);
            BigFloat numerator = new BigFloat(2 * steps - 1, c);
            numerator = numerator.multiply(numerator, c);
            value = numerator.divide(value, c);
            steps -= 1;
        }
        value = value.add(new BigFloat(3, c), c);
        return value;
    }
}
