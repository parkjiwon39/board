package action;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.BoardDAO;

public class DeleteAction implements Action {
	private String path;
	
	public DeleteAction(String path) {
		super();
		this.path = path;
	}

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//bno,password가져오기
		int bno = Integer.parseInt(req.getParameter("bno"));
		String password = req.getParameter("password");
		
		//페이지 나누기 후 추가
		String page = req.getParameter("page");		
		String criteria = req.getParameter("criteria");
		String keyword = URLEncoder.encode(req.getParameter("keyword"),"utf-8");
		
		//DB작업
		BoardDAO dao = new BoardDAO();
		int result = dao.deleteArticle(bno, password);
		if(result==0) {
			path="view/qna_board_pwdCheck.jsp?bno="+bno+"&page"+page+"&criteria="+criteria+"&keyword="+keyword;
		}else {
			path+="?page="+page+"&criteria="+criteria+"&keyword="+keyword;
		}
		return new ActionForward(path,true);
	}

}
