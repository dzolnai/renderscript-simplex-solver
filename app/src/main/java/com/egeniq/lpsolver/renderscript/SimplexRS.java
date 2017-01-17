package com.egeniq.lpsolver.renderscript;


import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Type;

import java.util.Arrays;

/**
 * Handler which takes care of setting up the custom RenderScript.
 * Created by Daniel Zolnai on 2017-01-05.
 */
public class SimplexRS {

    private ScriptC_simplex _script;
    private RenderScript _renderScript;

    public SimplexRS(RenderScript renderScript) {
        _renderScript = renderScript;
        _script = new ScriptC_simplex(renderScript);
    }

    /**
     * Solves the linear programming problem.
     *
     * @param data     The input data. Use the {@link TableauConverter} to generate this from Apache Math objects.
     * @param minimize If the program is a minimization problem.
     * @return Null if there was no solution found. Otherwise the (sub)optimal solution.
     */
    public float[] solve(float[][] data, boolean minimize) {
        int arraySize = _script.get_MAX_COLS() * _script.get_MAX_ROWS();
        // Supply the input tableau to the script.
        ScriptField_Tableau tableau = new ScriptField_Tableau(_renderScript, 1);
        int rowCount = data.length;
        int columnCount = data[0].length;
        tableau.set_rows(0, rowCount, false);
        tableau.set_columns(0, columnCount, false);
        tableau.set_dual_program(0, minimize, false);
        tableau.set_matrix(0, reduceToOneDimensional(data, arraySize), false);
        // Copy all values at once to struct in the C code.
        tableau.copyAll();
        _script.bind_tableau(tableau);
        // The following allocations will contain the solution and the size of it.
        Type solutionVectorType = new Type.Builder(_renderScript, Element.F32(_renderScript)).setX(_script.get_MAX_COLS()).create();
        Type resultSizeType = new Type.Builder(_renderScript, Element.I32(_renderScript)).create();
        Allocation arrayAllocation = Allocation.createTyped(_renderScript, solutionVectorType);
        Allocation resultSizeAllocation = Allocation.createTyped(_renderScript, resultSizeType);
        _script.set_solution_vector(arrayAllocation);
        _script.set_result_size(resultSizeAllocation);
        _script.invoke_solve();
        float[] solutionVector = new float[_script.get_MAX_COLS()];
        arrayAllocation.copyTo(solutionVector);
        int[] resultSizeVector = new int[1];
        resultSizeAllocation.copyTo(resultSizeVector);
        // If the solution vector size is -1, no solution has been found.
        if (resultSizeVector[0] <= 0) {
            // No result
            return null;
        }
        // Only the first N elements contain the result.
        return Arrays.copyOf(solutionVector, resultSizeVector[0]);
    }


    /**
     * Utility method to convert a two-dimensional array (matrix) to a one-dimensional (vector).
     * This is required because RenderScript does not support two-dimensional arrays.
     *
     * @param input     The input matrix.
     * @param arraySize The size of the final array (at least the product of the rows and columns of the input).
     * @return The rows of the matrix concatenated into a single vector.
     */
    private float[] reduceToOneDimensional(float[][] input, int arraySize) {
        int rows = input.length;
        int columns = input[0].length;
        float[] result = new float[arraySize];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                result[i * columns + j] = (float)input[i][j];
            }
        }
        return result;
    }
}