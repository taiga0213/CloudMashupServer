package jp.taiga.CloudTwitter;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jp.taiga.CloudBeans.TrendBean;
import jp.taiga.CloudDao.TrendDao;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterListener implements ServletContextListener {

	Timer timer;
	Twitter twitter;
	CloudTwitter cloudTwitter;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// サーバ終了時実行
		timer.cancel();

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// サーバ起動時実行
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("CqfMKJpp6lquljMPasjiahLuD")
				.setOAuthConsumerSecret(
						"2bHPd13QxIBYJ2DYoKcmlYoDN5Dii2gsfKQSPOr8YQyyckNdAt")
				.setOAuthAccessToken(
						"2583348918-bGBKiFBuZUgisroaClV21vG1Hti0xABY35ggSrZ")
				.setOAuthAccessTokenSecret(
						"dpcJdG2wVUuSndEkN1E5QF4yuL52LCDdk9mRQdj8BmT3K");
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();

		cloudTwitter = new CloudTwitter();

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// 定期実行内容
				System.out.println(new Date());

				TrendDao trendDao = new TrendDao();

				//新しく取得したトレンドを取得・登録する
				List<TrendBean> trends = cloudTwitter.getTrend(twitter);
				//取得成功
				if(trends.size()!=0){
					// 古いトレンドを削除する
					try {
						trendDao.delete();
						trendDao.commit();
						System.out.println("delete");
					} catch (SQLException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}

					for (TrendBean trendBean : trends) {
						try {
							if (trendBean.getTrend().length() <= 10 && !trendBean.getTrend().matches(".*#.*")) {
								trendDao.insert(trendBean);
								trendDao.commit();
							}
						} catch (SQLException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
					}

				}else{//取得失敗
					System.out.println("---NOT GET---");
				}

				try {
					trendDao.close();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}


			}
		}, 0, 1000 * 60 * 20);// 20分毎に最新のトレンドを取得

	}

}
