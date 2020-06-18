package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import domain.BoardVO;


public class BoardDAO {
	public static Connection getConnection() {
		Connection con = null;
	
	try {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Oracle");
		con = ds.getConnection();
	}catch (NamingException e) {
		e.printStackTrace();
	}catch(SQLException e) {
		e.printStackTrace();
	}
	return con;
	}
	//게시글 등록
	public int insertArticle(BoardVO vo) {
		int result=0;
		String sql="insert into board(bno,name,password,title,content,"
				+"attach,re_ref,re_lev,re_seq)"+ "values(board_seq.nextVal,?,?,?,?,?,board_seq.currVal,?,?)";
		try(Connection con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getTitle());
			pstmt.setString(4, vo.getContent());
			pstmt.setString(5, vo.getAttach());
			pstmt.setInt(6, 0);
			pstmt.setInt(7, 0);
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	//전체 리스트 가져오기
	//번호,제목,작성자,날짜,조회수
	public List<BoardVO> getList(){
		String sql = "select bno,title,name,regdate,readcount"+" from board order by bno desc";
		List<BoardVO> list = new ArrayList<BoardVO>();
		try(Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);) {		
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					BoardVO vo = new BoardVO();
					vo.setBno(rs.getInt(1));
					vo.setTitle(rs.getString(2));
					vo.setContent(rs.getString(3));
					vo.setRegdate(rs.getDate(4));
					vo.setReadcount(rs.getInt(5));
					list.add(vo);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return list;
	}
	//bno(pk)에 해당하는 게시글 가져오기
	//글쓴이,제목,내용,파일첨부
	public BoardVO getRow(int bno){
		BoardVO vo = null;
		String sql = "select name,title,content,attach from board where bno=?";
		
		try(Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);) {	
				pstmt.setInt(1, bno);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					vo = new BoardVO();			
					vo.setName(rs.getString(1));
					vo.setTitle(rs.getString(2));						
					vo.setContent(rs.getString(3));
					vo.setAttach(rs.getString(4));
					
				}				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return vo;
		
	}

}
