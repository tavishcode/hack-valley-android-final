package com.kevalpatel2106.sample;

/**
 * Created by tavish on 2/24/18.
 */

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.content.SharedPreferences;
        import android.database.Cursor;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import org.json.JSONObject;

        import java.util.ArrayList;

        import com.kevalpatel2106.sample.R;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //private Cursor messages;
    private ArrayList<String> messages;
    private String myFacebookId;

    public MessagesRecyclerAdapter(/*Cursor*/ArrayList<String> res, String fbId){
        myFacebookId = fbId;
        messages = res;
    }

    public void reassignCursor(/*Cursor*/ArrayList<String> res){
        messages = res;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch(viewType) {

            case 0: return new MessageViewHolderMyMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_message_item, parent, false));

            default: return new MessageViewHolderOtherMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.other_message_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //messages.moveToPosition(position);
        String msg = messages.get(position);

        switch(getItemViewType(position)){

            case 0:
                MessageViewHolderMyMessage mHolder = (MessageViewHolderMyMessage) holder;
                mHolder.mMessage.setText(/*messages.getString(3)*/msg);
                break;

            case 1:
                MessageViewHolderOtherMessage oHolder = (MessageViewHolderOtherMessage) holder;
                oHolder.otherMessage.setText(/*messages.getString(3)*/msg);
        }
    }

    @Override
    public int getItemCount() {
        return /*messages.getCount()*/messages.size();
    }

    @Override
    public int getItemViewType(int position) {
//            messages.moveToPosition(position);
//            String sender = messages.getString(4);
//
//
//            if(sender.compareTo(myFacebookId) == 0){
        return 0;
//            }
//            return 1;
    }

    public class MessageViewHolderMyMessage extends RecyclerView.ViewHolder {

        private TextView mMessage;

        public MessageViewHolderMyMessage(View itemView) {
            super(itemView);

            mMessage = (TextView) itemView.findViewById(R.id.user_message_view);
        }
    }

    public class MessageViewHolderOtherMessage extends RecyclerView.ViewHolder {

        private TextView otherMessage;

        public MessageViewHolderOtherMessage(View itemView) {
            super(itemView);

            otherMessage = (TextView) itemView.findViewById(R.id.other_message_view);
        }
    }
}
