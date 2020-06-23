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
import domain.SearchVO;

public class BoardDAO {
	public static Connection getConnection() {		
		Connection con = null;		
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Oracle");
			con = ds.getConnection();
		} catch (NamingException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return con;
	}
	
	//게시글 등록
	public int insertArticle(BoardVO vo) {
		int result=0;
		String sql = "insert into board(bno,name,password,title,content,"
				+ "attach,re_ref,re_lev,re_seq) "
				+ "values(board_seq.nextVal,?,?,?,?,?,board_seq.currVal,?,?)";
		
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
	// 번호,제목,작성자,날짜,조회수
	public List<BoardVO> getList(SearchVO search){
		
		int start = search.getPage()*search.getAmount();
		int limit = (search.getPage()-1)*search.getAmount();
		List<BoardVO> list = new ArrayList<BoardVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			StringBuilder sql = new StringBuilder();
			
			if(!search.getCriteria().isEmpty()) {			
				sql.append("select bno,title,name,regdate,readcount,re_lev");
				sql.append(" from (select rownum rn,A.* from (");
				sql.append(" select bno,title,name,regdate,readcount,re_lev ");
				sql.append(" from board where "+search.getCriteria()+" like ? ");
				sql.append(" order by re_ref desc, re_seq asc) A");
				sql.append(" where rownum <= ? )");
				sql.append(" where rn > ?");				
				pstmt = con.prepareStatement(sql.toString()); 
				pstmt.setString(1, "%"+search.getKeyword()+"%");	
				pstmt.setInt(2, start);
				pstmt.setInt(3, limit);		
			}else {
				sql.append("select bno,title,name,regdate,readcount,re_lev");
				sql.append(" from (select rownum rn,A.* from (");
				sql.append(" select bno,title,name,regdate,readcount,re_lev ");
				sql.append(" from board order by re_ref desc, re_seq asc) A");
				sql.append(" where rownum <= ? )");
				sql.append(" where rn > ?");
				pstmt = con.prepareStatement(sql.toString()); 					
				pstmt.setInt(1, start);
				pstmt.setInt(2, limit);	
			}			
			rs = pstmt.executeQuery();			
			while(rs.next()) {
				BoardVO vo = new BoardVO();
				vo.setBno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setReadcount(rs.getInt(5));
				vo.setRe_lev(rs.getInt(6));
				list.add(vo);						
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//전체 행 수 가져오기
	public int totalRows(String criteria,String keyword) {
		
		int total = 0;
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			 con = getConnection();
			 if(!criteria.isEmpty()) {
				 //검색 리스트를 요청할 때
				 String sql1 = "select count(*) from board where "+criteria+" like ?";
				 pstmt = con.prepareStatement(sql1);
				 pstmt.setString(1, "%"+keyword+"%");
			 }else {
				 //일반 리스트를 요청할 때
				 String sql	= "select count(*) from board";
				 pstmt = con.prepareStatement(sql);
			 }
			 ResultSet rs = pstmt.executeQuery();
			 if(rs.next()) {
				 total = rs.getInt(1);
			 }			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(!pstmt.isClosed()) pstmt.close();
				if(!con.isClosed()) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return total;
	}
	
	
	
	
	
	//bno(pk) 에 해당하는 게시글 가져오기
	//글쓴이,제목,내용,파일첨부
	public BoardVO getRow(int bno){
		
		BoardVO vo=null;
		String sql = "select bno,name,title,content,attach,re_ref,re_lev,re_seq from board where bno=?";
		
		try(Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, bno);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				vo = new BoardVO();
				vo.setBno(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setTitle(rs.getString(3));
				vo.setContent(rs.getString(4));
				vo.setAttach(rs.getString(5));
				//댓글 작업으로 인해 추가
				vo.setRe_ref(rs.getInt(6));
				vo.setRe_lev(rs.getInt(7));
				vo.setRe_seq(rs.getInt(8));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;		
	}
	
	//게시글 수정
	public int updateRow(BoardVO vo) {
		int result=0;
		PreparedStatement pstmt = null;
		try(Connection con = getConnection()) {
			String sql="";
			if(vo.getAttach()!=null) {
				sql="update board set content=?, title=?, attach=?"
						+ " where bno=? and password=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, vo.getContent());
				pstmt.setString(2, vo.getTitle());
				pstmt.setString(3, vo.getAttach());
				pstmt.setInt(4, vo.getBno());		
				pstmt.setString(5, vo.getPassword());
			}else {			
				sql="update board set content=?, title=? where bno=? and password=?";			
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, vo.getContent());
				pstmt.setString(2, vo.getTitle());				
				pstmt.setInt(3, vo.getBno());
				pstmt.setString(4, vo.getPassword());
			}			
			result=pstmt.executeUpdate();			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null)
				try {
					pstmt.close();
				} catch (SQLException e) {					
					e.printStackTrace();
				}
		}				
		return result;
	}
	
	//조회수 업데이트
	// update board set readcount=readcount+1 where bno=?
	public int hitUpdate(int bno) {
		String sql ="update board set readcount=readcount+1 where bno=?";
		int result=0;
		try(Connection con=getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setInt(1, bno);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int deleteArticle(int bno,String password) {
		String sql = "delete from board where bno=? and password=?";
		int result=0;
		try(Connection con=getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setInt(1, bno);
			pstmt.setString(2, password);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;		
	}
	
	//댓글처리
	public int replyAction(BoardVO vo) {
		int result=0;
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			con = getConnection();
			
			//댓글 작업을 위한 원본글의 re_ref,re_lev,re_lev 가져오기
			int re_ref = vo.getRe_ref();
			int re_seq = vo.getRe_seq();
			int re_lev = vo.getRe_lev();			
			
			
			//댓글 삽입하기 전 현재 원본글에 달려있는
			//댓글들의 re_seq 값 변경하기
			String sql="update board set re_seq=re_seq+1 ";
			sql+="where re_ref=? and re_seq>?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, re_ref);
			pstmt.setInt(2, re_seq);
			result = pstmt.executeUpdate();
			if(!pstmt.isClosed()) {
				pstmt.close();
			}
			
			//댓글 insert 작업하기
			sql = "insert into board(bno,name,password,title,content,"
					+ "attach,re_ref,re_lev,re_seq) "
					+ "values(board_seq.nextVal,?,?,?,?,?,?,?,?)";
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getTitle());
			pstmt.setString(4, vo.getContent());
			pstmt.setString(5, vo.getAttach());
			pstmt.setInt(6, re_ref); //원본글의 re_ref와 동일
			pstmt.setInt(7, re_lev+1); //원본글의 re_lev+1
			pstmt.setInt(8, re_seq+1); //원본글의 re_seq+1
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(!pstmt.isClosed()) {
					pstmt.close();
				}
				if(!con.isClosed()) {
					con.close();
				}				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}		
		return result;
	}
	
	//search
	// select * from board where name like '%홍길동%'
	// select * from board where title like '%홍길동'
	// select * from board where content like '%홍길동%'
	public List<BoardVO> getSearchList(String criteria,String keyword){
		String sql = "select bno,title,name,regdate,readcount,re_lev "
				+ "from board where "+criteria+" like ?"
				+ " order by re_ref desc, re_seq asc";
		List<BoardVO> list = new ArrayList<BoardVO>();
		try(Connection con = getConnection();
				PreparedStatement pstmt=con.prepareStatement(sql)) {
			
			pstmt.setString(1, "%"+keyword+"%");
			
			ResultSet rs = pstmt.executeQuery();			
			while(rs.next()) {
				BoardVO vo = new BoardVO();
				vo.setBno(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setRegdate(rs.getDate(4));
				vo.setReadcount(rs.getInt(5));
				vo.setRe_lev(rs.getInt(6));
				list.add(vo);						
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}



















