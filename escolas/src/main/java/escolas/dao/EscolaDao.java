package escolas.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.postgresql.util.PGobject;

public class EscolaDao {

	//Coloquem as informações do banco de vocês
	private static final String URL = "jdbc:postgresql://localhost:5432/escolas_monteiro";
	private static final String USER = "postgres";
	private static final String PASSWORD = "Deda.9817";
	
	private EscolaDao() {
	}

	//código
	public static Connection conectar() throws Exception {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	public static void testarConexao() {
        try (Connection conexao = conectar()) {
            System.out.println("Conexão estabelecida com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco: " + e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public static void inserirPonto(String nomeString, double longitude, double latitude) {

		String coordenadaString = "(" + longitude + " " + latitude + ")";
		// Inserindo um ponto (exemplo)
		String insertSql = "INSERT INTO locais (nome, coordenadas) VALUES (?, ?)";
		try (Connection conn = EscolaDao.conectar();
		     PreparedStatement stmt = conn.prepareStatement(insertSql)) {
		    stmt.setString(1, nomeString);

		    
		    PGobject geom = new PGobject();
		    geom.setType("geometry");
		    geom.setValue("POINT" + coordenadaString); // Exemplo de coordenadas
		    stmt.setObject(2, geom);
		    stmt.executeUpdate();
		}
		catch (Exception e) {
			System.out.println("DAO : erro ao inserir ponto");
		}

	}
	
	public static void atualizarCoordenadas(int id, double longitude, double latitude) {
        String sql = "UPDATE locais SET coordenadas = ST_SetSRID(ST_MakePoint(?, ?), 4326) WHERE id = ?";
        
        try (Connection conn = EscolaDao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Definindo os parâmetros para o SQL
            stmt.setDouble(1, longitude);  // Longitude
            stmt.setDouble(2, latitude);   // Latitude
            stmt.setInt(3, id);            // ID do local

            // Executando a atualização
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("DAO : Ponto atualizado com sucesso!");
            } else {
                System.out.println("DAO : Nenhum ponto encontrado para atualizar.");
            }
        } catch (Exception e) {
            System.err.println("DAO : Erro ao atualizar o ponto: " + e.getMessage());
        }
    }
	
	public static void deletarPonto(int id) {
        String sql = "DELETE FROM locais WHERE id = ?";
        
        try (Connection conn = EscolaDao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Definindo o parâmetro para o SQL
            stmt.setInt(1, id);  // ID do local

            // Executando a exclusão
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
            	JOptionPane.showMessageDialog(null, "Escola removida com sucesso!");
            } else {
            	JOptionPane.showMessageDialog(null, "Nenhuma escola encontrada!");
            }
        } catch (Exception e) {
            System.err.println("DAO : Erro ao deletar o ponto: " + e.getMessage());
        }
    }
	
	public static void visualizarPonto(int id) {
        String sql = "SELECT nome, ST_X(coordenadas) AS longitude, ST_Y(coordenadas) AS latitude FROM locais WHERE id = ?";
        
        try (Connection conn = EscolaDao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Definindo o parâmetro para o SQL
            stmt.setInt(1, id);  // ID do local

            // Executando a consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    double longitude = rs.getDouble("longitude");
                    double latitude = rs.getDouble("latitude");
                    JOptionPane.showMessageDialog(null, "Local: " + nome 
                    									+ "\nLongitude: " + longitude
                    									+ "\nLatitude: " + latitude);
                } else {
                    System.out.println("DAO : Nenhum ponto encontrado com o ID: " + id);
                }
            }
        } catch (Exception e) {
            System.err.println("DAO : Erro ao visualizar o ponto: " + e.getMessage());
        }
    }
	
