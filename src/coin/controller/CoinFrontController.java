package coin.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coin.model.MemberService;
import coin.model.dto.MemberDTO;

public class CoinFrontController extends HttpServlet {
	public CoinFrontController() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("euc-kr");
		String command = request.getParameter("command");
		try{
			if(command.equals("memberInsert")){//회원가입
				memberInsert(request, response);
			}
//			else if(command.equals("activistAll")){//모든 재능 기부자 검색
//				activistAll(request, response);
//			}else if(command.equals("activist")){//특정 재능 기부자 정보 검색
//				activist(request, response);
//			}else if(command.equals("activistInsert")){//재능 기부자 추가 등록
//				activistInsert(request, response);
//			}else if(command.equals("activistUpdateReq")){//재능 기부자 정보 수정요청
//				activistUpdateReq(request, response);
//			}else if(command.equals("activistUpdate")){//재능 기부자 정보 수정
//				activistUpdate(request, response);
//			}else if(command.equals("activistDelete")){//재능 기부자 탈퇴[삭제]
//				activistDelete(request, response);
//			}
		}catch(Exception s){
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showError.jsp").forward(request, response);
			s.printStackTrace();
		}
	}
	

//	//모두 ProbonoProject 검색 메소드
//	public void probonoProjectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String url = "showError.jsp";
//		try {
//			request.setAttribute("probonoProjectAll", ProbonoService.getAllProbonoProjects());
//			url = "probonoProjectList.jsp";
//		}catch(Exception s){
//			request.setAttribute("errorMsg", s.getMessage());
//		}
//		request.getRequestDispatcher(url).forward(request, response);
//	}
//	
//	//???
//	//모든 재능 기부자 검색 - 검색된 데이터 출력 화면[activistList.jsp]
//	public void activistAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String url = "showError.jsp";
//		try {
//			request.setAttribute("activistAll", ProbonoService.getAllActivists());
//			url = "activistList.jsp";
//		}catch(Exception s){
//			request.setAttribute("errorMsg", s.getMessage());
//		}
//		request.getRequestDispatcher(url).forward(request, response);
//	}
//	
//	//재능 기부자 검색 
//	public void activist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String url = "showError.jsp";
//		try {
//			request.setAttribute("activist", ProbonoService.getActivist(request.getParameter("activistId")));
//			url = "activistDetail.jsp";
//		}catch(Exception s){
//			request.setAttribute("errorMsg", s.getMessage());
//		}
//		request.getRequestDispatcher(url).forward(request, response);
//	}
//	

	//재능 기부자 가입 메소드
	protected void memberInsert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String major = request.getParameter("email");
		
		MemberDTO member = new MemberDTO(id, name, pw, major);
		try{
			boolean result = MemberService.addMember(member);
			if(result){
				request.setAttribute("member", member);
				request.setAttribute("successMsg", "가입 완료");
				url = "login.html";
			}else{
				request.setAttribute("errorMsg", "다시 시도하세요");
			}
		}catch(Exception s){
			s.printStackTrace();
			request.setAttribute("errorMsg", s.getMessage());
		}
		request.getRequestDispatcher(url).forward(request, response);
	}
	
//	//재능 기부자 수정 요구
//	public void activistUpdateReq(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String url = "showError.jsp";
//		try {
//			request.setAttribute("activist", ProbonoService.getActivist(request.getParameter("activistId")));
//			url = "activistUpdate.jsp";
//		}catch(Exception s){
//			request.setAttribute("errorMsg", s.getMessage());
//		}
//		request.getRequestDispatcher(url).forward(request, response);
//	}
//
//	//???
//	//재능 기부자 수정 - 상세정보 확인 jsp[activistDetail.jsp]
//	public void activistUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String url = "showError.jsp";
//		try {
//			String activistId = request.getParameter("activistId");
//			String major = request.getParameter("major");
//			ProbonoService.updateActivist(activistId, major);
//			request.setAttribute("activist", ProbonoService.getActivist(request.getParameter("activistId")));
//			url = "activistDetail.jsp";
//		}catch(Exception s){
//			request.setAttribute("errorMsg", s.getMessage());
//		}
//		request.getRequestDispatcher(url).forward(request, response);
//	}
//	
//	//???
//	//재능 기부자 삭제
//	public void activistDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String url = "showError.jsp";
//		try {
//			String activistId = request.getParameter("activistId");
//			if(ProbonoService.deleteActivist(activistId)){
//				request.setAttribute("activistAll", ProbonoService.getAllActivists());
//				url = "activistList.jsp";
//			}else{
//				request.setAttribute("errorMsg", "재 실행 해 주세요");
//			}
//		}catch(Exception s){
//			request.setAttribute("errorMsg", "진행중인 Probono Project가 있어 지금은 탈퇴가 불가합니다");
//		}
//		request.getRequestDispatcher(url).forward(request, response);
//	}
//
}
