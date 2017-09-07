package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.adapter.message.EMATextMessageBody;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.chat.Message;
import com.yjjr.yjfutures.model.chat.MessagesFixtures;
import com.yjjr.yjfutures.model.chat.User;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends BaseActivity
        implements MessagesListAdapter.OnMessageLongClickListener<Message>,
        MessageInput.InputListener,
        MessageInput.AttachmentsListener, MessagesListAdapter.SelectionListener{


    private static final int TOTAL_MESSAGES_COUNT = 100;

    protected final String senderId = "0";
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;

    private int selectionCount;
    private Date lastLoadedDate;

    private MessagesList messagesList;
    private EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            for(EMMessage msg:messages) {
                User user = new User(msg.getFrom(),msg.getUserName(),"",true);
                Message message = new Message(msg.getMsgId(),user,msg.getBody().toString());
                messagesAdapter.addToStart(message,true);
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ChatActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                com.yjjr.yjfutures.utils.imageloader.ImageLoader.load(mContext, url, imageView);
            }
        };
        MessageInput input = (MessageInput) findViewById(R.id.input);
        messagesList = (MessagesList) findViewById(R.id.messagesList);

        initAdapter();
        input.setInputListener(this);
        input.setAttachmentsListener(this);
        EMClient.getInstance().login("gzw", "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.d("登录成功");
            }

            @Override
            public void onError(int code, String error) {
                LogUtils.e("登录失败  "+error);

            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        loadMessages();
    }


    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
    }

    protected void loadMessages() {
        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation("admin");
//获取此会话的所有消息
                List<EMMessage> messages = conversation.getAllMessages();
                List<Message> messageList = new ArrayList<>();
                for(EMMessage msg:messages) {
                    User user = new User(msg.getFrom(),msg.getUserName(),"",true);
                    Message message = new Message(msg.getMsgId(),user,msg.getBody().toString());
                    messageList.add(message);
                }
                messagesAdapter.addToEnd(messageList,true);
            }
        }, 1000);
    }

    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
        return new MessagesListAdapter.Formatter<Message>() {
            @Override
            public String format(Message message) {
                String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                        .format(message.getCreatedAt());

                String text = message.getText();
                if (text == null) text = "[attachment]";

                return String.format(Locale.getDefault(), "%s: %s (%s)",
                        message.getUser().getName(), text, createdAt);
            }
        };
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        messagesAdapter.addToStart(
                MessagesFixtures.getTextMessage(input.toString()), true);
        return true;
    }

    @Override
    public void onAddAttachments() {
        messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true);
    }

    @Override
    public void onMessageLongClick(Message message) {

    }

    private void initAdapter() {
        MessageHolders holdersConfig = new MessageHolders();
//                .setIncomingTextLayout(R.layout.item_custom_incoming_text_message)
//                .setOutcomingTextLayout(R.layout.item_custom_outcoming_text_message)
//                .setIncomingImageLayout(R.layout.item_custom_incoming_image_message)
//                .setOutcomingImageLayout(R.layout.item_custom_outcoming_image_message);

        messagesAdapter = new MessagesListAdapter<>(senderId, holdersConfig, imageLoader);
        messagesAdapter.setOnMessageLongClickListener(this);
        messagesList.setAdapter(messagesAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
