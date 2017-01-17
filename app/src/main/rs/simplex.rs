#pragma version(1)
#pragma rs java_package_name(com.egeniq.lpsolver.renderscript)
#pragma rs_fp_full

#include "simplex.rsh"

/********************************************************
 * Equality function to compare two floats.
 ********************************************************
 * a: The first item to compare.
 * b: The second item to compare.
 ********************************************************
 * Returns: True if the difference between the two items is
 *          less than a given epsilon constant.
 */
static bool equal(float a, float b) {
    return fabs(a-b) < EPSILON;
}

/********************************************************
 * Convenience function to get an element of the tableau.
 ********************************************************
 * tableau: The tableau to get a specific element of.
 * row: The row the element is located in.
 * column: The column the element is located in.
 ********************************************************
 * Returns: The element at the specific column and row.
 */
static float get_element(Tableau_t *tableau, int row, int column) {
    int index = row * tableau->columns + column;
    return tableau->matrix[index];
}

/********************************************************
 * Convenience function to get an element of the tableau.
 ********************************************************
 * tableau: The tableau to get a specific element of.
 * row: The row the element is located in.
 * column: The column the element is located in.
 * value: The value to set at the cell specified by the column and row.
 */
static void set_element(Tableau_t *tableau, int row, int column, float value) {
    int index = row * tableau->columns + column;
    tableau->matrix[index] = value;
}


/********************************************************
 * We extend the 'A' matrix with the entity matrix (NxN),
 * by adding extra columns to the right.
 * This will help us execute the optimization.
 ********************************************************
 * tableau: The tableau to check on.
 ********************************************************
 * Returns: True, if all elements meet the requirement.
 *          Otherwise false, and the program should abort.
 */
static void add_slack_variables(Tableau_t *tableau) {
    // Here we are resizing the table, so we need to re-layout the array.
    int old_column_count = tableau->columns;
    int new_column_count = tableau->columns + tableau->rows - 1;
    // This won't destroy the data while copying, because we start from the back
    int previous_index = -1;
    for (int i = tableau->rows - 1; i >= 0; i--) {
        for (int j = tableau->columns - 1; j >= 0; j--) {
            int old_index = i * tableau->columns + j;
            float value = tableau->matrix[old_index];
            int new_index = i * new_column_count + j;
            tableau->matrix[new_index] = value;
            // Remove all the rubbish inbetween, if there's some.
            if (previous_index > new_index + 1) {
                for(int k = new_index + 1; k < previous_index; k++) {
                    tableau->matrix[k] = 0;
                }
            }
            previous_index = new_index;
        }
    }
    // Do the resize, now that data has been moved.
    tableau->columns += tableau->rows - 1;
    // We will add an entity matrix after the 'A' matrix,
    // but before the 'b' vector.
    // So we need to copy the 'b' vector to the most-right.
    for (int i = 0; i < tableau->rows - 1; i++) {
        float bi = get_element(tableau, i, old_column_count - 1);
        set_element(tableau, i, tableau->columns - 1, bi);
    }
    for (int i = 0; i < tableau->rows - 1; i++) {
        for (int j = 0; j < tableau->rows - 1; j++) {
            // Each row will contain exactly one '1' number.
            // Other elements in the row are 0.
            set_element(tableau, i, j + old_column_count - 1, i == j);
        }
    }
}

/********************************************************
 * Finds the entering column, which is the most negative
 * column in the matrix.
 ********************************************************
 * tableau: The tableau to find the pivot column in.
 ********************************************************
 * Returns: The column index of the entering column.
 */
static int find_entering_column(Tableau_t *tab) {
    int entering_column = 0;
    // Get the 'c' value for this column
    float lowest = get_element(tableau, tableau->rows - 1, entering_column);
    for(int j = 0; j < tab->columns - 1; j++) {
         // Check if we can find a lower 'c' cell than before.
        if (get_element(tableau, tableau->rows - 1, j) < lowest) {
            lowest = get_element(tableau, tableau->rows - 1, j);
            entering_column = j;
        }
    }
    LOG("Most negative column in 'c' is col: ", entering_column);
    LOG("With the value: ", lowest);
    if (lowest >= -CUT_OFF) {
        // All elements of 'c' are positive, we have achieved the optimal solution.
        return -1;
    }
    return entering_column;
}

/********************************************************
 * Finds the departing row, which is the one with the smallest
 * non-negative ratio: 'b' / pivot cell. Where the pivot cells are
 * all cells of the entering column.
 ********************************************************
 * tableau: The tableau to find the departing row in.
 * entering_column: The entering column, which we check the cells of.
 ********************************************************
 * Returns: The row index of the departing row.
 */
static int find_departing_row(Tableau_t *tableau, int entering_column) {
    int departing_row = 0;
    float smallest_ratio = -1;
    LOG("Checking ratios inside entering column: ", entering_column);
    for(int i = 0; i < tableau->rows - 1; i++){
        float ratio = get_element(tableau, i, tableau->columns - 1) / get_element(tableau, i, entering_column);
        if ((ratio > 0 && ratio < smallest_ratio ) || smallest_ratio < 0) {
            smallest_ratio = ratio;
            departing_row = i;
        }
    }
    if (smallest_ratio <= CUT_OFF) {
        // If all ratios are not more than zero,
        // then the solutions do not have a maximum.
        return -1;
    }
    LOG("Found pivot row: ", departing_row);
    LOG("Min ratio: ", smallest_ratio);
    return departing_row;
}

/********************************************************
 * Executes the pivoting function on the tableau.
 ********************************************************
 * tableau: The tableau to execute the pivoting function on.
 * departing_row: The departing row.
 * entering_column: The entering column.
 ********************************************************
 * Returns: True if everything went fine.
 *          False if there was an unexpected problem.
 *          The program should abort in this case.
 */
