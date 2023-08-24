package com.ezen.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ezen.member.MemberDAO;
import com.ezen.member.MemberVO;

public class MemberService {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private final String path = "/WEB-INF/views/member/";
	
	public MemberService(HttpServletRequest request, HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
		
	}

	public String exec() {
		String cmd = request.getParameter("cmd");
		String view = null;
		if (cmd.equals("login")) {
			return LoginService();
		} else if (cmd.equals("logout")) {
			return LogoutService();
		}
		return view;
		}

	private String LogoutService() {
		HttpSession session = request.getSession();
		session.invalidate();
		return "book?cmd=list";
	}

	private String LoginService() {
		String method = request.getMethod().toLowerCase();
		if(method.equals("get")) {
			return path+"login.jsp";
		} else { // post
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			// dao 객체 생성
			MemberDAO dao = MemberDAO.getInstance();
			// dao 메소드 수행 결과 받기
			MemberVO mvo = dao.login(id, pwd);
			if (mvo != null) { // 로그인 성공
				// 세션 저장
				HttpSession session = request.getSession();
				// 페이지에서 필요로 하는 정보 저장
				session.setAttribute("mvo", mvo);
				// 페이지 이동 : list
				return "book?cmd=list";
			} else { // 로그인 실패
				request.setAttribute("message", "로그인 실패, 당신 인생도 실패!");
				return path+"login.jsp";
			}		
		}
	}

}
