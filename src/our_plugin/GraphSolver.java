package our_plugin;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.BronKerboschCliqueFinder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class GraphSolver {
	
	
	
	public static void SolveGraph(MethodCloneInstance mci) {
		// TODO Auto-generated method stub
		int[][] matrix = mci.getAdjeceny_Matrix_For_Overlaps();
		UndirectedGraph<Integer, DefaultEdge> graph =
			      new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
		for(int i=0;i < mci.getSCCs().size();i++) // ADD ALL SCC INSTANCE CUSTOMS ( AS VERTICES )  
		{
			graph.addVertex(i);
		}
		for(int i = 0; i < mci.getSCCs().size();i++) // ADD ALL EDGES NOW
		{
			for(int j = 0 ; j < mci.getSCCs().size();j++)
			{
				if(matrix[i][j]==1)
				{
					graph.addEdge(i, j);	
				}
			}
		}
		BronKerboschCliqueFinder check= new BronKerboschCliqueFinder(graph);
		  Collection<Set<Integer>> set = check.getAllMaximalCliques(); // THIS ALGORITHM WILL FIND ALL CLIQUES
		Iterator<Set<Integer>> iterator = set.iterator();
		int maxweight=0;
		Set<Integer> maxweightedclique = null;
		while(iterator.hasNext()) // ITERATE THROUGH CLIQUES
		{  
			Set<Integer> currset=iterator.next();
			int currweight=0;
			Iterator<Integer> itr = currset.iterator();
			while(itr.hasNext()) // ITERATE THROUGH EACH INDIVIDUAL CLIQUE
			{
				currweight=currweight+mci.getSCCs().get(itr.next()).getCode().length();					  
     		}
			if(currweight>maxweight)
			{
				maxweight=currweight;
				maxweightedclique=currset;
			}
		}
		Iterator<Integer> itr = maxweightedclique.iterator();
		while(itr.hasNext()) // NOW ADD THE NEW SCC INSTNACES 
		{
			int index=itr.next();
			mci.getNewSccs_Contained().add(mci.getSCCs().get(index));
 		}
	}
}
