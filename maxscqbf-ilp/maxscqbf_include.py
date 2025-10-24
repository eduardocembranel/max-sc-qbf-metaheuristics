"""
this file contains:

1) the class that will be used to store an instance of the max-sc-qbf problem.
2) the readInstance function that reads the input of the problem from stdin 
    and loads it into an object of the MAXSCQBF class.
"""

from dataclasses import dataclass
from typing import List

@dataclass(slots=True)
class MAXSCQBF:
    # number of variables
    n: int
    # incidence matrix representing the list of subsets of variables.
    # S[i][j] = 1 means the subset S[i] contains the variable j.
    # S[i][j] = 0 means the subjet S[i] does not contain the variable j. 
    S: List[List[int]]
    # coefficient matrix A = (a_ij) of the MAX-QBF problem.
    # A is an n x n matrix of real numbers representing the coefficients.
    A: List[List[float]]

def readInstance() -> MAXSCQBF:
    n = int(input())
    setSizes = list(map(int, input().split()))
    S = [[0] * n for _ in range(n)]
    for i in range(n):
        row = list(map(int, input().split()))
        for var in row:
            S[i][var - 1] = 1

    A = [[0] * n for _ in range(n)]
    for i in range(n):
        row = list(map(float, input().split()))
        for j, val in enumerate(row):
            A[i][i + j] = val

    return MAXSCQBF(n=n, S=S, A=A)