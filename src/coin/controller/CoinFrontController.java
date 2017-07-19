package coin.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import coin.model.CoinService;
import coin.model.MemberService;
import coin.model.dto.BtcDTO;
import coin.model.dto.MemberDTO;
import coin.model.dto.WalletCoinDTO;

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
			if(command != null){
				if(command.equals("memberInsert")){//회원가입
					memberInsert(request, response);
				}else if(command.equals("login")){//로그인
					memberLogin(request, response);
				}else if(request.getSession().getAttribute("name") != null){//로그인 상태에서만 가능
					if(command.equals("generic")){//미등록
						response.sendRedirect("generic.jsp");
					}else if(command.equals("logout")){//로그아웃
						memberLogout(request, response);
					}else if(command.equals("wallet")){//내지갑
						response.sendRedirect("wallet.jsp");
					}else if(command.equals("coinInfo")){//코인정보
						coinInfo(request, response);
					}else if(command.equals("coinInfoUpdate")){//내지갑
						coinInfoUpdate(request, response);
					}
				}else if(command.equals("btcAll")){// 모든 비트코인 정보 검색
					btcAll(request, response);
				}else if(command.equals("btc")){// 날짜별 비트코인 검색
					btc(request, response);
				}else if(command.equals("btcInsert")){// 비트코인 추가 등록
					btcInsert(request, response);   
				}else{
					request.setAttribute("errorMsg", "로그인 후 이용해주세요");
					request.getRequestDispatcher("showError.jsp").forward(request, response);
				}
			}else{
				response.sendRedirect("index.jsp");
			}
		}catch(Exception s){
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showError.jsp").forward(request, response);
			s.printStackTrace();
		}
	}

	//코인 정보 조회 메소드
	protected void coinInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String coinName = request.getParameter("coinName");
		try{
			HttpSession session = request.getSession();
			WalletCoinDTO coinInfo = MemberService.getCoinInfo((String)session.getAttribute("id"), coinName);
		    String coinInfoJson = "{\"name\":\""+coinInfo.getCoinName()
		                    +"\",\"amount\":\""+coinInfo.getAmount()
		                    +"\",\"price\":\""+coinInfo.getPrice()+"\"}";
		    response.getWriter().print(coinInfoJson);
		}catch(Exception s){
			s.printStackTrace();
			request.setAttribute("errorMsg", s.getMessage());
		}
	}
	
	//내지갑 수정 
	public void coinInfoUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		try {
			HttpSession session = request.getSession();
			String cname = request.getParameter("cname");
			int amount = Integer.parseInt(request.getParameter("amount"));
			int price = Integer.parseInt(request.getParameter("price"));
			MemberService.updateCoin((String)session.getAttribute("id"), cname, amount, price);
			url = "wallet.jsp";
		}catch(Exception s){
			request.setAttribute("errorMsg", s.getMessage());
		}
		request.getRequestDispatcher(url).forward(request, response);
	}

	//회원 가입 메소드
	protected void memberInsert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String email = request.getParameter("email");
		MemberDTO member = new MemberDTO(id, name, pw, email);
		try{
			boolean result = MemberService.addMember(member);
			if(result){
				request.setAttribute("member", member);
				request.setAttribute("successMsg", "가입 완료");
				url = "login.jsp";
			}else{
				request.setAttribute("errorMsg", "다시 시도하세요");
			}
		}catch(Exception s){
			s.printStackTrace();
			request.setAttribute("errorMsg", s.getMessage());
		}
		request.getRequestDispatcher(url).forward(request, response);
	}
	//로그인 메소드
	protected void memberLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		try{
			MemberDTO member = MemberService.getMember(id);
			HttpSession session = request.getSession();//세션 생성
			session.setAttribute("member", member);
			session.setAttribute("name", member.getName());
			session.setAttribute("id", member.getId());
			System.out.println("세션의 id값 : "+((MemberDTO)session.getAttribute("member")).getId());
			request.setAttribute("successMsg", "로그인 성공");
			url = "index.jsp";
		}catch(Exception s){
			s.printStackTrace();
			request.setAttribute("errorMsg", s.getMessage());
		}
		request.getRequestDispatcher(url).forward(request, response);
	}
	
	//로그아웃 메소드
	protected void memberLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "showError.jsp";
		try{
			request.getSession(true).invalidate();
			request.setAttribute("successMsg", "로그아웃 성공");
			url = "index.jsp";
		}catch(Exception s){
			s.printStackTrace();
			request.setAttribute("errorMsg", s.getMessage());
		}
		request.getRequestDispatcher(url).forward(request, response);
	}
	
	// 모든 비트코인 검색
		public void btcAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String url = "showError.jsp";
			try {
				request.setAttribute("btcAll", CoinService.getAllBtc());
				url = "coinList.jsp";
			}catch(Exception s){
				request.setAttribute("errorMsg", s.getMessage());
			}
			request.getRequestDispatcher(url).forward(request, response);
		}
		
		// 날짜에 따른 비트코인 검색 
		public void btc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String url = "showError.jsp";
			try {
				request.setAttribute("btc", CoinService.getBtc(request.getParameter("btcDay")));
				url = "btcDetail.jsp";
			}catch(Exception s){
				request.setAttribute("errorMsg", s.getMessage());
			}
			request.getRequestDispatcher(url).forward(request, response);
		}
		
		// 비트코인 데이터 삽입
		protected void btcInsert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String url = "showError.jsp";
			String d = request.getParameter("hiddenData");
			String[] data = d.split(",");
			String date = null;
			double price, volume;
			int eid;
			
			try{
				for (int i = 0; i < data.length; i++) {
					date = data[i].split("/")[0];
					price = Double.parseDouble(data[i].split("/")[1]);
					volume = Double.parseDouble(data[i].split("/")[2]);
					eid = Integer.parseInt(data[i].split("/")[3]);
					BtcDTO btc = new BtcDTO(date, price, volume, eid);
					boolean result = CoinService.addBtc(btc);
					
					if(result){
						request.setAttribute("btc", btc);
						request.setAttribute("successMsg", "삽입 완료");
					}else{
						request.setAttribute("errorMsg", "다시 시도하세요");
					}
				}
			}catch(Exception s){
				s.printStackTrace();
				request.setAttribute("errorMsg", s.getMessage());
			}
		}
