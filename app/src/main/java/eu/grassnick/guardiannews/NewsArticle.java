package eu.grassnick.guardiannews;

public class NewsArticle {
    private static final String TAG = "NewsArticle";

    private String mTimeStr;
    private String mDateStr;
    private String mHeadline;
    private String mTrailText;
    private String mArticleUrl;
    private String mAuthor;
    private String mSectionName;

    public NewsArticle(String time, String date, String headline, String trailText, String articleUrl, String author, String sectionName) {
        mTimeStr = time;
        mDateStr = date;
        mHeadline = headline;
        mTrailText = trailText;
        mArticleUrl = articleUrl;
        mAuthor = author;
        mSectionName = sectionName;
    }

    public String getTimeStr() {
        return mTimeStr;
    }

    public String getDateStr() {
        return mDateStr;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getTrailText() {
        return mTrailText;
    }

    public String getArticleUrl() {
        return mArticleUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getSectionName() {
        return mSectionName;
    }
}
