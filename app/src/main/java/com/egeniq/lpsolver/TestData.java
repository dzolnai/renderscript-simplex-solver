package com.egeniq.lpsolver;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class which provides the test data.
 * Created by Daniel Zolnai on 2017-01-07.
 */
@SuppressWarnings({ "unused", "MismatchedReadAndWriteOfArray" })
public class TestData {

    private static final double[] dm = { 859f, 937f, 954f, 941f, 995f, 913f, 872f, 872f, 730f, 897f, 882f, 888f, 876f, 921f, 868f, 865f, 985f, 970f, 950f, 950f, 950f, 950f };
    private static final double[] moisture = { 131f, 63f, 46f, 59f, 5f, 87f, 128f, 128f, 270f, 103f, 118f, 112f, 124f, 79f, 132f, 135f, 15f, 30f, 50f, 50f, 50f, 50f };
    private static final double[] ash = { 21f, 17f, 462f, 57f, 0, 158f, 12f, 22f, 96f, 70f, 15f, 64f, 62.2, 61.7, 15f, 50f, 0, 0, 950f, 950f, 950f, 950f };
    private static final double[] cp = { 104f, 927f, 402f, 349f, 0f, 657f, 82f, 94f, 42f, 135f, 94f, 435f, 430.1, 286.4, 111f, 153f, 942f, 584f, 0, 0, 0, 0 };
    private static final double[] cfat = { 17f, 7f, 52f, 70f, 995f, 107f, 38f, 61f, 0, 141f, 29f, 81f, 21.9, 99.5, 13f, 34f, 0, 0, 0, 0, 0, 0 };
    private static final double[] cfibre = { 46f, 0, 0, 177f, 0, 0, 22f, 51f, 0, 53f, 22f, 64f, 61.3, 246.8, 24f, 86f, 0, 0, 0, 0, 0, 0 };
    private static final double[] staew = { 509f, 0, 0, 34f, 0, 0, 624f, 456f, 0, 329f, 631f, 8f, 8f, 30.4, 587f, 201f, 0, 0, 0, 0, 0, 0 };
    private static final double[] ca = { 0.6, 0.5, 178.7, 2.3, 0, 35.9, 0.2, 1.4, 6.9, 4.6, 0.3, 3f, 2.7, 2.9, 0.4, 1f, 0, 0, 240f, 365f, 350f, 0 };
    private static final double[] p = { 3.5, 1.7, 82.8, 10.3, 0, 24.0, 2.7, 4.8, 0.7, 13.6, 2.9, 6.5, 6.5, 7.6, 3.1, 10.6, 0, 0, 182f, 0, 0, 0 };
    private static final double[] ip = { 2.3, 0, 0, 7.7, 0, 0, 1.8, 3.6, 0.1, 12.2, 2f, 4.2, 4.2, 6.1, 2f, 9f, 0, 0, 0, 0, 0, 0 };
    private static final double[] k = { 4.9, 2.8, 2.2, 14.5, 0, 9.9, 3.4, 4.9, 38.3, 9.9, 3.6, 21.9, 21.9, 13f, 4.2, 13.3, 0, 0, 0, 0, 0, 0 };
    private static final double[] na = { 0.1, 5.9, 6.4, 0, 0, 10.7, 0, 0.2, 1.6, 0.1, 0.1, 0.3, 0.1, 0.2, 0.1, 0.2, 0, 0, 0, 0, 0, 380f };
    private static final double[] cl = { 1.0, 3.5, 10.1, 0.3, 0, 17.0, 0.6, 1.0, 17.4, 0.5, 1.5, 0.3, 0.4, 1.2, 0.5, 0.6, 0, 0, 0, 0, 0, 570f };
    private static final double[] oeslk = { 2546f, 0, 1937f, 0, 8372f, 3268f, 3205f, 2736f, 2040f, 2846f, 3130f, 2450f, 2039f, 1891f, 2856f, 3130f, 2450f, 2039f, 1891f, 2856f, 1473f, 3505f, 4637f, 0, 0, 0, 0 };
    private static final double[] oelh = { 2806f, 3149f, 1993f, 1862f, 9770f, 3408f, 3339f, 2793f, 1927f, 3267f, 3245f, 2600f, 2087f, 1979f, 3097f, 1876f, 3505f, 4637f, 0, 0, 0, 0 };
    private static final double[] opp = { 1.3, 1.4, 51.3, 3.1, 0, 17.8, 0.8, 1.7, 0.3, 2.2, 0.9, 2.7, 2.7, 2.1, 1.2, 2.9, 0, 0, 142f, 0, 0, 0 };
    private static final double[] lys = { 3.7, 82.5, 17.7, 14.3, 17.7, 49.9, 2.3, 3.0, 0.2, 5.6, 2.2, 26.9, 26.7, 10f, 3.1, 6.1, 788f, 0, 0, 0, 0, 0 };
    private static final double[] met = { 1.8, 11.1, 4.8, 5.6, 4.8, 18.4, 1.7, 1.9, 0.2, 2.8, 1.6, 6f, 6f, 6.3, 1.8, 2.5, 0, 995f, 0, 0, 0, 0 };
    private static final double[] cys = { 2.3, 11.1, 2.8, 5.9, 2.8, 5.9, 1.8, 2f, 0.3, 3f, 1.8, 6.5, 6.5, 4.9, 2.4, 3.2, 0, 0, 0, 0, 0, 0 };
    private static final double[] m_plus_c = { 4f, 22.2, 7.6, 11.5, 7.6, 24.3, 3.5, 4f, 0.5, 5.8, 3.4, 12.6, 12.5, 11.1, 4.2, 5.7, 0, 995f, 0, 0, 0, 0, 0 };
    private static final double[] thr = { 3.5, 40.7, 11.7, 11.1, 11.7, 27.5, 3.0, 3.4, 0.4, 5.0, 3.1, 17.0, 16.7, 10.6, 3.2, 5f, 0, 0, 0, 0, 0, 0 };
    private static final double[] trp = { 1.3, 13.9, 1.2, 4.2, 1.2, 7.2, 0.5, 0.7, 0.1, 1.4, 1.0, 5.7, 5.5, 3.4, 1.3, 2.1, 0, 0, 0, 0, 0, 0 };
    private static final double[] ile = { 3.6, 11.1, 10f, 10.8, 10f, 27.5, 2.8, 3.3, 0.4, 5.0, 3.7, 20.0, 19.7, 11.7, 3.8, 4.9, 0, 0, 0, 0, 0, 0 };
    private static final double[] val = { 5.1, 79.7, 16.1, 15.3, 16.1, 32.1, 3.9, 4.8, 0.8, 7.4, 4.7, 20.8, 20.6, 14.0, 4.7, 7.2, 0, 0, 0, 0, 0, 0 };
    private static final double[] dlysp = { 2.4, 66.0, 13.6, 8.4, 0, 45.4, 1.4, 1.9, -0.3, 4f, 1.2, 23.2, 22.9, 7.8, 2.6, 4.4, 788f, 0, 0, 0, 0, 0 };
    private static final double[] dmetp = { 1.3, 8.9, 3.9, 3.8, 0, 17.1, 1.5, 1.6, 0, 1.9, 1.1, 5.1, 5.0, 5.4, 1.6, 1.8, 0, 995f, 0, 0, 0, 0 };
    private static final double[] dcysp = { 1.6, 8.9, 1.6, 4.0, 0, 5.2, 1.4, 1.5, 0, 1.9, 1.1, 5.2, 5.1, 3.8, 2.0, 2.5, 0, 0, 0, 0, 0, 0 };
    private static final double[] dm_plus_cp = { 2.9, 17.8, 5.5, 7.8, 0.0, 22.4, 2.9, 3.1, 0.0, 3.9, 2.3, 10.2, 10.2, 9.2, 3.6, 4.2, 0, 995f, 0, 0, 0, 0 };
    private static final double[] dthrp = { 2.4, 32.6, 9.7, 7.7, 0.0, 23.1, 2.2, 2.4, -0.2, 3.1, 1.4, 14.1, 13.9, 8.6, 2.5, 3.4, 0, 0, 0, 0, 0, 0 };
    private static final double[] dtrpp = { 0.9, 11.1, 0.9, 3.1, 0, 6.4, 0.5, 0.5, 0, 1.1f, 0.7, 4.8, 4.8, 2.8, 1.1, 1.6, 0, 0, 0, 0, 0, 0 };
    private static final double[] dilep = { 2.7, 8.9, 8f, 7.8, 0, 23.7, 2.3, 2.6, 0, 3.5, 2.7, 17.2, 17f, 10.3, 3.2, 3.6, 0, 0, 0, 0, 0, 0 };
    private static final double[] dargp = { 4.0, 31.9, 19.5, 31.4, 0, 35.6, 3.4, 4.0, -0.3, 8.6, 2.5, 28.7, 28.4, 21.1, 4.5, 8.7, 0, 0, 0, 0, 0, 0 };
    private static final double[] dvalp = { 3.8, 63.8, 13f, 11.3, 0, 28f, 3.1, 3.8, 0.2, 5f, 3.2, 17.8, 17.6, 12.1, 3.9, 5.3, 0, 0, 0, 0, 0, 0 };
    private static final double[] dneaa = { 46.71, 338.92, 179.13, 132.4, 0.0, 271.5, 38.3, 42.4, 7.6, 45.7, 39.4, 201.0, 198.7, 127.1, 59.3, 62.9, 0, 0, 0, 0, 0, 0 };
    private static final double[] c18_2 = { 6.7, 0, 7f, 26.8, 2.9, 0.9, 18.8, 26.8, 0, 41.7, 11.7, 32.8, 7.7, 48.5, 5.2, 13.6, 0, 0, 0, 0, 0, 0 };
    private static final double[] c20 = { 0, 0, 3, 0.5, 1.2, 38.5, 0.3, 0.5, 0.0, 2.3, 0.3, 0.2, 0.1, 0.2, 0.1, 0.2, 0, 0, 0, 0, 0, 0 };

