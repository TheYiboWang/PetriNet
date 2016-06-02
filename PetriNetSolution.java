/* PetriNetSolution.java
 * 
 * This program parses input file and emulate the petri-net execution printing the number of tokens
 * in each place at the end of each cycle. The program exits when the maximum number of cycles has
 * been reached or the petri-net is no longer executable.
 * 
 * This program takes two command­line arguments, the name of the input file and an integer 
 * specifying the maximum number of cycles to execute. 
 * 
 * In the input file, p​lace,​ t​ransition,​and e​dge​ are keywords. Identifiers (for places and transitions) 
 * may be short case­sensitive strings of up to size 4. The number of initial tokens may be 0 to a 
 * maximum of 100. 
 * 
 * How to run: 
 * for example we have initial state file name is initialState.txt, and firing cycle = 10.
 * To compile this program: javac PetriNetSolution.java
 * To run this program: java PetriNetSolution initialState.txt 10
 * 
 * Resutls are printed on screen. The initial state file enclosed has been tested.
 * 
 * author Yibo Wang 56043171
 * Jun 2, 2016
 */


import java.util.*;
import java.io.*;

public class PetriNetSolution {
    
    public static void main(String[] args) throws Exception {
    	int cycle = Integer.valueOf(args[1]);	
    	PetriNet pn = new PetriNet();
        String sourcefile = args[0];
        pn.createPetriNet(sourcefile);
        
        // print initial state
        System.out.println("initial state: ");
        Iterator<Place> ppit = pn.places.iterator();
        while (ppit.hasNext()) {
            Place p = ppit.next();
            System.out.println(p.name + " " + p.tokenNumber);
        }
        System.out.println("-------------------");
        
        
        List<Transition> list = pn.getWhoCanFire(); //get all transitions which can fire;
        
        while (!list.isEmpty() && cycle != 0) {
        	Collections.shuffle(list);
        	Transition tfire = list.get(0);
        	tfire.fire();
        	System.out.println("after firing: " + tfire.name );
        	
        	Iterator<Place> pit = pn.places.iterator();
        	while (pit.hasNext()) {
        		Place p = pit.next();
        		System.out.println(p.name + " " + p.tokenNumber);
        	}
        	System.out.println("-------------------");
        	
        	list = pn.getWhoCanFire();
        	cycle--;
        }

    }
    
    static class Place {
        int tokenNumber;
        String name;
        List<Transition> in;
        List<Transition> out;
        
        public Place(String n, int tn) {
            name = n;
            tokenNumber = tn;
            in = new ArrayList<Transition>();
            out = new ArrayList<Transition>();
        }
    }
    
    static class Transition {
        String name;
        List<Place> in;
        List<Place> out;
        
        public Transition(String n) {
            name = n;
            in = new ArrayList<Place>();
            out = new ArrayList<Place>();
        }
        
        public boolean canFire() {
            Iterator<Place> it = in.iterator();
            while (it.hasNext()) {
            	Place p = it.next();
            	int n = 1;
            	for (int i = in.indexOf(p)+1; i<in.size(); i++) {
            		if (p.name.equals(in.get(i).name)) n++;
            	}
            	
            	
                if (p.tokenNumber < n) return false;
            }
            return true;
        }
        
        public void fire() {
            Iterator<Place> iit = in.iterator();
            while (iit.hasNext()) {
            	iit.next().tokenNumber--;
            }
            Iterator<Place> oit = out.iterator();
            while (oit.hasNext()) {
            	oit.next().tokenNumber++;
            }	
        }
    }
    
    static class Edge {
        Place place;
        Transition transition;
        
        public Edge (Place p, Transition t) {
            place = p;
            transition = t;
        }
        
    }  

    static class PetriNet {
        List<Place> places;
        List<Edge> edges;
        List<Transition> transitions;
        
        public PetriNet() {
            places = new ArrayList<Place>();
            edges = new ArrayList<Edge>();
            transitions = new ArrayList<Transition>();
        }
        
        public void createPetriNet(String sourcefile) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(sourcefile));            
                String line = null;
                while ((line = br.readLine()) != null) {
//                    System.out.println(line);
                    StringTokenizer stk = new StringTokenizer(line," "); 
                    
                    String s1 = stk.nextToken();
                    
                    if (s1.equals("place")) {
                            String name = stk.nextToken();
                            String number = stk.nextToken();
                            int n = Integer.valueOf(number);
                            
                            Place p = new Place(name, n);                            
//                            System.out.println(p.name);
//                            System.out.println(p.tokenNumber);
                            places.add(p);

                    } else if (s1.equals("transition")) {
                            String name = stk.nextToken();
                            Transition t = new Transition(name);
                            transitions.add(t);
//                            System.out.println(t.name);

                    } else if (s1.equals("edge")) {
                            String from = stk.nextToken();
//                            System.out.println(from);
                            String to = stk.nextToken();
//                            System.out.println(to);
                            
                            insertEdge(from, to);
                    }
                    
                }
                
             br.close();
  
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public void insertEdge(String from, String to) {
        	Place pp = null;
        	Transition tt = null;
        	boolean placeToTransition = false;
        	
        	Iterator<Place> pit  = places.iterator();
        	while (pit.hasNext()) {
        		Place p1 = pit.next();
        		if (p1.name.equals(from)) {
        			placeToTransition = true;
        			pp = p1;
        			break;
        		} else if (p1.name.equals(to)) {
        			placeToTransition = false;
        			pp = p1;
        			break;
        		}
        	}
        	
        	Iterator<Transition> tit = transitions.iterator();
        	while (tit.hasNext()) {
        		Transition t1 = tit.next();
        		if (placeToTransition) {
        			if (t1.name.equals(to)) {
        				tt = t1;
        				break;
        			}
        		} else if (t1.name.equals(from)) {
        			tt = t1;
        			break;
        		}
        	}


        	if (placeToTransition) {
        		pp.out.add(tt);
        		tt.in.add(pp);
        	} else {
        		pp.in.add(tt);
        		tt.out.add(pp);
        	}
        	
        }
        
        public List<Transition> getWhoCanFire() {
            List<Transition> listOfWhoCanFire = new ArrayList<Transition>();
            Iterator<Transition> lit = transitions.iterator();
            while (lit.hasNext()) {
            	Transition t = lit.next();
            	if (t.canFire()) listOfWhoCanFire.add(t);
            }
            
            return listOfWhoCanFire;
        }
    }
    
    
}


