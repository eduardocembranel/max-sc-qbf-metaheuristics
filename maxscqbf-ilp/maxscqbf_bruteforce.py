"""
This file contains a brute-force algorithm that provides an exact and optimal solution to the SCQBF problem testing
all the possible combinations for the variables x[i].

This should be used only for solving small instances to compare the results with the ILP solver.
"""

from typing import List
import itertools

from maxscqbf_include import MAXSCQBF, readInstance

def bruteforce(maxscqbf: MAXSCQBF):
    #generate all combinations (2^n)
    combinations = list(itertools.product([0, 1], repeat=maxscqbf.n))

    bestVal = -float('inf')
    #bestX = None
    for x in combinations:
        if isFeasibleSolution(maxscqbf, x):
            val = calcSolution(maxscqbf, x)
            if val > bestVal:
                bestVal = val
                #bestX = x

    print("Optimal solution: ", round(bestVal, 4))
    #print(bestX)

def isFeasibleSolution(maxscqbf: MAXSCQBF, x: List[int]) -> bool:
    varsCovered = set()
    for i in range(maxscqbf.n):
        if x[i] == 1:
            for j in range(maxscqbf.n):
                if maxscqbf.S[i][j] == 1:
                    varsCovered.add(j)

    return len(varsCovered) == maxscqbf.n

def calcSolution(maxscqbf: MAXSCQBF, x: List[int]):
    val = 0
    for i in range(maxscqbf.n):
        for j in range(maxscqbf.n):
            val += maxscqbf.A[i][j] * x[i] * x[j]
    return val

if __name__ == '__main__':
    maxscqbf = readInstance()
    bruteforce(maxscqbf)
