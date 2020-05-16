package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

		private Graph<Country,DefaultEdge> grafo;
		private List<Border> confini; 
		private List<Country> listaNazionii; 
		private Map<Integer,Country> nazioni;
		private BordersDAO dao;
	
		public Model() {
			this.grafo= new SimpleGraph<>(DefaultEdge.class);
			this.dao=new BordersDAO();
			this.nazioni= new HashMap<>(dao.loadAllCountries());
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
		
}
