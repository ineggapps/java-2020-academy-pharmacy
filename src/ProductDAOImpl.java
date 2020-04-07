
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

import oracle.jdbc.OracleTypes;

public class ProductDAOImpl implements ProductDAO {
	private Connection conn = DBConn.getConnection();

//입고(제품추가)
	@Override
	public int insertProduct(InputDTO dto) {
		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "{CALL insertInput (?,?,?,?)}";
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, dto.getPnum());
			cstmt.setString(2, dto.getIdate());
			cstmt.setInt(3, dto.getIqty());
			cstmt.registerOutParameter(4, OracleTypes.INTEGER);
			cstmt.executeUpdate();
			result = cstmt.getInt(4);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null)
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
		}
		return result;
	}

//제품수정
	@Override
	public int updateProduct(InputDTO dto) {
		CallableStatement cstmt = null;
		int result = 0;
		String sql;

		try {
			sql = "{CALL updateInput (?,?,?,?)}";
			cstmt = conn.prepareCall(sql);

			cstmt.setInt(1, dto.getInum());
			cstmt.setInt(2, dto.getPnum());
			cstmt.setString(3, dto.getIdate());
			cstmt.setInt(4, dto.getIqty());

			cstmt.executeUpdate();
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return result;
	}

//제품삭제
	@Override
	public int deleteProduct(int inum) {
		CallableStatement cstmt = null;
		String sql;
		int result = 0;

		try {
			sql = "{CALL deleteInput (?)}";
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, inum);
			cstmt.executeUpdate();
			result = 1;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

//재고리스트(남은재고안내)
	@Override
	public List<ProductDTO> listStock() {
		List<ProductDTO> list = new ArrayList<ProductDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT pnum, pname, stock, price FROM product");
			sb.append(" ORDER BY pnum");
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProductDTO dto = new ProductDTO();
				dto.setPnum(rs.getInt("pnum"));
				dto.setPname(rs.getString("pname"));
				dto.setStock(rs.getInt("stock"));
				dto.setPrice(rs.getInt("price"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return list;
	}

//손님별 리스트
	@Override
	public List<SaleSumDTO> listCustomer(String rrn) {
		List<SaleSumDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT cName,TO_CHAR(sDate,'yyyy-mm-dd')sDate,pName,SUM(sQty) sQty");
			sb.append(" FROM sale s");
			sb.append(" JOIN product p ON s.pNum=p.pNum");
			sb.append(" JOIN customer c ON s.cNum=c.cNum");
			sb.append(" WHERE INSTR(rrn,?)>=1");
			sb.append(" GROUP BY cName,TO_CHAR(sDate,'yyyy-mm-dd'),pName");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, rrn);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SaleSumDTO dto = new SaleSumDTO();

				dto.setcName(rs.getString("cName"));
				dto.setSdate(rs.getString("Sdate"));
				dto.setpName(rs.getString("pName"));
				dto.setsQty(rs.getInt("sQty"));

				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}

//총 판매리스트
	@Override
	public List<SaleSumDTO> listProduct() {
		List<SaleSumDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT pName,p.pNum,SUM(sQty) sQty,");
		sb.append("TO_CHAR(sDate,'yyyy-mm-dd')sDate");
		sb.append(" FROM sale s");
		sb.append(" JOIN product p ON s.pNum=p.pNum");
		sb.append(" GROUP BY pName,p.pNum,TO_CHAR(sDate,'yyyy-mm-dd')");

		try {
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SaleSumDTO dto = new SaleSumDTO();

				dto.setpName(rs.getString("pname"));
				dto.setpNum(rs.getInt("pnum"));
				dto.setsQty(rs.getInt("sQty"));
				dto.setSdate(rs.getString("sDate"));

				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}

	@Override
	public List<ProductDTO> searchKeyword(String keyword) {
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		List<ProductDTO> list = new ArrayList<ProductDTO>();
		try {
			sql = "select p.pnum, pname, price, stock from product p" + " "
					+ "JOIN product_keyword k ON k.pnum = p.pnum" + " " + "WHERE keyword=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProductDTO dto = new ProductDTO();
				dto.setPnum(rs.getInt("pnum"));
				dto.setPname(rs.getString("pname"));
				dto.setPrice(rs.getInt("price"));
				dto.setStock(rs.getInt("stock"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}

	@Override
	public int insertSale(int pnum, int qty) {
		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		try {
			sql = "{CALL insertSaleItem(?,?)}";// pnum, qty만 보내기
			cstmt = conn.prepareCall(sql);
			cstmt.setObject(1, pnum);
			cstmt.setInt(2, qty);
			cstmt.executeUpdate();
			result = 1;
		} catch (SQLException e) {
			// 오라클 오류 메시지 파싱
			String messages[] = e.getMessage().split(": ");
			String errorType = messages[0];
			String errorMessage = messages[1].split("[\\r\\n]+")[0];
			System.out.println(errorType + ":" + errorMessage);
			System.out.println();
			System.out.println(e.getMessage());
//			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

	@Override
	public List<String> getKeywords() {
		List<String> list = new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT DISTINCT keyword from product_keyword";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("keyword"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}

	@Override
	public List<ProductKeywordDTO> listByKeyword(String keyword) {
		List<ProductKeywordDTO> list = new ArrayList<ProductKeywordDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "select keyword, p.pnum, pname, price, stock from product_keyword k" + " "
					+ "JOIN product p ON k.pnum = p.pnum" + " " + "WHERE keyword=? ORDER BY keyword, stock desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ProductKeywordDTO dto = new ProductKeywordDTO();
				dto.setKeyword(rs.getNString("keyword"));
				ProductDTO product = new ProductDTO();
				product.setPnum(rs.getInt("pnum"));
				product.setPname(rs.getString("pname"));
				product.setPrice(rs.getInt("price"));
				product.setStock(rs.getInt("stock"));
				dto.setProduct(product);
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}

	@Override
	public int insertKeyword(int pnum, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "INSERT INTO product_keyword (pnum, keyword) VALUES(?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pnum);
			pstmt.setString(2, keyword);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

	@Override
	public int deleteKeywordProduct(int pnum, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "DELETE FROM product_keyword WHERE pnum=? and keyword=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pnum);
			pstmt.setString(2, keyword);
			pstmt.executeUpdate();
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

}
