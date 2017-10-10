package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.sj.emoji.DefEmoticons;
import com.sj.emoji.EmojiBean;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.event.CSUnreadEvent;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.model.chat.Message;
import com.yjjr.yjfutures.model.chat.User;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.BigPhotoActivity;
import com.yjjr.yjfutures.ui.TakePhotoActivity;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.widget.ChatFuncView;
import com.yjjr.yjfutures.widget.HeaderView;
import com.yjjr.yjfutures.widget.listener.EmojiFilter;
import com.yjjr.yjfutures.widget.listener.YJChat;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import sj.keyboard.XhsEmoticonsKeyBoard;
import sj.keyboard.adpater.EmoticonsAdapter;
import sj.keyboard.adpater.PageSetAdapter;
import sj.keyboard.data.EmoticonPageEntity;
import sj.keyboard.data.EmoticonPageSetEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.interfaces.EmoticonDisplayListener;
import sj.keyboard.interfaces.PageViewInstantiateListener;
import sj.keyboard.utils.imageloader.ImageBase;
import sj.keyboard.widget.EmoticonPageView;

public class ChatActivity extends TakePhotoActivity implements
        MessagesListAdapter.OnMessageLongClickListener<Message>,
        MessagesListAdapter.OnMessageClickListener<Message> {

    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;

    private String toSendId;

    private MessagesList mMessagesList;
    private UserInfo mUserInfo;
    private String mOtherUrl = "1";
    private String mMineUrl = "0";
    private EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            showMsg(messages);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            LogUtils.d(messages.toString());
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            LogUtils.d(messages.toString());
        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {
            LogUtils.d(messages.toString());
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            LogUtils.d(message.toString());
        }
    };
    private XhsEmoticonsKeyBoard mEditText;

    public static void startActivity(Context context) {
        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
        if (userInfo == null || TextUtils.isEmpty(userInfo.getEmchatAccount()) || TextUtils.isEmpty(userInfo.getEmchatPwd())) {
            return;
        }
        context.startActivity(new Intent(context, ChatActivity.class));
    }

    /**
     * 显示消息
     *
     * @param messages
     */
    private void showMsg(List<EMMessage> messages) {
        for (EMMessage msg : messages) {
            String from = msg.getFrom();
            boolean isMe = TextUtils.equals(mUserInfo.getEmchatAccount(), from);
            User user = new User(from, isMe ? mUserInfo.getName() : "有间客服", isMe ? mMineUrl : mOtherUrl, true);
            EMMessageBody body = msg.getBody();
            final Message message = new Message(msg.getMsgId(), user, body.toString());
            if (body instanceof EMTextMessageBody) {
                message.setText(((EMTextMessageBody) body).getMessage());
            } else if (body instanceof EMImageMessageBody) {
                message.setImage(new Message.Image(((EMImageMessageBody) body).getRemoteUrl()));
            } else if (body instanceof EMVoiceMessageBody) {
                EMVoiceMessageBody b = (EMVoiceMessageBody) body;
                message.setVoice(new Message.Voice(b.getRemoteUrl(), b.getLength()));
            }
            mMessagesList.post(new Runnable() {
                @Override
                public void run() {
                    messagesAdapter.addToStart(message, true);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mUserInfo = BaseApplication.getInstance().getUserInfo();
        toSendId = mUserInfo.getYjEmchat();
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                if (TextUtils.isEmpty(url)) return;
                if (TextUtils.equals("0", url)) {
                    imageView.setImageResource(R.drawable.ic_chat_avaster_male);
                } else if (TextUtils.equals("1", url)) {
                    imageView.setImageResource(R.drawable.ic_chat_avaster_female);
                } else {
                    com.yjjr.yjfutures.utils.imageloader.ImageLoader.load(mContext, url, imageView);
                }
            }
        };
        mMessagesList = (MessagesList) findViewById(R.id.messagesList);
        ((HeaderView) findViewById(R.id.header_view)).bindActivity(mContext);
        initInput();
        initAdapter();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        loadMessages();
    }

    private void initInput() {
        mEditText = (XhsEmoticonsKeyBoard) findViewById(R.id.ek_bar);

        // source data
        ArrayList<EmojiBean> emojiArray = new ArrayList<>();
        Collections.addAll(emojiArray, DefEmoticons.sEmojiArray);
        // emoticon click
        final EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
            @Override
            public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
                if (isDelBtn) {
                    int action = KeyEvent.ACTION_DOWN;
                    int code = KeyEvent.KEYCODE_DEL;
                    KeyEvent event = new KeyEvent(action, code);
                    mEditText.getEtChat().onKeyDown(KeyEvent.KEYCODE_DEL, event);
                } else {
                    if (o == null) {
                        return;
                    }
                    String content = null;
                    if (o instanceof EmojiBean) {
                        content = ((EmojiBean) o).emoji;
                    }
                    int index = mEditText.getEtChat().getSelectionStart();
                    Editable editable = mEditText.getEtChat().getText();
                    editable.insert(index, content);
                }
            }
        };

        // emoticon instantiate
        final EmoticonDisplayListener emoticonDisplayListener = new EmoticonDisplayListener() {
            @Override
            public void onBindView(int i, ViewGroup viewGroup, EmoticonsAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {
                final EmojiBean emojiBean = (EmojiBean) object;
                if (emojiBean == null && !isDelBtn) {
                    return;
                }

                viewHolder.ly_root.setBackgroundResource(com.keyboard.view.R.drawable.bg_emoticon);

                if (isDelBtn) {
                    viewHolder.iv_emoticon.setImageResource(R.drawable.ic_input_delete_normal);
                } else {
                    viewHolder.iv_emoticon.setImageResource(emojiBean.icon);
                }

                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emoticonClickListener.onEmoticonClick(emojiBean, 0, isDelBtn);
                    }
                });
            }
        };

        //  page instantiate
        PageViewInstantiateListener pageViewInstantiateListener = new PageViewInstantiateListener<EmoticonPageEntity>() {
            @Override
            public View instantiateItem(ViewGroup viewGroup, int i, EmoticonPageEntity pageEntity) {
                if (pageEntity.getRootView() == null) {
                    EmoticonPageView pageView = new EmoticonPageView(viewGroup.getContext());
                    pageView.setNumColumns(pageEntity.getRow());
                    pageEntity.setRootView(pageView);
                    try {
                        EmoticonsAdapter adapter = new EmoticonsAdapter(viewGroup.getContext(), pageEntity, null);
                        // emoticon instantiate
                        adapter.setOnDisPlayListener(emoticonDisplayListener);
                        pageView.getEmoticonsGridView().setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return pageEntity.getRootView();
            }
        };

        // build
        EmoticonPageSetEntity xhsPageSetEntity
                = new EmoticonPageSetEntity.Builder()
                .setLine(3)
                .setRow(7)
                .setEmoticonList(emojiArray)
                .setIPageViewInstantiateItem(pageViewInstantiateListener)
                .setShowDelBtn(EmoticonPageEntity.DelBtnStatus.LAST)
                .setIconUri(ImageBase.Scheme.DRAWABLE.toUri("ic_launcher"))
                .build();

        PageSetAdapter pageSetAdapter = new PageSetAdapter();
        pageSetAdapter.add(xhsPageSetEntity);
        mEditText.setAdapter(pageSetAdapter);

        ChatFuncView funcView = new ChatFuncView(mContext);
        funcView.setListener(new ChatFuncView.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                mEditText.reset();
                if (position == 0) {
                    getTakePhoto().onPickFromGallery();
                } else if (position == 1) {
                    getTakePhoto().onPickFromCapture(Uri.fromFile(new File(getFilesDir().getAbsolutePath() + "/sendpic.png")));
                }
            }
        });
        mEditText.addFuncView(funcView);
        mEditText.getEtChat().setHint("请输入您要咨询的问题");

        // add a filter
        mEditText.getEtChat().addEmoticonFilter(new EmojiFilter());
        mEditText.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextMsg(mEditText.getEtChat().getText());
                mEditText.getEtChat().setText(null);
            }
        });
