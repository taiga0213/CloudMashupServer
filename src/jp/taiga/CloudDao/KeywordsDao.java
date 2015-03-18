package jp.taiga.CloudDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.taiga.CloudBeans.KeywordsBean;

public class KeywordsDao {
	/** Connection */
	private Connection con;

	/**
	 * コンストラクタ
	 *
	 * @throws NamingException
	 * @throws SQLException
	 */
	public KeywordsDao() {
		ConnectionGet get = new ConnectionGet();
		con = get.getCon();
	}

	/**
	 * コンストラクタ
	 *
	 * @param con
	 */
	public KeywordsDao(Connection con) {
		this.con = con;
	}

	/**
	 * 全件取得する
	 *
	 * @return 全件
	 * @throws SQLException
	 */
	public List<KeywordsBean> getAll() throws SQLException {

		PreparedStatement select = con
				.prepareStatement("select * from keywords;");

		ResultSet result = select.executeQuery();

		ArrayList<KeywordsBean> table = new ArrayList<KeywordsBean>();
		while (result.next()) {

			KeywordsBean record = new KeywordsBean();

			record.setKeyword(result.getString("keyword"));

			table.add(record);
		}

		return table;
	}

	public List<KeywordsBean> getSelect(int count) throws SQLException {

		PreparedStatement select = con
				.prepareStatement("select * from keywords;");

		ResultSet result = select.executeQuery();

		ArrayList<KeywordsBean> shuffleTable = new ArrayList<KeywordsBean>();
		while (result.next()) {

			KeywordsBean record = new KeywordsBean();

			record.setKeyword(result.getString("keyword"));

			shuffleTable.add(record);
		}

		Collections.shuffle(shuffleTable);

		ArrayList<KeywordsBean> table = new ArrayList<KeywordsBean>();

		for (int i = 0; i < count; i++) {
			table.add(shuffleTable.get(i));
		}

		return table;
	}

	/**
	 * 主キーで検索
	 *
	 * @param keyword
	 * @return
	 * @throws SQLException
	 */
	public KeywordsBean getOne(String keyword) throws SQLException {

		PreparedStatement select = con
				.prepareStatement("select * from keywords where keyword = ? ");

		select.setString(1, keyword);
		ResultSet result = select.executeQuery();

		KeywordsBean record = new KeywordsBean();

		if (result.next()) {
			record.setKeyword(result.getString("keyword"));
		}

		return record;
	}

	/**
	 * 更新処理
	 *
	 * @param updateRecord
	 *            更新データ
	 * @return 影響のあった行数
	 * @throws SQLException
	 */
	public int update(KeywordsBean updateRecord) throws SQLException {

		PreparedStatement update = con
				.prepareStatement("update keywords set keyword = ? where keyword = ? ");

		update.setString(1, updateRecord.getKeyword());

		return update.executeUpdate();
	}

	/**
	 * 新規保存
	 *
	 * @param newRecord
	 *            保存データ
	 * @return 影響のあった行数
	 * @throws SQLException
	 */
	public int insert(KeywordsBean newRecord) throws SQLException {

		PreparedStatement insert = con
				.prepareStatement("insert into keywords (keyword) values (?)");
		insert.setString(1, newRecord.getKeyword());

		return insert.executeUpdate();
	}

	/**
	 * 削除処理
	 *
	 * @param languageId
	 *            削除対象
	 * @return 影響のあった行数
	 * @throws SQLException
	 */
	public int delete(String keyword) throws SQLException {

		PreparedStatement delete = con
				.prepareStatement("delete from keywords where keyword = ? ");
		delete.setString(1, keyword);
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
