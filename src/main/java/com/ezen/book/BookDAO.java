package com.ezen.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ezen.util.JDBCUtil;

public class BookDAO {
	// 싱글톤
	private static BookDAO dao = new BookDAO();

	private BookDAO() {
	}

	public static BookDAO getInstance() {
		return dao;
	}

	// ---- booktbl crud 작업 메소드 정의
	// booktbl 모든 데이터를 읽어서 List 담아서 반환하는 메소드 정의
	public List<BookVO> getBookList() {
		List<BookVO> list = null;
		Connection conn = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 최신 등록 도서부터 가지고 오기
		String sql = "select bno,title,writer,price,publisher,regdate " + "from booktbl order by regdate desc";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {// 검색된 행이 하나이상이면
				list = new ArrayList<>();
				do {
					BookVO vo = new BookVO(rs.getInt("bno"), rs.getString("title"), rs.getString("writer"),
							rs.getInt("price"), rs.getString("publisher"), rs.getDate("regdate"));
					list.add(vo);
				} while (rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstmt, rs);
		}

		return list;
	}

	// 페이지용 booktbl의 모든 행의 개수를 반환 해주는 메소드
	public int getRowConut() {
		int result = 0;
		Connection conn = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select count(*) from booktbl";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstmt, rs);
		}
		return result;
	}

	public List<BookVO> getBookList(int page, int displayRow) {
		List<BookVO> list = null;
		Connection conn = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 최신 등록 도서부터 가지고 오기
		/*
		 * select * from ( select rownum rn, A.* from (select p from booktbl order by
		 * bno desc) A where rownum<=15 -- 10 page(현재페이지):3, displayRow(페이지당 표시 행수) : 5
		 * ) where rn>=11; -- 6 : page*displayRow-displayRow+1 : 3*5-5+1
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (");
		sb.append("select rownum rn, A.* from");
		sb.append("(select bno,title,writer,price,publisher,regdate from booktbl order by bno desc) A");
		sb.append(" where rownum<=?");
		sb.append(") where rn>=?");
		System.out.println(sb.toString());
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, page * displayRow);
			pstmt.setInt(2, page * displayRow - displayRow + 1);
			rs = pstmt.executeQuery();
			if (rs.next()) {// 검색된 행이 하나이상이면
				list = new ArrayList<>();
				do {
					BookVO vo = new BookVO(rs.getInt("bno"), rs.getString("title"), rs.getString("writer"),
							rs.getInt("price"), rs.getString("publisher"), rs.getDate("regdate"));
					list.add(vo);
				} while (rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstmt, rs);
		}

		return list;
	}

	public List<BookVO> getBookList(int page, int displayRow, String searchtype, String searchword) {
		List<BookVO> list = null;
		Connection conn = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (");
		sb.append("select rownum rn, A.* from");
		sb.append("(select bno,title,writer,price,publisher,regdate from booktbl");
		sb.append(" where ");
		sb.append(searchtype);
		sb.append(" like ? order by bno desc) A");// 최신순 정렬
		sb.append(" where rownum<=?");
		sb.append(") where rn>=?");
		System.out.println(sb.toString());
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, "%" + searchword + "%");
			pstmt.setInt(2, page * displayRow);
			pstmt.setInt(3, page * displayRow - displayRow + 1);
			rs = pstmt.executeQuery();
			if (rs.next()) {// 검색된 행이 하나이상이면
				list = new ArrayList<>();
				do {
					BookVO vo = new BookVO(rs.getInt("bno"), rs.getString("title"), rs.getString("writer"),
							rs.getInt("price"), rs.getString("publisher"), rs.getDate("regdate"));
					list.add(vo);
				} while (rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstmt, rs);
		}

		return list;
	}

	public int getRowConut(String searchtype, String searchword) {
		int result = 0;
		Connection conn = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// select count(*) from booktbl where title like '%자유%'
		String sql = "select count(*) from booktbl where " + searchtype + " like ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + searchword + "%");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstmt, rs);
		}
		return result;
	}

	// ----------- 삽입 메소드
	public int insertBook(BookVO vo) {
		int result = 0;
		Connection conn = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		String sql = "insert into booktbl(bno,title,writer,price,publisher,content,srcfilename,savefilename,savepath) "
				+ "values(book_seq.nextval,?,?,?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getWriter());
			pstmt.setInt(3, vo.getPrice());
			pstmt.setString(4, vo.getPublisher());
			pstmt.setString(5, vo.getContent());
			pstmt.setString(6, vo.getSrcfilename());
			pstmt.setString(7, vo.getSavefilename());
			pstmt.setString(8, vo.getSavepath());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstmt);
		}
		return result;
	}
	
	public BookVO getBook(int bno) {
		BookVO vo = null;
		Connection conn = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from booktbl where bno = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				vo = new BookVO(rs.getInt("bno"), rs.getString("title"), rs.getString("writer"), rs.getInt("price"), rs.getString("publisher"), rs.getString("content"), rs.getString("srcfilename"), rs.getString("savefilename"), rs.getString("savepath"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstmt, rs);
		}
		return vo;
	}
	
	public int updateBook(BookVO vo) {
		int result=0;
		Connection conn = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		//update문 2개로 작성한다.
		//원본파일명이 null이 아니면 파일관련 3개의 필드 update 하고
		//원본파일명이 null이 아니면 파일관련 3개의 필드 update 제외
		String oFileName=vo.getSrcfilename();
		String sql = null;
		if(oFileName==null) {
			sql="update booktbl set title=?,writer=?"
					+ ",price=?,publisher=?,content=? where bno=?";
		} else  {
			sql="update booktbl set title=?,writer=?"
					+ ",price=?,publisher=?,content=?,srcFilename=?"
					+ ",saveFilename=?,savepath=? where bno=?";
		}
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getWriter());
			pstmt.setInt(3, vo.getPrice());
			pstmt.setString(4, vo.getPublisher());
			pstmt.setString(5, vo.getContent());
			if (oFileName == null) {
				pstmt.setInt(6, vo.getBno());
			} else {
				pstmt.setString(6, vo.getSrcfilename());
				pstmt.setString(7, vo.getSavefilename());
				pstmt.setString(8, vo.getSavepath());
				pstmt.setInt(9, vo.getBno());
			}
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstmt);
		}
		return result;
	}

	public int deleteBook(int bno) {
		int result = 0;
		Connection conn = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		String sql = "delete from booktbl where bno=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstmt);
		}
		return result;
	}
}
