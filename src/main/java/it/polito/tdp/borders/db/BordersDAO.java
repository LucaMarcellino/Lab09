package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public Map<Integer,Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		Map<Integer,Country> result = new HashMap<Integer,Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country c= new Country( rs.getString("StateAbb"),rs.getInt("ccode"), rs.getString("StateNme"));
				result.put(rs.getInt("ccode"), c);
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno,Map<Integer,Country> nazioni) {
		List<Border> confini =new ArrayList<>();
		String sql = "";
		
		if(anno>2005) {
			sql="SELECT c1.state1no,c1.state2no,c1.YEAR FROM contiguity2006 AS c1,contiguity2006 AS c2 WHERE c1.state1no=c2.state1no and c1.state2no=c2.state2no and c1.state1no<c2.state2no AND c1.conttype=1";
			try {
				Connection conn = ConnectDB.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet rs = st.executeQuery();
				
				while(rs.next()) {
					Country nazioneA= nazioni.get(rs.getInt("state1no"));
					Country nazioneB= nazioni.get(rs.getInt("state2no"));
					int anno1=rs.getInt("YEAR");
					Border bo= new Border(nazioneA, nazioneB, anno1);
					confini.add(bo);
				}
				conn.close();
				return confini;
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
		
			
		}
		else {
			sql="SELECT c1.state1no,c1.state2no,c1.YEAR FROM contiguity as c1,contiguity AS c2 WHERE c1.state1no=c2.state1no and c1.state2no=c2.state2no and c1.state1no<c2.state2no AND c1.year<=? AND c1.conttype=1 group BY c1.state1no,c1.state2no,c1.year";         
		
			try {
				Connection conn = ConnectDB.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1,anno);
				ResultSet rs = st.executeQuery();
				
				while(rs.next()) {
					Country nazioneA= nazioni.get(rs.getInt("state1no"));
					Country nazioneB= nazioni.get(rs.getInt("state2no"));
					int anno1=rs.getInt("YEAR");
					Border bo= new Border(nazioneA, nazioneB, anno1);
					confini.add(bo);
				}
				conn.close();
				return confini;
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
			
		}
		
	}
	
	public List<Country> getVertex(int anno, Map<Integer, Country> nazioni){
		List<Country> nazioni1 = new ArrayList<>();
		String sql="";
		
		if(anno>2005) {
			sql="SELECT DISTINCT(state1no) from contiguity2006 WHERE conttype=1";
			try {
				Connection conn = ConnectDB.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet rs = st.executeQuery();
				
				while(rs.next()) {
					Country nazioneA= nazioni.get(rs.getInt("state1no"));
					nazioni1.add(nazioneA);

				}
				conn.close();
				return nazioni1;
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
		
		}
			else {
				sql="SELECT DISTINCT(state1no) from contiguity WHERE conttype=1 AND YEAR<=?";
				try {
					Connection conn = ConnectDB.getConnection();
					PreparedStatement st = conn.prepareStatement(sql);
					st.setInt(1, anno);
					ResultSet rs = st.executeQuery();
					
					while(rs.next()) {
						Country nazioneA= nazioni.get(rs.getInt("state1no"));
						nazioni1.add(nazioneA);
					}
					conn.close();
					return nazioni1;
					
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Errore connessione al database");
					throw new RuntimeException("Error Connection Database");
				}
			}
		
	}
	
}