//		//내지갑 수정 요구
//		public void coinInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//			String url = "showError.jsp";
//			try {
//				request.setAttribute("activist", ProbonoService.getActivist(request.getParameter("activistId")));
//				url = "activistUpdate.jsp";
//			}catch(Exception s){
//				request.setAttribute("errorMsg", s.getMessage());
//			}
//			request.getRequestDispatcher(url).forward(request, response);
//		}
//	
//		//내지갑 수정 
//		public void activistUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//			String url = "showError.jsp";
//			try {
//				String activistId = request.getParameter("activistId");
//				String major = request.getParameter("major");
//				ProbonoService.updateActivist(activistId, major);
//				request.setAttribute("activist", ProbonoService.getActivist(request.getParameter("activistId")));
//				url = "activistDetail.jsp";
//			}catch(Exception s){
//				request.setAttribute("errorMsg", s.getMessage());
//			}
//			request.getRequestDispatcher(url).forward(request, response);
//		}
		
		
		
		
		
	
//		//모두 ProbonoProject 검색 메소드
//		public void probonoProjectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//			String url = "showError.jsp";
//			try {
//				request.setAttribute("probonoProjectAll", ProbonoService.getAllProbonoProjects());
//				url = "probonoProjectList.jsp";
//			}catch(Exception s){
//				request.setAttribute("errorMsg", s.getMessage());
//			}
//			request.getRequestDispatcher(url).forward(request, response);
//		}
		
//		//???
//		//모든 재능 기부자 검색 - 검색된 데이터 출력 화면[activistList.jsp]
//		public void activistAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//			String url = "showError.jsp";
//			try {
//				request.setAttribute("activistAll", ProbonoService.getAllActivists());
//				url = "activistList.jsp";
//			}catch(Exception s){
//				request.setAttribute("errorMsg", s.getMessage());
//			}
//			request.getRequestDispatcher(url).forward(request, response);
//		}
		
//		//재능 기부자 검색 
//		public void activist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//			String url = "showError.jsp";
//			try {
//				request.setAttribute("activist", ProbonoService.getActivist(request.getParameter("activistId")));
//				url = "activistDetail.jsp";
//			}catch(Exception s){
//				request.setAttribute("errorMsg", s.getMessage());
//			}
//			request.getRequestDispatcher(url).forward(request, response);
//		}
	
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
