# PetriNet
* PetriNetSolution.java

 * This program parses input file and emulate the petri-net execution printing the number of tokens in each place at the end of each cycle. The program exits when the maximum number of cycles has been reached or the petri-net is no longer executable.
 
 * This program takes two command­line arguments, the name of the input file and an integer specifying the maximum number of cycles to execute. 
 
 * In the input file, place, transition, and edge​ are keywords. Identifiers (for places and transitions) may be short case­sensitive strings of up to size 4. The number of initial tokens may be 0 to a maximum of 100. 
  
 * How to run: 
 * for example we have initial state file name is initialState.txt, and firing cycle = 10.
 * To compile this program: javac PetriNetSolution.java
 * To run this program: java PetriNetSolution initialState.txt 10
  
 * Resutls are printed on screen. The initial state file enclosed has been tested.
  
 * author Yibo Wang
 * Jun 2, 2016
 */
