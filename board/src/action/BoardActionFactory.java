package action;

public class BoardActionFactory {
	private static BoardActionFactory factory;
	
	private BoardActionFactory() {}
	public static BoardActionFactory getInstance() {
		if(factory==null) {
			factory = new BoardActionFactory();
		}
		return factory;
	}
	private Action action = null;
	public Action action(String cmd) {
		if(cmd.equals("/write.do")) {
			action = new WriteAction("/list.do");
		}else if(cmd.equals("/list.do")) {
			action = new ListAction("view/qna_board_list.jsp");
		}else if(cmd.equals("/view.do")) {
			action = new ViewAction("view/qna_board_view.jsp");
		}else if(cmd.equals("/modify.do")) {
			action = new ModifyAction("view/qna_board_modify.jsp");
		}else if(cmd.equals("/update.do")) {
			action = new UpdateAction("/view.do");
		}else if(cmd.equals("/hitupdate.do")) {
			action = new HitUpdateAction("/view.do");
		}else if(cmd.equals("/delete.do")) {
			action = new DeleteAction("/list.do");		
		}else if(cmd.equals("/replyView.do")) {
			action = new ReplyViewAction("view/qna_board_reply.jsp");
		}else if(cmd.equals("/reply.do")) {
			action = new ReplyAction("/list.do");
		}else if(cmd.equals("/search.do")) {
			action = new SearchAction("view/qna_board_list.jsp");
		}
		return action;
	}

}
