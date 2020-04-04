import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.util.DBConn;

public class CustomerDAOImpl implements CustomerDAO {
	Connection conn = DBConn.getConnection();

	@Override
	public int insertSale(int pnum, String date, int qty, int cnum) {
		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		try {
			sql = "{CALL insertSale(?,?,?,?)}";
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, pnum);
			cstmt.setString(2, date);
			cstmt.setInt(3, qty);
			cstmt.setInt(4, cnum);
			cstmt.executeUpdate();
			result = 1;
		} catch (SQLException e) {
			//오라클 오류 메시지 파싱
			String messages[] = e.getMessage().split(": ");
			String errorType = messages[0];
			String errorMessage = messages[1].split("[\\r\\n]+")[0];
			System.out.println(errorType + ":" + errorMessage);
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

}