static bool do_pivoting(Tableau_t *tableau, int departing_row, int entering_column) {
    float pivot = get_element(tableau, departing_row, entering_column);
    if (pivot <= CUT_OFF) {
        LOG("Pivot is not more than 0", pivot);
        return false;
    }
    for(int j = 0; j < tableau->columns; j++) {
        float cell = get_element(tableau, departing_row, j);
        set_element(tableau, departing_row, j, cell / pivot);
    }
    if (!equal(get_element(tableau, departing_row, entering_column), 1.0f)) {
        LOG("Pivot does not equal 1, it's value is: ", get_element(tableau, departing_row, entering_column));
        return false;
    }
    for(int i = 0; i < tableau->rows; i++) {
        float multiplier = get_element(tableau, i, entering_column);
        if (i == departing_row) {
            // We skip the pivot cell.
            continue;
        }
        for(int j = 0; j < tableau->columns; j++) {
            float new_value = get_element(tableau, i, j) - multiplier * get_element(tableau, departing_row, j);
            set_element(tableau, i, j, new_value);
        }
    }
    return true;
}


/********************************************************
 * Given a column of the identity matrix, find the row
 * containing the only 1.
 ********************************************************
 * tableau: The tableau in which to find the identity vector.
 * column: The column to search in.
 ********************************************************
 * Returns: -1, if the column is not an identity vector.
 *          Otherwise the row index of the cell containing
 *          the only '1' number.
 */
static int find_entity_row(Tableau_t *tableau, int column) {
    int entity_row = -1;
    // An identity vector contains exactly one '1', and the
    // rest are zeroes. Inside the given column, we need to
    // find the row which has this '1' number.
    for(int i = 0; i < tableau->rows - 1; i++) {
        if (equal(get_element(tableau, i, column), 1)) {
            if (entity_row < 0) {
                // First '1'. If no other found, this is the solution.
                entity_row = i;
            } else {
                // Already found a '1' before. This is not an identity vector.
                return -1;
            }
        } else if (!equal(get_element(tableau, i, column), 0)) {
            // The cell does not contain a '0' or '1'.
            // Not an identity vector.
            return -1;
        }
    }
    return entity_row;
}

/********************************************************
 * Writes the final optimal values to the output allocation,
 * so that the Java side can read it out.
 ********************************************************
 * tableau: The tableau which contains the final results.
 */
static void write_result(Tableau_t *tableau) {
    LOG("Writing results to output allocation.", 0);
    // If this is a dual program, we need to convert the LP solutions to the DP solutions
    if (tableau->dual_program) {
        LOG("Retrieving DP solutions.", 0);
        // In this case it is quite easy.
        // We know that the tableau has N rows.
        // N is also the number of the LP program variables.
        // Also the last column is the 'b' vector.
        // The first N columns are the columns for the LP solutions.
        // All other columns contain the solutions for the DP program, in order.
        int start_index = tableau->columns - tableau->rows;
        int end_index = tableau->columns - 1;
        for (int j = start_index; j < end_index; j++) {
            float value = get_element(tableau, tableau->rows - 1, j);
            rsSetElementAt_float(solution_vector, value, j - start_index);
            LOG("", value);
        }
        rsSetElementAt_int(result_size, end_index - start_index, 0);
        return;
    }
    // For each column we find the basis variable.
    LOG("Retrieving LP solutions.", 0);
    for(int j = 0; j < tableau->columns - 1; j++) {
        int entity_row = find_entity_row(tableau, j);
        if (entity_row != -1) {
            // We write the value to the output allocation.
            float value = get_element(tableau, entity_row, tableau->columns - 1);
            rsSetElementAt_float(solution_vector, value, j);
            LOG("", value);
        } else {
            // In this case we write zero.
            rsSetElementAt_float(solution_vector, 0, j);
            LOG("", 0);
        }
    }
    rsSetElementAt_int(result_size, tableau->columns - tableau->rows, 0);
}

/********************************************************
 * Entry function, which starts the optimization process.
 * Make sure you have allocated the memory for the output
 * variables, and supplied the input tableau from the Java
 * part.
 */
void solve() {
    // The tableau has been already set from the Java side.
    // Start with the optimization loop.
    add_slack_variables(tableau);
    int iter = 0;
    while(++iter) {
        // The entering column is the column with the most negative cell in the bottom row.
        int entering_column = find_entering_column(tableau);
        if (entering_column < 0) {
            // If all values in the bottom row are positive, we have reached
            // the optimal solution.
            LOG("No entering column found.", 0);
            LOG("Script finished with an optimal result. Loops: ", iter);
            write_result(tableau);
            return;
        }
        // The departing row is the row where the element
        // found in the crossing of the row and the entering column
        // has the most non-negative ratio (closest to zero) from all the rows.
        int departing_row = find_departing_row(tableau, entering_column);
        if (departing_row < 0) {
            // If none of the ratios are positive, the solution is unbound,
            // so we can't find a max possible value.
            LOG("Solution has no maximum value.", 0);
            LOG("Script finished without a result.", 0);
            rsSetElementAt_int(result_size, -1, 0);
            return;
        }
        // Do the pivoting around the cell which is in the crossing of the
        // entering column and departing row.
        bool successful = do_pivoting(tableau, departing_row, entering_column);
        if (!successful) {
            // Theoretically this should not happen.
            LOG("Unexpected result while pivoting.", 0);
            LOG("Script finished without a result. ", 0);
            rsSetElementAt_int(result_size, -1, 0);
        }
        if (iter > MAX_ITER) {
            LOG("Too many iterations > ", iter);
            LOG("Script finished with suboptimal result.", 0);
            write_result(tableau);
            return;
        }
    }
}