    private static final String[] ingredients = {
            "Barley", "Blood meal", "Bone meal", "Cotton seed expeller/cake partially with husks", "Fats/oils vegetable", "Fish meal CP630-680",
            "Maize", "Maize feed meal -> Maize bran", "Molasses, Cane, SUG>475", "Rice feed meal, ASH<90", "Sorghum", "Soybean expeller/cake",
            "Soybean meal CF45-70 CP<450", "Sunflower seed expeller/cake partially dehulled", "Wheat", "Wheat middlings -> Wheat bran", "Lysine-HCl (78%)",
            "DL-Methionine (99.5%)", "DiCalciumphosphate,2H2O", "Limestone", "Shells", "Salt" };

    public static String[] getIngredientNames() {
        return ingredients;
    }

    public static LinearObjectiveFunction getObjectiveFunction() {
        // Linear objective function: the problem to optimize on.
        // In this case the price of each ingredient.
        return new LinearObjectiveFunction(new double[] {
                47d, 100d, 36d, 55d, 80d, 120d, 22d, 22d, 27d, 17d, 22d, 90d, 97d, 70d,
                22d, 17d, 540d, 950d, 120d, 7d, 13d, 16d }, 0); // last parameter is d, the offset. Is 0 in this case.
    }

    public static List<LinearConstraint> getLinearConstraints() {
        // Not all of these are used.

        // Constraints we should be taking care of
        List<LinearConstraint> constraints = new ArrayList<>();
        // --- Metabolic Energy [MIN] ---
        constraints.add(new LinearConstraint(_clone(oelh), Relationship.GEQ, 2870));
        // --- Metabolic Energy [MAX] ---
        constraints.add(new LinearConstraint(_clone(oelh), Relationship.LEQ, 3200));
        // --- Crude protein [MIN] ---
        constraints.add(new LinearConstraint(_clone(cp), Relationship.GEQ, 160));
        // --- Crude protein [MAX] ---
        constraints.add(new LinearConstraint(_clone(cp), Relationship.LEQ, 220));
        // --- Crude fibre [MAX] ---
        constraints.add(new LinearConstraint(_clone(cfibre), Relationship.LEQ, 43));
        // --- Starch [MIN] ---
        constraints.add(new LinearConstraint(_clone(staew), Relationship.GEQ, 382));
        // --- Calcium [MIN] ---
        constraints.add(new LinearConstraint(_clone(ca), Relationship.GEQ, 42.3));
        // --- Calcium [MAX] ---
        constraints.add(new LinearConstraint(_clone(ca), Relationship.LEQ, 49));
        // --- Absorbable phosphorus [MIN] ---
        constraints.add(new LinearConstraint(_clone(opp), Relationship.GEQ, 3.14));
        // --- Sodium [MIN] ---
        constraints.add(new LinearConstraint(_clone(na), Relationship.GEQ, 1.23));
        // --- Sodium [MAX] ---
        constraints.add(new LinearConstraint(_clone(na), Relationship.LEQ, 2.12));
        // --- Digestible lysine [MIN] ---
        constraints.add(new LinearConstraint(_clone(dlysp), Relationship.GEQ, 7.6));
        // --- Digestible lysine [MAX] ---
        constraints.add(new LinearConstraint(_clone(dlysp), Relationship.LEQ, 8.44));
        // --- Digestible methionine [MIN] ---
        constraints.add(new LinearConstraint(_clone(dmetp), Relationship.GEQ, 3.62));
        // --- Digestible meth+cystine [MIN] ---
        constraints.add(new LinearConstraint(_clone(dm_plus_cp), Relationship.GEQ, 5.99));
        // --- Digestible meth+cystine [MAX] ---
        constraints.add(new LinearConstraint(_clone(dm_plus_cp), Relationship.LEQ, 7.1));
        // --- Digestible Threonine [MIN] ---
        constraints.add(new LinearConstraint(_clone(dthrp), Relationship.GEQ, 4.52));
        // --- Digestible Tryptophan [MIN] ---
        constraints.add(new LinearConstraint(_clone(dtrpp), Relationship.GEQ, 1.33));
        // --- Digestible Valine [MIN] ---
        constraints.add(new LinearConstraint(_clone(dvalp), Relationship.GEQ, 6.37));
        // --- Digestible Arginine [MIN] ---
        constraints.add(new LinearConstraint(_clone(dargp), Relationship.GEQ, 7.16));
        // --- Sum Digestible non essential AA [MIN] ---
        constraints.add(new LinearConstraint(_clone(dneaa), Relationship.GEQ, 73.6));


        double[] maxConstraints = {
                0.1818, // Barley
                0.03636, // Blood meal
                0.04848, // Bone meal
                0.1212, // Cotton seed
                0.0606, // Fats oils
                0.0606, // Fish meal
                0, // Maize
                0.1818, // Maize feed meal
                0.02424, // Molasses
                0.09696, // Rice feed meal
                0, // Sorghum
                0.303, // Soybean expeller
                0.303, // Soybean meal
                0.1212, // Sunflower seed
                0.303, // Wheat
                0.08484, // Wheat bran
                0, // Lysine
                0, // DL-methionine
                0, // DiCalciumPhosphate
                0.03636, // Limestone
                0.10908, // Shells
                0.004242, // Salt
        };


        // Max amount constraints (see var above)
        for (int i = 0; i < 22; ++i) {
            if (Math.abs(maxConstraints[i]) < 0.00001) {
                continue;
            }
            double[] maxConstraint = new double[22];
            maxConstraint[i] = 1; // rest is 0
            constraints.add(new LinearConstraint(maxConstraint, Relationship.LEQ, maxConstraints[i])); // Should be less then the max
        }
        // Cotton seed + Sunflower exp [MAX]
        constraints.add(new LinearConstraint(new double[] { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, Relationship.LEQ, 0.16968)); // Should be less then the max

        // Min amounts
        // Maize [MIN]
        constraints.add(new LinearConstraint(new double[] { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, Relationship.GEQ, 0.35));
        // Shells [MIN]
        constraints.add(new LinearConstraint(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, Relationship.GEQ, 0.05));
        // Salt [MIN]
        constraints.add(new LinearConstraint(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, Relationship.GEQ, 0.002));

        // Most important one: it should add up to 1 kg (we allow some freedom here because this is a really strong constraint)
        constraints.add(new LinearConstraint(new double[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, Relationship.GEQ, 0.9999));
        constraints.add(new LinearConstraint(new double[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, Relationship.LEQ, 1.0001));
        return constraints;
    }

    /**
     * Utility function to clone an array.
     *
     * @param array The array to clone.
     * @return The clone of the input array.
     */
    private static double[] _clone(double[] array) {
        return Arrays.copyOf(array, array.length);
    }
}
