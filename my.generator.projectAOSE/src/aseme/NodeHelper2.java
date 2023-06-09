package aseme;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import statechart.Model;
import statechart.Node;
import statechart.Transition;
import statechart.impl.TransitionImpl;

public class NodeHelper2 {
	public static  ArrayList<Node> p = new ArrayList<Node>();
	public static  ArrayList<Node> nodeStateList = new ArrayList<Node>();
	public static  Map<Node,NodeStore> adjacencyList = new HashMap<Node,NodeStore>();
	public static  ArrayList<String> actionsList = new ArrayList<String>();

	/*
	 * {Node : {"adjacentSibling":ArrayList<Node>,
	 * 			"count" : int,
	 * 			"transition";ArrayList<Transition>
	 * 			}
	 */
		 public static String timestamp() {
			 return String.valueOf( System.currentTimeMillis() );
		 }
		 public String getActions() {
			 String returnMessage="";
			 for (int i =0 ; i<actionsList.size();i++) {
				 returnMessage += "to ";
				 returnMessage += actionsList.get(i).replace("(", "[").replace(")", "]")+" :"+"\n";
				 returnMessage  += "end"+"\n";
			 }
			 return returnMessage;
		}
		public List<Node> tree2list(Node h, List<Transition> l){
			if (!p.isEmpty()){
				p = new ArrayList<Node>();
			}
			
				    Stack<Node> s = new Stack<Node>();
				    
				    s.push(h);
				    p.add(h);
				    
				    while(!s.empty()){
				    	
				    	
				       h = s.pop();
				       
				       if (h.getChildren() != null){
				    	   for(int i=0; i< h.getChildren().size(); i++){
				    		  	   
					    	   if (!(p.contains(h.getChildren().get(i)))){
					    		   s.push(h.getChildren().get(i));
					    		   p.add(h.getChildren().get(i));
					    		 
					    	   }
					    	   else{
					    		    h = s.pop();
					    	   }
					       }
				    	   
				       }
				       else{	h=	s.pop();
				    	  
				       }       
				      
				    }
				    
				    //System.err.println("tree2list post-while");
				    //for (int j=0; j<p.size(); j++ ){
				    	//System.out.println(p.get(j).getLabel());
				    //}
				    initAdjacencyList(l);
				    return p;
				 }
		public void initAdjacencyList(List<Transition> l){
			for(int i =0 ; i < p.size();i++) {
				//Init Adjancency List
				Node iter = p.get(i);
				//String iterName = iter.getName().replaceAll(" ", "_");
				//iter.setName(iterName);
				adjacencyList.put(iter, new NodeStore());
				if(nodeStateType(iter)) {// node is a node state type
					if(iter.getType().compareTo("OR")==0) {
						if(iter.getFather()!=null) {
							nodeStateList.add(iter);
						}
					}
					else {
						nodeStateList.add(iter);
					}
				}
			}
			//iter through edges
			Transition iter ;
			for (int j=0 ; j< l.size(); j++) {//Pour toutes transitions
				iter = l.get(j);
				if(iter.getTE()!=null) {
					String iterName = iter.getTE().replaceAll(" ", "_");
					//extract function in TE in condition and action
					addFunction(iterName);
					iterName = iterName.replace("(", " ");
					iterName = iterName.replace(")", " ");

					iter.setTE(iterName);
					// add to action list if function in TE
				}

				if(adjacencyList.containsKey(iter.getSource())) {//adding edge
					//start go to edge -> go directly to start
					if(iter.getSource().getType().compareTo("START")==0) {
						adjacencyList.get(iter.getSource().getFather()).add(iter.getTarget(),iter);

					}
					else {
						adjacencyList.get(iter.getSource()).add(iter.getTarget(),iter);
					}
					
				}
			}
		}
		/*
		public Node getSourceNode(Node n, int counter) {
			ArrayList<Node> sourceNodeList = adjacencyList.get(n);// possible source of a node
			Node sourceNode = sourceNodeList.get(0);
			if(sourceNode.getType().compareTo("CONDITION")==0) {// source Node is a condition
				counter++;
				return getSourceNode(sourceNode,counter);
			}
			else {
				return sourceNode;
			}
			
		}*/
		public String nodeToCode(Node n ) {
			// Mark all the vertices as not visited(By default
	        // set as false)
			if(!testType(n)) {
				return "";
			}
			Map<Node,Boolean> visited = new HashMap<Node,Boolean>();	 
	        // Create a queue for BFS
	        LinkedList<Node> queue = new LinkedList<Node>();
	        String returnMessage ="";
	        boolean stopFunction=false;//stop the function if we encounter basic node
	        // Mark the current node as visited and enqueue it
	        visited.put(n, true);
	        queue.add(n);
	        int level = 1;
			String retenue = "";

	        while (queue.size() != 0) {
	            // Dequeue a vertex from queue and print it
	        	int level_size = queue.size();
	        	while (level_size-- != 0) {
	        		Node iter = queue.poll();
	 
	        		// Get all adjacent vertices of the dequeued
	        		// vertex s If a adjacent has not been visited,
	        		// then mark it visited and enqueue it
	        		Iterator<Node> i = adjacencyList.get(iter).getNodes().listIterator();
	        		Iterator<Transition> t = adjacencyList.get(iter).getTransitions().listIterator();
	        		if(iter.getType().compareTo("OR")==0 && iter.getFather()==null) {//
	        			for(int z =0 ; z<nodeStateList.size();z++) {
		        			returnMessage+= "\t".repeat(level)+"if etat = \""+nodeStateList.get(z).getName()+"\""+" ["+"\n";
		        			returnMessage+= "\t"+"\t".repeat(level)+nodeStateList.get(z).getName()+"\n"+"]"+"\n";
	        			}
	        		}

	        		while (i.hasNext()) {
	        			
	        			Node nodeI = i.next();
	        			Transition transitionT = t.next(); 
	        			if (!visited.containsKey(nodeI)) {//NOEUD NON VISITE => Creer du code netlogo

	        				if(testType(nodeI)) {//Noeud non condition
	        					if(transitionT.getTE()==null) {
	        						if(transitionT.getTarget().getType().compareTo("END")!=0) {
				        				returnMessage+= "\t".repeat(level)+"set etat "+"\""+transitionT.getTarget().getName()+"\""+"\n";
	        						}
	        					}
	        					else {
			        				returnMessage+= "\t".repeat(level)+"if "+transformAction(transitionT.getTE(),level)+"\n";
			        				returnMessage+= "\t"+"\t".repeat(level)+"set etat "+"\""+transitionT.getTarget().getName()+"\""+"\n"+"]"+retenue+"\n";
			        				if(retenue.compareTo("")!=0) {
				        				retenue = "";
			        				}
			        				
	        					}

	        					visited.put(nodeI, true);

	        				}
	        				else {// Noeud condition
	        					if(transitionT.getTE()==null) {
	        						level--;
	        					}
	        					else {
		        					returnMessage+= "\t".repeat(level)+"if "+transformAction(transitionT.getTE(),level)+"\n";
		        					retenue += "]";
	        					}
	        					//returnMessage+= "\t"+"\t".repeat(level)+transitionT.getTarget().getName()+"\n";
	        					visited.put(nodeI, true);
	        					queue.add(nodeI);
	        				}
	        			}
	        		}
	            level++;
	        	}
	        }
			return returnMessage;
		}
		/*
		public String checkTransitionFor(Transition t, Node n) {
			/*This function objective 			 * 
			 * @return : netlogo Code if a transition originate from a node s
			 * Transition with
			 * (1) sourceNode has condition or
			 * find first not node
			 
			String returnMessage = "";
			//CHECK if transition source is node or not
			if(t.getSource().equals(n) && testType(t.getTarget())) {//Matching sourceNode and targetNode is not rejected
				returnMessage+="\t"+"if "+t.getTE()+"\n";
				returnMessage+="\t\t"+t.getTarget().getName()+"\n";
				return returnMessage;
			}

			//sourceNode is a rejected type, go back up tree
			if(!testType(t.getSource())) {
				int counter = 1;
				Node sourceNode2 = getSourceNode(t.getSource(),counter);
				if(sourceNode2.equals(n)) {
					returnMessage+="\t"+"if "+t.getTE()+"\n";
					returnMessage+="\t\t"+t.getTarget().getName()+"\n";
					return returnMessage;
					//target condition of target condition
				}
			}
			return "";
		}*/
		// split TE expressionto get condition and action call
		public String transformAction(String s,int level) {
			if(s==null) {
				return "";
			}
			String[] teSplit = s.split("/");
			if(teSplit.length==2) {
				String transition = teSplit[0];
				String action = teSplit[1];
				addActionsList(action.strip());
				String messageReturn  = transition+" ["+"\n";
				messageReturn += "\t"+"\t".repeat(level)+action;
				return messageReturn;
			}
			else if(teSplit.length==1) {
				return s+" [";
			}
			else {
				System.out.println("split by /  give 2+ part");
				return "";
			}
		}
		public boolean testType(Node n ) {
			ArrayList<String> rejectedType= new ArrayList<String>();
			rejectedType.add("CONDITION");
			for(int i =0 ; i<rejectedType.size();i++) {
				if(n.getType().compareTo(rejectedType.get(i))==0) {
					return false;
				}
			}
			return true;
		}
		public boolean nodeStateType(Node n ) {
			ArrayList<String> rejectedType= new ArrayList<String>();
			rejectedType.add("CONDITION");
			rejectedType.add("START");
			rejectedType.add("END");

			for(int i =0 ; i<rejectedType.size();i++) {
				if(n.getType().compareTo(rejectedType.get(i))==0) {
					return false;
				}
			}
			return true;
		}
		public static String extractFunction(String input) {
		    // Regular expression pattern to match a function call with optional arguments
		    String functionCallPattern = "\\w+\\((\\w,?)*\\)";

		    // Create a regular expression object
		    Pattern pattern = Pattern.compile(functionCallPattern);

		    // Use the regular expression object to search for a match in the input string
		    Matcher matcher = pattern.matcher(input);

		    // If a match was found, extract the function call string
		    if (matcher.find()) {
		        return matcher.group();
		    }
		    // If no match was found, return null
		    return null;
		}
		public static void addActionsList(String s) {
			int indexOpen = s.indexOf("(");
			int indexEnd = s.indexOf(")");
			if(indexEnd <0 || indexOpen <0) {
				return ;
			}
			int diff = indexEnd-indexOpen;
			String send = s;
			if(diff==1) {//no arg
				send = send.replace("(", " ").replace(")", " ").strip();

			}
			else {//arg
				send = send.replace("(", " [").replace(")", "]").strip();
			}
			if(!actionsList.contains(send) && !send.contains("and")) {
				actionsList.add(send);
			}
		}
		public static void addFunction(String s) {
			if(s!=null) {
				String[] teSplit = s.split("/");
				if(teSplit.length==2) {
					String transition = teSplit[0];
					String action = teSplit[1];
					addActionsList(action.strip());
					String functionFound = extractFunction(transition);
					if(functionFound!=null) {
						addActionsList(functionFound.strip());
					}
				}
				else if(teSplit.length==1) {
					String functionFound = extractFunction(s);
					if(functionFound!=null) {
						addActionsList(functionFound.strip());
					}
				}
			}
		}
}
