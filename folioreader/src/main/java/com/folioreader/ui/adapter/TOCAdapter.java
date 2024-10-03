package com.folioreader.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.folioreader.Config;
import com.folioreader.R;
import com.folioreader.model.TOCLinkWrapper;

import java.util.ArrayList;

/**
 * Created by mahavir on 3/10/17.
 */

public class TOCAdapter extends RecyclerView.Adapter<TOCAdapter.TOCRowViewHolder> {

    private static final int LEVEL_ONE_PADDING_PIXEL = 15;

    private TOCCallback callback;
    private final Context mContext;
    private String selectedHref;
    private Config mConfig;
    private ArrayList<TOCLinkWrapper> tocLinkWrappers = new ArrayList<TOCLinkWrapper>();

    public TOCAdapter(Context context, ArrayList<TOCLinkWrapper> tocLinkWrappers, String selectedHref, Config config) {
        mContext = context;
        this.selectedHref = selectedHref;
        this.mConfig = config;
        this.tocLinkWrappers = tocLinkWrappers;
    }

    public void setCallback(TOCCallback callback) {
        this.callback = callback;
    }

    @Override
    public TOCAdapter.TOCRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TOCRowViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_table_of_contents, parent, false));
    }

    public TOCLinkWrapper getItemAt(int position) {
        return tocLinkWrappers.get(position);
    }

    @Override
    public void onBindViewHolder(TOCAdapter.TOCRowViewHolder viewHolder, int position) {
        TOCLinkWrapper tocLinkWrapper = tocLinkWrappers.get(position);

        viewHolder.sectionTitle.setText(tocLinkWrapper.getTocLink().getTitle());
        if (tocLinkWrapper.getTocLink().getHref().equals(selectedHref)) {
            viewHolder.sectionTitle.setTextColor(Color.parseColor("#F9A02B"));
        } else {
            viewHolder.sectionTitle.setTextColor(Color.parseColor("#767676"));
        }
        boolean isHaveNextLevel = false;
        for (int i = 0; i < tocLinkWrappers.size(); i++) {
            if (1 == tocLinkWrappers.get(i).getIndentation()) {
                isHaveNextLevel = true;
                break;
            }
        }
        if (tocLinkWrapper.getIndentation() == 0 && isHaveNextLevel) {
            viewHolder.view.setBackgroundColor(Color.parseColor("#C2C6CC"));
        } else {
            viewHolder.view.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return tocLinkWrappers.size();
    }

    public interface TOCCallback {
        void onTocClicked(int position);

        void onExpanded(int position);
    }

    public class TOCRowViewHolder extends RecyclerView.ViewHolder {
        public ImageView children;
        TextView sectionTitle;
        private LinearLayout container;
        private View view;

        TOCRowViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            children = (ImageView) itemView.findViewById(R.id.children);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            sectionTitle = (TextView) itemView.findViewById(R.id.section_title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) callback.onTocClicked(getAdapterPosition());
                }
            });
        }
    }
}
