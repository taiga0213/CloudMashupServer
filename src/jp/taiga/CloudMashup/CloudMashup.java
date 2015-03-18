package jp.taiga.CloudMashup;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import jp.taiga.CloudBeans.JSONBean;
import jp.taiga.CloudBeans.KeywordsBean;
import jp.taiga.CloudBeans.TrendBean;
import jp.taiga.CloudDao.KeywordsDao;
import jp.taiga.CloudDao.TrendDao;
import jp.taiga.CloudTwitter.CloudTwitter;
import net.arnx.jsonic.JSON;

/**
 * Servlet implementation class CloudMashup
 */
@WebServlet("/CloudMashup")
public class CloudMashup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int COUNT = 100;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CloudMashup() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		access(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		access(request, response);
	}

	public void access(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		try {

			KeywordsDao keywordsDao = new KeywordsDao();
			// 全件取得
			List<KeywordsBean> selectResult = keywordsDao.getAll();

			// 指定件数取得
			// List<KeywordsBean> selectResult = keywordsDao.getSelect(COUNT);

			// 送信用JSONの作成
			// 送信JSON形式
			//{
			//	  "keywordsBean":[
			//	      {"keyword":"ワード"},
			//	      {"keyword":"ワード"},
			//  	  {"keyword":"ワード"}
			//　　　　],
			//  	"trendbean":[
			//	      {"place":"場所","trend":"ワード"},
			//	      {"place":"場所","trend":"ワード"},
			//	      {"place":"場所","trend":"ワード"}
			//	　  ]
			//}
			JSONBean responseJsonBean = new JSONBean();


			KeywordsBean[] selectResultTable = (KeywordsBean[]) selectResult
					.toArray(new KeywordsBean[0]);

			responseJsonBean.setKeywordsBean(selectResultTable);

			// TODO Too　Many request対策
			// トレンドワードの取得
			// Twitter

			TrendDao trendDao = new TrendDao();
			TrendBean[] respnceTrends = (TrendBean[])trendDao.getAll().toArray(new TrendBean[0]);
			trendDao.close();

			responseJsonBean.setTrendBean(respnceTrends);

			String responseJson = JSON.encode(responseJsonBean);

			System.out.println("送信："+responseJson);

			String encodeJson = URLEncoder.encode(responseJson, "UTF-8");

			// 送信用JSON出力
			out.println(encodeJson);

			// JSON受信処理
			if (request.getParameter("json") != null) {
				byte[] bytes = URLDecoder.decode(request.getParameter("json"),
						"UTF-8").getBytes("ISO-8859-1");
				String reqestJson = new String(bytes, "UTF-8");
				System.out.println("受信："+reqestJson);

				JSONBean reqestJsonBean = JSON.decode(reqestJson,
						JSONBean.class);
				KeywordsBean[] keywordsBean = reqestJsonBean.getKeywordsBean();

				for (KeywordsBean keywords : keywordsBean) {
					try {
						keywordsDao.insert(keywords);
						keywordsDao.commit();
					} catch (Exception e) {
						// e.printStackTrace();
						keywordsDao.rollback();
					}
				}

			}

			keywordsDao.close();

		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}
}
