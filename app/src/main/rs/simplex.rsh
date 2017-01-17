const int MAX_ROWS  = 50;
const int MAX_COLS  = 50;

const int MAX_ITER = 100;

const float EPSILON = 1.0e-6;  // When comparing two values, if the difference between them
                               // is smaller than epsilon, they are considered equal.
                               // Smaller epsilons yield better punctuality, but more loops.
const float CUT_OFF = 1.0e-10; // The value below which we treat values as zero.

// Set this to true if you want to see debug logs.
// Makes the program run slower.
#define DEBUG false

#if DEBUG
#define LOG(msg, param) rsDebug(msg, param)
#else
#define LOG(msg, param)
#endif

typedef struct __attribute__((packed)) Tableau {
  int rows, columns; // M rows, N columns, and an [m x n] matrix
  float matrix[MAX_ROWS * MAX_COLS]; // Multidimensional arrays are sadly not supported, so we will have to map between one and two dimensions.
  bool dual_program; // If this is true, the program will return the slack variable solutions, instead of the LP solution.
} Tableau_t;

// The input tableau, to be set on the Java side.
Tableau_t *tableau;
// We will write to these allocations when we have the final result
rs_allocation solution_vector;
rs_allocation result_size;
// Main function.
void solve();