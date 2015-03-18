/**
 *
 */
package jp.taiga.CloudTwitter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.taiga.CloudBeans.TrendBean;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * @author Taiga
 *
 */
public class CloudTwitter {

	public List<TrendBean> getTrend(Twitter twitter) {
		ArrayList<TrendBean> trends = new ArrayList<TrendBean>();
		try {
			Trends localTrends;

			String[][] placeIdList = getPlaceIdList();

			for (String[] placeId : placeIdList) {
				localTrends = twitter.getPlaceTrends(Integer
						.parseInt(placeId[1]));
				for (Trend trend : localTrends.getTrends()) {
					TrendBean trendBean = new TrendBean();
					trendBean.setPlace(placeId[0]);
					trendBean.setTrend(trend.getName());

					trends.add(trendBean);

//					System.out
//							.println("【" + placeId[0] + "】" + trend.getName());
				}
			}
			System.out.println("-------------------GET----------------------");
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			System.out.println("-------------------ERR----------------------");
		}

		return trends;
	}

	/**
	 * トレンドワード取得用WOEID_ID一覧取得
	 *
	 * @return Twitterのトレンドワードが取得できる場所とそのIDの配列<br/>
	 *         placeIdList[x][0] = 場所名<br/>
	 *         placeIdList[x][1] = ID
	 */
	public String[][] getPlaceIdList() {
		String[][] placeIdList = new String[15][2];

		String[] place = new String[] { "北九州", "埼玉", "千葉", "福岡", "広島", "川崎",
				"神戸", "熊本", "名古屋", "新潟", "札幌", "仙台", "高松", "東京", "横浜" };// 排除-相模原-浜松

		String[] id = new String[] { "1110809", "1116753", "1117034",
				"1117099", "1117227", "1117502", "1117545", "1117605",
				"1117817", "1117881", "1118108", "1118129", "1118285",
				"1118370", "1118550", };// 相模原-1118072　浜松-1117155

//		String[][] placeIdList = new String[2][2];
//		String[] place = new String[] { "北九州", "埼玉"};// 排除-相模原-浜松
//
//		String[] id = new String[] { "1110809", "1116753"};// 相模原-1118072　浜松-1117155

		for (int i = 0; i < id.length; i++) {
			placeIdList[i][0] = place[i];
			placeIdList[i][1] = id[i];
		}

		return placeIdList;

	}
}