	public static void buscarPontoNome(String nome) {
        String sql = "SELECT id, ST_X(coordenadas) AS longitude, ST_Y(coordenadas) AS latitude FROM locais WHERE nome = ?";
        
        try (Connection conn = EscolaDao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Definindo o parâmetro para o SQL
        	  stmt.setString(1, nome);  // ID do local

            // Executando a consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    double longitude = rs.getDouble("longitude");
                    double latitude = rs.getDouble("latitude");
                    JOptionPane.showMessageDialog(null, "Local: " + nome 
							+ "\nLongitude: " + longitude
							+ "\nLatitude: " + latitude 
							+ "\nID: " + id);
                } else {
                    System.out.println("DAO : Nenhum ponto encontrado com o nome: " + nome);
                }
            }
        } catch (Exception e) {
            System.err.println("DAO : Erro ao visualizar o ponto: " + e.getMessage());
        }
    }
	
	public static void buscarPontoCoordenadas(double longitude, double latitude) {
        String sql = "SELECT id, nome, ST_X(coordenadas) AS longitude, ST_Y(coordenadas) AS latitude " +
                     "FROM locais WHERE ST_Equals(coordenadas, ST_SetSRID(ST_MakePoint(?, ?), 4326))";
        
        try (Connection conn = EscolaDao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Definindo os parâmetros para o SQL
            stmt.setDouble(1, longitude);  // Longitude fornecida
            stmt.setDouble(2, latitude);   // Latitude fornecida

            // Executando a consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    double lon = rs.getDouble("longitude");
                    double lat = rs.getDouble("latitude");
                    JOptionPane.showMessageDialog(null, "Local: " + nome 
							+ "\nLongitude: " + lon
							+ "\nLatitude: " + lat 
							+ "\nID: " + id);
                } else {
                    System.out.println("DAO : Nenhum ponto encontrado com as coordenadas fornecidas.");
                }
            }
        } catch (Exception e) {
            System.err.println("DAO : Erro ao buscar o ponto: " + e.getMessage());
        }
    }
	
	public static void buscarPontoPorDistancia(double longitude, double latitude, double distancia) {
	    String sql = "SELECT id, nome, ST_X(coordenadas) AS longitude, ST_Y(coordenadas) AS latitude " +
	                 "FROM locais WHERE ST_DWithin(coordenadas, ST_SetSRID(ST_MakePoint(?, ?), 4326), ?)";
	    // Distância em metros

	    try (Connection conn = EscolaDao.conectar();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        // Definindo os parâmetros para o SQL
	        stmt.setDouble(1, longitude);  // Longitude fornecida
	        stmt.setDouble(2, latitude);   // Latitude fornecida
	        stmt.setDouble(3, distancia / 1000);  // Distância em metros (convertida para quilômetros)

	        // Executando a consulta
	        try (ResultSet rs = stmt.executeQuery()) {
	            boolean encontrouResultados = false; // Flag para verificar se algum local foi encontrado
	            
	            while (rs.next()) {
	                encontrouResultados = true; // Atualiza a flag ao encontrar pelo menos um resultado
	                int id = rs.getInt("id");
	                String nome = rs.getString("nome");
	                double lon = rs.getDouble("longitude");
	                double lat = rs.getDouble("latitude");
	                JOptionPane.showMessageDialog(null, 
	                    "Local encontrado: " + nome 
	                    + "\nLongitude: " + lon
	                    + "\nLatitude: " + lat 
	                    + "\nID: " + id, 
	                    "Resultado da Busca", JOptionPane.INFORMATION_MESSAGE);
	            }

	            // Caso nenhum resultado seja encontrado
	            if (!encontrouResultados) {
	                JOptionPane.showMessageDialog(null, 
	                    "Nenhum local foi encontrado na área especificada.", 
	                    "Sem Resultados", JOptionPane.WARNING_MESSAGE);
	            }
	        }
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, 
	            "Erro ao buscar os pontos: " + e.getMessage(), 
	            "Erro", JOptionPane.ERROR_MESSAGE);
	        System.err.println("DAO: Erro ao buscar o ponto: " + e.getMessage());
	    }
	}

	
	
	public static void buscarPontosPorPoligono(String poligonoWKT) {
	    String sql = "SELECT id, nome, ST_X(coordenadas) AS longitude, ST_Y(coordenadas) AS latitude " +
	                 "FROM locais WHERE ST_Within(coordenadas, ST_GeomFromText(?, 4326))";

	    try (Connection conn = EscolaDao.conectar();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        // Passa o polígono no formato WKT como parâmetro
	        stmt.setString(1, poligonoWKT);

	        // Executa a consulta
	        try (ResultSet rs = stmt.executeQuery()) {
	            boolean encontrou = false;
	            while (rs.next()) {
	                encontrou = true;
	                int id = rs.getInt("id");
	                String nome = rs.getString("nome");
	                double longitude = rs.getDouble("longitude");
	                double latitude = rs.getDouble("latitude");
	                JOptionPane.showMessageDialog(null, "Local: " + nome 
							+ "\nLongitude: " + longitude
							+ "\nLatitude: " + latitude 
							+ "\nID: " + id);
	            }
	            if (!encontrou) {
	            	JOptionPane.showMessageDialog(null, "Nenhuma escola foi encontrada nessa área.", "Erro", JOptionPane.ERROR_MESSAGE);
	                System.out.println("DAO : Nenhum ponto encontrado dentro do polígono.");
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("DAO : Erro ao buscar pontos por polígono: " + e.getMessage());
	    }
	}
	
	public static void buscarPontosPorLinhaEProximidade(double longitude1, double latitude1, double longitude2, double latitude2, double distancia) {
	    String sql = "SELECT id, nome, ST_X(coordenadas) AS longitude, ST_Y(coordenadas) AS latitude " +
	                 "FROM locais WHERE ST_DWithin(coordenadas, ST_MakeLine(ST_SetSRID(ST_MakePoint(?, ?), 4326), ST_SetSRID(ST_MakePoint(?, ?), 4326)), ?)";
	    
	    try (Connection conn = EscolaDao.conectar();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        // Configurar os parâmetros da linha e da distância
	        stmt.setDouble(1, longitude1); // Longitude do primeiro ponto
	        stmt.setDouble(2, latitude1);  // Latitude do primeiro ponto
	        stmt.setDouble(3, longitude2); // Longitude do segundo ponto
	        stmt.setDouble(4, latitude2);  // Latitude do segundo ponto
	        stmt.setDouble(5, distancia/1000);  // Distância em metros

	        // Executar a consulta
	        try (ResultSet rs = stmt.executeQuery()) {
	            boolean encontrou = false;
	            while (rs.next()) {
	                encontrou = true;
	                int id = rs.getInt("id");
	                String nome = rs.getString("nome");
	                double lon = rs.getDouble("longitude");
	                double lat = rs.getDouble("latitude");
	                JOptionPane.showMessageDialog(null, "Local: " + nome 
							+ "\nLongitude: " + lon
							+ "\nLatitude: " + lat 
							+ "\nID: " + id);
	            }
	            if (!encontrou) {
	            	JOptionPane.showMessageDialog(null, "Nenhuma escola foi encontrada nas proximidades.", "Erro", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("DAO : Erro ao buscar pontos próximos à linha: " + e.getMessage());
	    }
	}




}
