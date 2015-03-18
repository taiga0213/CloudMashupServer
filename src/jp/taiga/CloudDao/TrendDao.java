package jp.taiga.CloudDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.taiga.CloudBeans.KeywordsBean;
import jp.taiga.CloudBeans.TrendBean;

public class TrendDao {

	private Connection con;

	/**
	 * コンストラクタ
	 */
	public TrendDao() {
		ConnectionGet get = new ConnectionGet();
		con = get.getCon();
	}

	/**
	 * コンストラクタ
	 * @param con
	 */
	public TrendDao(Connection con) {
		this.con = con;
	}


	/**
	 *　全件取得
	 * @return
	 * @throws SQLException
	 */
	public List<TrendBean> getAll() throws SQLException {

		PreparedStatement select = con
				.prepareStatement("select * from trends;");

		ResultSet result = select.executeQuery();

		ArrayList<TrendBean> table = new ArrayList<TrendBean>();
		while (result.next()) {

			TrendBean record = new TrendBean();

			record.setPlace(result.getString("place"));
			record.setTrend(result.getString("trend"));

			table.add(record);
		}

		return table;
	}


	/**
	 *　レコード挿入
	 * @param newRecord
	 * @return
	 * @throws SQLException
	 */
	public int insert(TrendBean newRecord) throws SQLException {

		PreparedStatement insert = con
				.prepareStatement("insert into trends (place,trend) values (?,?)");
		insert.setString(1, newRecord.getPlace());
		insert.setString(2, newRecord.getTrend());

		return insert.executeUpdate();
	}

	/**
	 *　全件削除
	 * @return
	 * @throws SQLException
	 */
	public int delete() throws SQLException {

		PreparedStatement delete = con
				.prepareStatement("DELETE FROM trends;");
		return delete.executeUpdate();
	}

	/**
	 * 接続を閉じる
	 *
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		con.close();
	}

	/**
	 * コミット
	 *
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		con.commit();
	}

	/**
	 * ロールバック
	 *
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		con.rollback();
	}

}
