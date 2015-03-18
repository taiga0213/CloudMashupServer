package jp.taiga.CloudBeans;

public class JSONBean {
//	private String[] words;
//	private String[] trend;
	private TrendBean[] trendBean;
	private KeywordsBean[] keywordsBean;

//	public String[] getWords() {
//		return words;
//	}
//	public void setWords(String[] words) {
//		this.words = words;
//	}
//	public String[] getTrend() {
//		return trend;
//	}
//	public void setTrend(String[] trend) {
//		this.trend = trend;
//	}
	public TrendBean[] getTrendBean() {
		return trendBean;
	}
	public void setTrendBean(TrendBean[] respnceTrends) {
		this.trendBean = respnceTrends;
	}
	public KeywordsBean[] getKeywordsBean() {
		return keywordsBean;
	}
	public void setKeywordsBean(KeywordsBean[] keywordsBean) {
		this.keywordsBean = keywordsBean;
	}
}
