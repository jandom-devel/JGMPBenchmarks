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
package it.unich.jgmpbenchmarks.examples;

import it.unich.jgmp.MPZ;

/**
 * Example for JGMP: computing factorial.
 */
public class FactorialExample {

	public static void main(String[] args) {
		int x = 100000;
		MPZ factorial = factorialMPZ(x);
		MPZ immutableFactorial = immutableFactorialMPZ(x);
		System.out.print(factorial.equals(immutableFactorial));
	}

	/**
	 * Compute the factorial of x.
	 */
	public static MPZ factorialMPZ(int x) {
		MPZ f = new MPZ(1);
		while (x >= 1) {
			f.mulAssign(x);
			x -= 1;
		}
		return f;
	}

	/**
	 * Compute the factorial of x using the immutable API.
	 */
	public static MPZ immutableFactorialMPZ(int x) {
		MPZ f = new MPZ(1);
		while (x >= 1) {
			f = f.mul(x);
			x -= 1;
		}
		return f;
	}
}
