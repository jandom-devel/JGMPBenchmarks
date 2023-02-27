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

import it.unich.jgmp.*;

/**
 * Example for JGMP: naive matrix multiplication
 */
public class MatrixMultiplicationExample {

    public static void main(String[] args) {
        int n = 100;
        int m = 200;
        int p = 300;
        MPZ one = new MPZ(1);
        MPZ seed = new MPZ(0);
        MPZ[][] a = new MPZ[n][m];
        MPZ[][] b = new MPZ[m][p];
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                a[i][j] = seed;
                seed.addAssign(one);
            }
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < p; j++) {
                b[i][j] = seed;
                seed.addAssign(one);
            }
        }
    }

    /**
     * Compute matrix multiplication with a naive algorithm.
     *
     * @param a an n × m matrix
     * @param b an m × p matrix
     * @return an n × p matrix a*b
     */
    public static MPZ[][] matrixMultiplication(MPZ[][] a, MPZ[][] b) {
        if (a.length == 0 || b.length == 0 || a[0].length != b.length) {
            return null;
        }
        int n = a.length;
        int m = a[0].length;
        int p = b[0].length;

        MPZ[][] c = new MPZ[n][p];
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < p; j++) {
                MPZ sum = new MPZ(0);
                for (int k = 1; k < m; k++) {
                    sum.addmulAssign(a[i][k], b[k][j]);
                    // equivalent to sum.addAssign(a[i][k].mul(b[k][j]));
                }
                c[i][j] = sum;
            }
        }
        return c;
    }
}
