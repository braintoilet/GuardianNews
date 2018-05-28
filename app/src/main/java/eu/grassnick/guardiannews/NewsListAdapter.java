package eu.grassnick.guardiannews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class NewsListAdapter extends ArrayAdapter<NewsArticle> {
    private static final String TAG = "NewsListAdapter";

    public NewsListAdapter(Context context, ArrayList<NewsArticle> articles){
        super(context, 0, articles);
    }

    public void setArticles(ArrayList<NewsArticle> data) {
        this.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the Item for this position
        final NewsArticle article = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_listitem, parent, false);
        }

        TextView time = convertView.findViewById(R.id.news_timeText);
        TextView date = convertView.findViewById(R.id.news_dateText);
        TextView headline = convertView.findViewById(R.id.news_headLineText);
        TextView trailText = convertView.findViewById(R.id.news_trailText);
        TextView author = convertView.findViewById(R.id.news_authorText);

        time.setText(article.getTimeStr());
        date.setText(article.getDateStr());
        headline.setText(article.getHeadline());
        trailText.setText(article.getTrailText());
        author.setText("by " + article.getAuthor());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent to show article in browser
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(article.getArticleUrl()));
                getContext().startActivity(i);
            }
        });

        return convertView;
    }
}
