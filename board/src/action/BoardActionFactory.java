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
		}
		return action;
	}

}
