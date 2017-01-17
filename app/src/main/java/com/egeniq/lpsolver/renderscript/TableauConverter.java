package com.egeniq.lpsolver.renderscript;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;

import java.util.List;

/**
 * Converts the Apache objects to a Simplex Tableau.
 * Created by Daniel Zolnai on 2017-01-05.
 */
public class TableauConverter {

    /**
     * Use this to convert your Apache Math objective function and constraints to a matrix,
     * which can be used with the RenderScript setup for optimizing a maximization linear programming problem.
     *
     * @param objectiveFunction The objective function which's value has to be maximized.
     * @param linearConstraints The constraints, which define the area of possible solutions.
     * @return The matrix, which can be used with @link{{@link SimplexRS)}}.
     */
    public static float[][] convertMaximize(LinearObjectiveFunction objectiveFunction, List<LinearConstraint> linearConstraints) {
        // Last row contains the negated objective function.
        int rows = linearConstraints.size() + 1;
        int columns = linearConstraints.get(0).getCoefficients().getDimension() + 1;
        float[][] result = new float[rows][columns];
        for (int j = 0; j < objectiveFunction.getCoefficients().getDimension(); j++) {
            result[rows - 1][j] = (float)-objectiveFunction.getCoefficients().getEntry(j);
        }
        for (int i = 0; i < linearConstraints.size(); i++) {
            LinearConstraint constraint = linearConstraints.get(i);
            // Last column has the 'b' vector.
            result[i][columns - 1] = (float)constraint.getValue();
            for (int j = 0; j < constraint.getCoefficients().getDimension(); j++) {
                result[i][j] = (float)constraint.getCoefficients().getEntry(j);
            }
        }
        return result;
    }

    /**
     * Use this to convert your Apache Math objective function and constraints to a matrix,
     * which can be used with the RenderScript setup for optimizing a minimization linear programming problem.
     *
     * @param objectiveFunction The objective function which's value has to be minimized.
     * @param linearConstraints The constraints, which define the area of possible solutions.
     * @return The matrix, which can be used with @link{{@link SimplexRS)}}. Make sure the second parameter of the
     * solve function is set to true, so you get the correct results.
     */
    public static float[][] convertMinimize(LinearObjectiveFunction objectiveFunction, List<LinearConstraint> linearConstraints) {
        // In this case, we don't want to maximize, but minimize, so we do not negate the objective function.
        int rows = linearConstraints.get(0).getCoefficients().getDimension() + 1;
        int columns = linearConstraints.size() + 1;
        float[][] result = new float[rows][columns];
        // Last row contains the negated 'b' vector.
        for (int i = 0; i < linearConstraints.size(); i++) {
            LinearConstraint constraint = linearConstraints.get(i);
            boolean negate = constraint.getRelationship() == Relationship.GEQ;
            result[rows - 1][i] = (float)constraint.getValue();
            if (negate) {
                result[rows - 1][i] *= -1;
            }
        }
        // Last column contains the non-negated objective function.
        for (int i = 0; i < objectiveFunction.getCoefficients().getDimension(); i++) {
            result[i][columns - 1] = (float)objectiveFunction.getCoefficients().getEntry(i);
        }
        // The rest is the constraints, but transposed (notice we changed the two indices in the end.
        for (int i = 0; i < linearConstraints.size(); i++) {
            LinearConstraint constraint = linearConstraints.get(i);
            boolean negate = constraint.getRelationship() == Relationship.GEQ;
            for (int j = 0; j < constraint.getCoefficients().getDimension(); j++) {
                result[j][i] = (float)-constraint.getCoefficients().getEntry(j);
                if (negate) {
                    result[j][i] *= -1;
                }
            }
        }
        return result;
    }
}
