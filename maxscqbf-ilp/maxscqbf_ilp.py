import gurobipy as gp
from gurobipy import GRB

from maxscqbf_include import MAXSCQBF, readInstance

def solve(maxscqbf: MAXSCQBF, timeLimitSec=1800):
    model = gp.Model("maxscqbf")
    model.setParam("TimeLimit", timeLimitSec)
    
    # one-dimensional binary vars x[i] to indicate whether the var i is selected
    x = model.addVars(maxscqbf.n, vtype=GRB.BINARY, name="x")
    
    # two-dimensional binary vars y[i, j] to indicate whether both vars i and j are selected
    y = model.addVars(
        [(i, j) for i in range(maxscqbf.n) for j in range(maxscqbf.n) if j >= i],
        vtype=GRB.BINARY,
        name="y"
    )
    
    model.setObjective(
        gp.quicksum(
            maxscqbf.A[i][j] * y[i, j] for i in range(maxscqbf.n) for j in range(maxscqbf.n) if j >= i
        ),
        GRB.MAXIMIZE
    )

    # constraints to guaranty the consistency between the variables x[i] and y[i, j]
    # y[i, j] = 1 if and only if both x[i] = 1 and x[j] = 1
    for i in range(maxscqbf.n):
        for j in range(maxscqbf.n):
            if j >= i:
                model.addConstr(y[i, j] <= x[i])
                model.addConstr(y[i, j] <= x[j])
                model.addConstr(y[i, j] >= x[i] + x[j] - 1)

    # set covering constraints
    for k in range(maxscqbf.n):
        # the variable k must be covered by at least one subset
        expr = gp.quicksum(x[i] for i in range(maxscqbf.n) if maxscqbf.S[i][k] == 1)
        model.addConstr(expr >= 1, name=f"set_covering_var_{k}")

    model.optimize()

    print("\n=================================")
    if model.Status == GRB.OPTIMAL:
        print("Optimal solution:", round(model.ObjVal, 4))
    else:
        print("Best LB:", round(model.ObjVal, 4))
        print("Best UB:", round(model.ObjBound, 4))
        print(f"Gap: {round(model.MIPGap * 100, 4)}%")

    print(f"Runtime: {model.Runtime:.2f} seconds")

if __name__ == "__main__":
    maxscqbf = readInstance()
    try:
        solve(maxscqbf)
    except gp.GurobiError as e:
        print(f"Error code {e.errno}: {e}")
    except AttributeError:
        print("Encountered an attribute error")
