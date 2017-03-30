package mx.edu.utng.ws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DetailExamWS {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private PreparedStatement ps;
	
	public DetailExamWS(){
		conectar();
	}

	private void conectar() {
		try{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/wstest"
						,"postgres", "1234");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	public int addDetailExam(DetailExam detailExam){
		int result = 0;
		if(connection!=null){
			try{
				ps=connection.prepareStatement(
						"INSERT INTO detail_exam(cve_matter, cve_content, cve_result, cve_question, answer, exam_departament, reactive)"
						+ " VALUES(?,?,?,?,?,?,?);");
				ps.setString(1, detailExam.getCveMatter());
				ps.setString(2, detailExam.getCveContent());
				ps.setString(3, detailExam.getCveResult());
				ps.setString(4, detailExam.getCveQuestion());
				ps.setString(5, detailExam.getAnswer());
				ps.setString(6, detailExam.getExamDepartament());
				ps.setString(7, detailExam.getReactive());
				result = ps.executeUpdate();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public int editDetailExam(DetailExam detailExam){
		int result=0;
		if(connection!=null){
			try{
				ps = connection.prepareStatement(
						"UPDATE detail_exam SET cve_matter=?, cve_content=?, cve_result=?, cve_question=?, answer=?, exam_departament=?,"
						+ " reactive=?"
						+ " WHERE id=?;");
				ps.setString(1, detailExam.getCveMatter());
				ps.setString(2, detailExam.getCveContent());
				ps.setString(3, detailExam.getCveResult());
				ps.setString(4, detailExam.getCveQuestion());
				ps.setString(5, detailExam.getAnswer());
				ps.setString(6, detailExam.getExamDepartament());
				ps.setString(7, detailExam.getReactive());
				ps.setInt(8, detailExam.getId());
				result = ps.executeUpdate();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public int removeDetailExam( int id){
		int result=0;
		if(connection!=null){
			try{
				ps = connection.prepareStatement(
						"DELETE FROM detail_exam WHERE id=?;");
				ps.setInt(1, id);
				result = ps.executeUpdate();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public DetailExam[] getDetailExams(){
		DetailExam[] result=null;
		List<DetailExam> detailExams = new ArrayList<DetailExam>();
		if(connection!=null){
			try{
				statement = connection.createStatement();
				resultSet = statement.executeQuery(
						"SELECT * FROM detail_exam;");
				while(resultSet.next()){
					DetailExam detailExam = new DetailExam(
							resultSet.getInt("id"),
							resultSet.getString("cve_matter"),
							resultSet.getString("cve_content"),
							resultSet.getString("cve_result"),
							resultSet.getString("cve_question"),
							resultSet.getString("answer"),
							resultSet.getString("exam_departament"),
							resultSet.getString("reactive"));
					detailExams.add(detailExam);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		result=detailExams.toArray(new DetailExam[detailExams.size()]);
		return result;
	}
	
	public DetailExam getDetailExamById(int id){
		DetailExam detailExam = null;
		if(connection!=null){
			try{
				ps = connection.prepareStatement("SELECT * FROM detail_exam WHERE id=?;");
				ps.setInt(1, id);
				resultSet = ps.executeQuery();
				while(resultSet.next()){
					detailExam = new DetailExam(
							resultSet.getInt("id"),
							resultSet.getString("cve_matter"),
							resultSet.getString("cve_content"),
							resultSet.getString("cve_result"),
							resultSet.getString("cve_question"),
							resultSet.getString("answer"),
							resultSet.getString("exam_departament"),
							resultSet.getString("reactive"));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return detailExam;
	}
	
}
