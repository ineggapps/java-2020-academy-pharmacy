import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.util.DBConn;

import oracle.jdbc.OracleTypes;

public class CustomerDAOImpl implements CustomerDAO {
	Connection conn = DBConn.getConnection();

	@Override
	public int checkPurchaseMask(CustomerDTO dto) {
		int qty = 0;
		// 마스크 살 수 있는 개수 조사
		CallableStatement cstmt = null;
		String sql = "{CALL checkPurchaseMask(?, ?, ?)}";
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, null);
			cstmt.setString(2, dto.getRrn());
			cstmt.registerOutParameter(3, OracleTypes.INTEGER);
			cstmt.executeUpdate();
			qty = cstmt.getInt(3);
		} catch (SQLException e) {
			// 오라클 오류 메시지 파싱
			String messages[] = e.getMessage().split(": ");
			String errorType = messages[0];
//			String errorMessage = messages[1].split("[\\r\\n]+")[0];
//			System.out.println(errorMessage);
			qty = Integer.parseInt(errorType.split("ORA")[1]);
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
		return qty;
	}

	@Override
	public int insertCustomer(CustomerDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "INSERT INTO customer(cnum, cname, rrn) VALUES(customer_seq.NEXTVAL, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getcName());
			pstmt.setString(2, dto.getRrn());
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
	public int insertSaleMask(CustomerDTO dto, int pnum, int qty) {
		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		try {
			sql = "{CALL insertSaleForMask(?,?,?,?,?)}";
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, pnum);
			cstmt.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			cstmt.setInt(3, qty);
			cstmt.setString(4, dto.getRrn());
			cstmt.setString(5, dto.getcName());
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
	public int insertSaleItem(int pnum, int qty) {
		int result = 0;
		CallableStatement cstmt = null;
		String sql = "{CALL insertSaleItem(?, ?)}";
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, pnum);
			cstmt.setInt(2, qty);
			result = cstmt.executeUpdate();
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
	public int deleteSale(int snum) {
		int result = 0;

		return result;
	}

	@Override
	public CustomerDTO readCustomer(String rrn) {
		CustomerDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT cnum, cname, rrn FROM customer WHERE rrn=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, rrn);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new CustomerDTO();
				dto.setcNum(rs.getInt("cNum"));
				dto.setRrn(rs.getString("rrn"));
				dto.setcName(rs.getString("cName"));
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

		return dto;
	}

	/////////////
	public String checkAvailability(String rrn) {
		String result = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		sql = "SELECT DECODE(MOD(SUBSTR(?,2,1),5),1,'월요일',2,'화요일',3,'수요일',4,'목요일',0,'금요일') rrn";
		sql += " FROM dual";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, rrn);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getString("rrn");
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
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}

		return result;

	}

	public String checkDate() {
		String result = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT TO_CHAR(SYSDATE, 'DAY') day  FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("day");
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
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}

		return result;

	}

	public int buyProduct(CustomerDTO dto) {
		int result = 0;

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

}
