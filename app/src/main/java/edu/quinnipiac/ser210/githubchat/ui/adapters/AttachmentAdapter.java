package edu.quinnipiac.ser210.githubchat.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachment;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubAttachable;
import edu.quinnipiac.ser210.githubchat.ui.util.ImageLoader;

public class AttachmentAdapter extends ArrayAdapter<GithubAttachment> implements OnFetchGithubAttachable {

    private int channelGithubAttachable;

    private final List<GithubAttachment> githubAttachments;

    public AttachmentAdapter(Context context) {
        this(context, R.layout.list_attachment_item, new LinkedList<>());
    }

    protected AttachmentAdapter(@NonNull Context context, int resource, @NonNull List<GithubAttachment> objects) {
        super(context, resource, objects);
        this.githubAttachments = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder = null;
        final GithubAttachment attachment = githubAttachments.get(position);


        if(convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_attachment_item,parent,false);
            viewHolder.iconView = convertView.findViewById(R.id.list_attachment_imageview_icon);
            viewHolder.numberView = convertView.findViewById(R.id.list_attachment_textview_number);
            viewHolder.titleView = convertView.findViewById(R.id.list_attachment_textview_title);
            convertView.setOnClickListener((v) -> onAttachmentClicked(attachment));

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.iconView.setImageResource(attachment.getStatusDrawable());
        viewHolder.numberView.setText(MessageFormat.format(" #{0}: ", attachment.getNumber()));
        viewHolder.titleView.setText(attachment.getTitle());

        return convertView;

    }

    public void resetListView(int channel) {
        channelGithubAttachable = channel;

        githubAttachments.clear();
        notifyDataSetChanged();
    }

    private void onAttachmentClicked(GithubAttachment attachment) {
        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(attachment.getURL())));
    }

    @Override
    public synchronized void onFetchGithubAttachable(GithubAttachment attachable, int channel) {
        if(channel == channelGithubAttachable) {
            githubAttachments.add(attachable);
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder {


        private ImageView iconView;
        private TextView numberView;
        private TextView titleView;

    }
}
