package com.egeniq.lpsolver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v8.renderscript.RenderScript;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.egeniq.lpsolver.renderscript.SimplexRS;
import com.egeniq.lpsolver.renderscript.TableauConverter;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * The main activity, which is opened when the app starts.
 * The optimization process is done on the UI thread for both options, which is not nice,
 * but for the sake of the brevity of the code, and for this being a demonstrational app, it can be overlooked.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.setLogTextView((TextView)findViewById(R.id.log));
        findViewById(R.id.solve_apache_math).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solveApacheMath();
            }
        });
        findViewById(R.id.solve_renderscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solveRenderScript();
            }
        });
    }

    /**
     * Solves the optimization problem using the Apache Math package.
     */
    public void solveApacheMath() {
        Log.clear();
        Log.logWithTimeStamp("Started looking for optimal solution with Apache Commons Math...");
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution;
        long startTime = System.currentTimeMillis();
        solution = solver.optimize(new MaxIter(100), TestData.getObjectiveFunction(), new LinearConstraintSet(TestData.getLinearConstraints()), GoalType.MINIMIZE, new NonNegativeConstraint(true));
        long endTime = System.currentTimeMillis();
        // Very simple way to show how much time it required to finish.
        Toast.makeText(this, String.valueOf(endTime - startTime) + "ms", Toast.LENGTH_LONG).show();
        Log.logWithTimeStamp("Best price is: " + solution.getValue());
        Log.log("-----------------------");
        Log.log("Amounts to buy: ");
        double[] results = solution.getPointRef();
        double sum = 0;
        NumberFormat format = new DecimalFormat("#.##");
        String[] ingredients = TestData.getIngredientNames();
        for (int i = 0; i < ingredients.length; ++i) {
            sum += results[i] * 1000;
            Log.log(ingredients[i] + ": " + format.format(results[i] * 1000) + "gr");
        }
        Log.log("-----------------------");
        Log.log("Total grams: " + format.format(sum) + "gr");
    }

    /**
     * Solves the optimization problem using our own RenderScript code.
     */
    public void solveRenderScript() {
        Log.clear();
        Log.logWithTimeStamp("Started looking for optimal solution with RenderScript...");
        float[] solution;
        RenderScript renderScript = RenderScript.create(this);
        SimplexRS simplexRS = new SimplexRS(renderScript);
        long startTime = System.currentTimeMillis();
        LinearObjectiveFunction objectiveFunction = TestData.getObjectiveFunction();
        solution = simplexRS.solve(TableauConverter.convertMinimize(objectiveFunction, TestData.getLinearConstraints()), true);
        long endTime = System.currentTimeMillis();
        // Very simple way to show how much time it required to finish.
        Toast.makeText(this, String.valueOf(endTime - startTime) + "ms", Toast.LENGTH_LONG).show();
        if (solution == null) {
            Log.logWithTimeStamp("No solution found");
            return;
        }
        float bestPrice = 0;
        for (int i = 0; i < solution.length; ++i) {
            bestPrice += solution[i] * objectiveFunction.getCoefficients().getEntry(i);
        }
        Log.logWithTimeStamp("Best price is: " + bestPrice);
        Log.log("-----------------------");
        Log.log("Amounts to buy: ");
        double sum = 0;
        NumberFormat format = new DecimalFormat("#.##");
        String[] ingredients = TestData.getIngredientNames();
        for (int i = 0; i < ingredients.length; ++i) {
            sum += solution[i] * 1000;
            Log.log(ingredients[i] + ": " + format.format(solution[i] * 1000) + "gr");
        }
        Log.log("-----------------------");
        Log.log("Total grams: " + format.format(sum) + "gr");
    }

}
