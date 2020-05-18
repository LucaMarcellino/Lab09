package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

		private Graph<Country,DefaultEdge> grafo;
		private List<Border> confini; 
		private List<Country> listaNazionii; 
		private Map<Integer,Country> nazioni;
		private Map<Country,Country> visita;
		private BordersDAO dao;
	
		public Model() {
			this.grafo= new SimpleGraph<>(DefaultEdge.class);
			this.dao=new BordersDAO();
			this.nazioni= new HashMap<>(dao.loadAllCountries());
			this.visita=new HashMap<>();
	}

		public void creaGrafo(int anno) {
			
			this.confini= new ArrayList<Border>(dao.getCountryPairs(anno,nazioni));
			this.listaNazionii=new ArrayList<>(dao.getVertex(anno, nazioni));
			Graphs.addAllVertices(grafo, listaNazionii);
			
			for(Border b: confini ) {
				grafo.addEdge(b.getStatoA(),b.getStatoB());
			}
			
			
		}
		
		
		
		public List<Country> getListaNazionii(int anno) {
			this.creaGrafo(anno);
			return listaNazionii;
		}


		public int numeroArchi(int anno,Country c) {
			this.creaGrafo(anno);
			return grafo.outDegreeOf(c);
		}
		
		public int numeroVertex(int anno) {
			this.creaGrafo(anno);
			return grafo.vertexSet().size();
		}
		
		public List<Country> vicini(Country c){
			List<Country> result = new ArrayList<>();
			
			BreadthFirstIterator<Country , DefaultEdge> bfi = new BreadthFirstIterator<Country, DefaultEdge>(grafo,c);
			visita.put(c, null);
			
			bfi.addTraversalListener(new TraversalListener<Country,DefaultEdge>(){

				@Override
				public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
					Country soregente= grafo.getEdgeSource(e.getEdge());
					Country arrivo = grafo.getEdgeTarget(e.getEdge());
					if(!visita.containsKey(arrivo) && visita.containsKey(soregente)) {
						visita.put(arrivo, soregente);
					}else if(visita.containsKey(arrivo) && !visita.containsKey(soregente)) {
						visita.put(soregente, arrivo);
					}
					
				}

				@Override
				public void vertexTraversed(VertexTraversalEvent<Country> e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void vertexFinished(VertexTraversalEvent<Country> e) {
					// TODO Auto-generated method stub
					
				}
				
			} );
			if(bfi.hasNext()) {
			bfi.next();
			}
			
		for(Country step : visita.keySet()) {
			result.add(step);
		}
		
		result.remove(c);
			
			return result;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
}
