"""
GRASP implementation for QBF with Set Cover constraints
"""

import random

from typing import List
from core.abstract_grasp import AbstractGRASP
from core.solution import Solution
from problems.qbf_sc import QBF_SC


class GRASP_QBF_SC(AbstractGRASP[int]):
    """GRASP implementation for QBF with Set Cover constraints"""
    
    def __init__(self, seed: int, target: float, alpha: float, maxTimeSec: int, filename: str, 
                 construction_method: str = "standard", local_search_method: str = "first_improving"):
        super().__init__(seed, target, QBF_SC(filename), alpha, maxTimeSec, construction_method, local_search_method)
    
    def make_CL(self) -> List[int]:
        """Create candidate list with all subsets"""
        return list(range(self.obj_function.get_domain_size()))
    
    def make_RCL(self) -> List[int]:
        """Create empty RCL (populated during constructive phase)"""
        return []
    
    def update_CL(self):
        """Update CL to include only subsets that can cover uncovered variables"""
        if self.obj_function.is_feasible(self.sol):
            # If already feasible, any subset not in solution can be candidate
            self.CL = [i for i in range(self.obj_function.get_domain_size()) if i not in self.sol]
        else:
            # Only consider subsets that cover uncovered variables
            uncovered = self.obj_function.get_uncovered_variables(self.sol)
            self.CL = []
            for i in range(self.obj_function.get_domain_size()):
                if i not in self.sol:
                    subset_coverage = self.obj_function.subsets.get(i, set())
                    if uncovered.intersection(subset_coverage):  # Covers at least one uncovered variable
                        self.CL.append(i)
    
    def create_empty_sol(self) -> Solution[int]:
        """Create empty solution"""
        sol = Solution[int]()
        sol.cost = float('-inf')
        return sol
    
    def make_feasible(self):
        """Make solution feasible by adding necessary subsets"""
        max_attempts = 100
        attempts = 0
        
        while not self.obj_function.is_feasible(self.sol) and attempts < max_attempts:
            uncovered = self.obj_function.get_uncovered_variables(self.sol)
            if not uncovered:
                break
            
            # Find subset that covers most uncovered variables
            best_subset = None
            best_coverage = 0
            
            for i in range(self.obj_function.get_domain_size()):
                if i not in self.sol:
                    subset_coverage = self.obj_function.subsets.get(i, set())
                    coverage_count = len(uncovered.intersection(subset_coverage))
                    if coverage_count > best_coverage:
                        best_coverage = coverage_count
                        best_subset = i
            
            if best_subset is not None:
                self.sol.add(best_subset)
            else:
                break
            
            attempts += 1
        
        self.obj_function.evaluate(self.sol)
    
    def local_search(self) -> Solution[int]:
        """Local search with insertion, removal, and exchange moves"""

        improved = True
        while improved:
            improved = False
            best_move = None
            best_delta = 0.0
            
            # Current candidates (not in solution)
            current_CL = [i for i in range(self.obj_function.get_domain_size()) if i not in self.sol]

            indices_CL = list(range(len(current_CL)))
            random.shuffle(indices_CL)
            
            # Evaluate insertions
            for idx in indices_CL:
                cand_in = current_CL[idx]
                
                delta_cost = self.obj_function.evaluate_insertion_cost(cand_in, self.sol)
                if delta_cost > best_delta:
                    best_delta = delta_cost
                    best_move = (cand_in, None)
                    if self.local_search_method == "first_improving":
                        break
            
            sol_list = list(self.sol)
            indices_out = list(range(len(sol_list)))
            random.shuffle(indices_out)

            if not (self.local_search_method == "first_improving" and best_move is not None):
                # Evaluate removals
                for idx in indices_out:
                    cand_out = sol_list[idx]

                    delta_cost = self.obj_function.evaluate_removal_cost(cand_out, self.sol)
                    if delta_cost > best_delta:
                        best_delta = delta_cost
                        best_move = (None, cand_out)
                        if self.local_search_method == "first_improving":
                            break
            
            if not (self.local_search_method == "first_improving" and best_move is not None):
                sol_list = list(self.sol)
                indices_out = list(range(len(sol_list)))
                random.shuffle(indices_out)
                
                # Evaluate exchanges
                for idx_in in indices_CL:
                    cand_in = current_CL[idx_in]

                    for idx_out in indices_out:
                        cand_out = sol_list[idx_out]

                        delta_cost = self.obj_function.evaluate_exchange_cost(cand_in, cand_out, self.sol)
                        if delta_cost > best_delta:
                            best_delta = delta_cost
                            best_move = (cand_in, cand_out)
                            if self.local_search_method == "first_improving":
                                break
                    if self.local_search_method == "first_improving" and best_move is not None:
                        break
            
            # Apply best move
            if best_move is not None:
                cand_in, cand_out = best_move

                if cand_in is not None and cand_out is not None:
                    self.sol.remove(cand_out)
                    self.sol.add(cand_in)
                elif cand_in is not None:
                    self.sol.add(cand_in)
                elif cand_out is not None:
                    self.sol.remove(cand_out)
                    
                
                self.obj_function.evaluate(self.sol)
                improved = True
        
        return self.sol