package com.yjjr.yjfutures.ui.mine;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.chat.Message;
import com.yjjr.yjfutures.widget.listener.EmojiFilter;

/**
 * Created by dell on 2017/9/11.
 */

public class Holders {
    public static class TextMessageHolder extends MessageHolders.OutcomingTextMessageViewHolder<IMessage> {

        protected ImageView userAvatar;

        public TextMessageHolder(View itemView) {
            super(itemView);
            userAvatar = (ImageView) itemView.findViewById(com.stfalcon.chatkit.R.id.messageUserAvatar);
        }

        @Override
        public void onBind(IMessage message) {
            super.onBind(message);
            if (text != null) {
                EmojiFilter.filter(text, text.getText(), 0);
                text.setTextIsSelectable(true);
            }
            if (userAvatar != null) {
                boolean isAvatarExists = getImageLoader() != null
                        && message.getUser().getAvatar() != null
                        && !message.getUser().getAvatar().isEmpty();

                userAvatar.setVisibility(isAvatarExists ? View.VISIBLE : View.GONE);
                if (isAvatarExists) {
                    getImageLoader().loadImage(userAvatar, message.getUser().getAvatar());
                }
            }
        }

    }

    public static class ImageMessageHolder extends MessageHolders.OutcomingImageMessageViewHolder<Message> {

        protected ImageView userAvatar;

        public ImageMessageHolder(View itemView) {
            super(itemView);
            userAvatar = (ImageView) itemView.findViewById(com.stfalcon.chatkit.R.id.messageUserAvatar);
        }

        @Override
        public void onBind(Message message) {
            super.onBind(message);
            if (userAvatar != null) {
                boolean isAvatarExists = getImageLoader() != null
                        && message.getUser().getAvatar() != null
                        && !message.getUser().getAvatar().isEmpty();

                userAvatar.setVisibility(isAvatarExists ? View.VISIBLE : View.GONE);
                if (isAvatarExists) {
                    getImageLoader().loadImage(userAvatar, message.getUser().getAvatar());
                }
            }
        }

    }


    public static class IncomingTextMessageHolder extends MessageHolders.IncomingTextMessageViewHolder<IMessage> {


        public IncomingTextMessageHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(IMessage message) {
            super.onBind(message);
            if (text != null) {
                EmojiFilter.filter(text, text.getText(), 0);
                text.setTextIsSelectable(true);
            }
        }

    }
}