//        mEditText.getBtnVoice().setVisibility(View.GONE);
        mEditText.getBtnVoice().setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
    }


    protected void loadMessages() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toSendId);
//获取此会话的所有消息
        if (conversation == null) return;
        List<EMMessage> messages = conversation.getAllMessages();
        showMsg(messages);
    }


    private boolean sendTextMsg(CharSequence input) {
        if (mUserInfo == null) return false;
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        EMTextMessageBody txtBody = new EMTextMessageBody(input.toString());
        message.addBody(txtBody);
        message.setTo(toSendId);
// 设置自定义扩展字段
        message.setAttribute("em_force_notification", true);
// 发送消息
        message.setMessageStatusCallback(new YJChat(mContext, new YJChat.CallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String error) {

            }

            @Override
            public void onProgress(int progress, String status) {

            }
        }));
        EMClient.getInstance().chatManager().sendMessage(message);
        messagesAdapter.addToStart(new Message(DateUtils.nowTime() + "", new User(mUserInfo.getEmchatAccount(), mUserInfo.getName(), mMineUrl, true), input.toString()), true);
        return true;
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        final TImage image = result.getImage();
        EMMessage imageSendMessage = EMMessage.createImageSendMessage(image.getOriginalPath(), false, toSendId);
        imageSendMessage.setMessageStatusCallback(new YJChat(mContext, new YJChat.CallBack() {
            @Override
            public void onSuccess() {
                Message message = new Message(DateUtils.nowTime() + "", new User(mUserInfo.getEmchatAccount(), mUserInfo.getName(), mMineUrl, true), null);
                message.setImage(new Message.Image("file://" + image.getOriginalPath()));
                messagesAdapter.addToStart(message, true);
            }

            @Override
            public void onError(int code, String error) {

            }

            @Override
            public void onProgress(int progress, String status) {

            }
        }));
        EMClient.getInstance().chatManager().sendMessage(imageSendMessage);
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


        holdersConfig.setOutcomingTextConfig(Holders.TextMessageHolder.class, R.layout.item_custom_outcoming_message);
        holdersConfig.setIncomingTextConfig(Holders.IncomingTextMessageHolder.class, R.layout.item_custom_incoming_text_message);
        holdersConfig.setOutcomingImageConfig(Holders.ImageMessageHolder.class, R.layout.item_custom_outcoming_image_message);
        messagesAdapter = new MessagesListAdapter<>(mUserInfo.getEmchatAccount(), holdersConfig, imageLoader);
        messagesAdapter.setOnMessageLongClickListener(this);
        messagesAdapter.setOnMessageClickListener(this);
        messagesAdapter.setDateHeadersFormatter(new DateFormatter.Formatter() {
            @Override
            public String format(Date date) {
                return DateUtils.formatDateTime(date);
            }
        });
        mMessagesList.setAdapter(messagesAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将未读消息标志位已读
        EventBus.getDefault().post(new CSUnreadEvent(0));
//        EMClient.getInstance().chatManager().importMessages();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    public void onMessageClick(Message message) {
        if (!TextUtils.isEmpty(message.getImageUrl())) {
            BigPhotoActivity.startActivity(mContext, message.getImageUrl());
        }
    }
}
