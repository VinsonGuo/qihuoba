package com.yjjr.yjfutures.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.sj.emoji.DefEmoticons;
import com.sj.emoji.EmojiBean;
import com.sj.emoji.EmojiDisplay;
import com.sj.emoji.EmojiSpan;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.biz.UserInfo;
import com.yjjr.yjfutures.model.chat.Message;
import com.yjjr.yjfutures.model.chat.MessagesFixtures;
import com.yjjr.yjfutures.model.chat.User;
import com.yjjr.yjfutures.ui.BaseActivity;
import com.yjjr.yjfutures.ui.BaseApplication;
import com.yjjr.yjfutures.ui.TakePhotoActivity;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.RxUtils;
import com.yjjr.yjfutures.utils.ToastUtils;
import com.yjjr.yjfutures.widget.ChatFuncView;
import com.yjjr.yjfutures.widget.listener.EmojiFilter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import sj.keyboard.XhsEmoticonsKeyBoard;
import sj.keyboard.adpater.EmoticonsAdapter;
import sj.keyboard.adpater.PageSetAdapter;
import sj.keyboard.data.EmoticonPageEntity;
import sj.keyboard.data.EmoticonPageSetEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.interfaces.EmoticonDisplayListener;
import sj.keyboard.interfaces.EmoticonFilter;
import sj.keyboard.interfaces.PageViewInstantiateListener;
import sj.keyboard.utils.EmoticonsKeyboardUtils;
import sj.keyboard.utils.imageloader.ImageBase;
import sj.keyboard.widget.EmoticonPageView;

public class ChatActivity extends TakePhotoActivity
        implements MessagesListAdapter.OnMessageLongClickListener<Message>{

    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;

    private String toSendId = "13163725850";
//    private String toSendId = "18566745261";

    private MessagesList mMessagesList;

    private EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            for (EMMessage msg : messages) {
                User user = new User(msg.getFrom(), msg.getUserName(), mOtherUrl, true);
                final Message message = new Message(msg.getMsgId(), user, msg.getBody().toString());
                mMessagesList.post(new Runnable() {
                    @Override
                    public void run() {
                        messagesAdapter.addToStart(message, true);
                    }
                });
            }
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
    private UserInfo mUserInfo;
    private String mOtherUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504867752018&di=f305320cf4629db92fc59e6e56fefe23&imgtype=0&src=http%3A%2F%2Fwanzao2.b0.upaiyun.com%2Fsystem%2Fpictures%2F36203759%2Foriginal%2F1464751360_640x640.jpg";
    private String mMineUrl = "https://image.baidu.com/search/detail?ct=503316480&z=&tn=baiduimagedetail&ipn=d&word=%E6%88%91%E6%96%B9&step_word=&ie=utf-8&in=&cl=2&lm=-1&st=-1&cs=425063068,452471341&os=1422492999,2408581962&simid=3343167365,162758627&pn=0&rn=1&di=88563311540&ln=1981&fr=&fmq=1504857769755_R&ic=0&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&is=0,0&istype=2&ist=&jit=&bdtype=0&spn=0&pi=0&gsm=0&hs=2&objurl=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2016-4%2F2016042620205417694.png&rpstart=0&rpnum=0&adpicid=0";

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ChatActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mUserInfo = BaseApplication.getInstance().getUserInfo();
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                com.yjjr.yjfutures.utils.imageloader.ImageLoader.load(mContext, url, imageView);
            }
        };
        mMessagesList = (MessagesList) findViewById(R.id.messagesList);
        initInput();
        initAdapter();
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                EMClient.getInstance().createAccount(mUserInfo.getAccount(), "123456");
            }
        })
                .compose(RxUtils.applySchedulers())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        ToastUtils.show(mContext, "注册成功");
                        login();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        ToastUtils.show(mContext, throwable.getMessage());
                        login();
                    }
                });

        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    private void initInput() {
        final XhsEmoticonsKeyBoard ek_bar = (XhsEmoticonsKeyBoard) findViewById(R.id.ek_bar);

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
                    ek_bar.getEtChat().onKeyDown(KeyEvent.KEYCODE_DEL, event);
                } else {
                    if (o == null) {
                        return;
                    }
                    String content = null;
                    if (o instanceof EmojiBean) {
                        content = ((EmojiBean) o).emoji;
                    }
                    int index = ek_bar.getEtChat().getSelectionStart();
                    Editable editable = ek_bar.getEtChat().getText();
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
        ek_bar.setAdapter(pageSetAdapter);

        ChatFuncView funcView = new ChatFuncView(mContext);
        funcView.setListener(new ChatFuncView.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                if(position == 0) {
                    getTakePhoto().onPickFromGallery();
                }else if(position == 1) {
                    getTakePhoto().onPickFromCapture(Uri.parse(getFilesDir().getAbsolutePath()+"/sendpic.jpg"));
                }
            }
        });
        ek_bar.addFuncView(funcView);

        // add a filter
        ek_bar.getEtChat().addEmoticonFilter(new EmojiFilter());
        ek_bar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextMsg(ek_bar.getEtChat().getText());
                ek_bar.getEtChat().setText(null);
            }
        });
        ek_bar.getBtnVoice().setVisibility(View.GONE);
    }

    private void login() {
        EMClient.getInstance().login(mUserInfo.getAccount(), "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.d("登录成功");
                loadMessages();
            }

            @Override
            public void onError(int code, String error) {
                LogUtils.e("登录失败  " + error);

            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    protected void loadMessages() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toSendId);
//获取此会话的所有消息
        if(conversation == null) return;
        List<EMMessage> messages = conversation.getAllMessages();
        final List<Message> messageList = new ArrayList<>();
        for (EMMessage msg : messages) {
            User user = new User(msg.getFrom(), msg.getUserName(), mOtherUrl, true);
            Message message = new Message(msg.getMsgId(), user, msg.getBody().toString());
            messageList.add(message);
        }
        mMessagesList.post(new Runnable() {
            @Override
            public void run() {
                messagesAdapter.addToEnd(messageList, true);
            }
        });
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
        EMClient.getInstance().chatManager().sendMessage(message);
        messagesAdapter.addToStart(new Message(System.currentTimeMillis() + "", new User(mUserInfo.getAccount(), mUserInfo.getName(),mMineUrl, true), input.toString()), true);
        return true;
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

        messagesAdapter = new MessagesListAdapter<>(mUserInfo.getAccount(), holdersConfig, imageLoader);
        messagesAdapter.setOnMessageLongClickListener(this);
        mMessagesList.setAdapter(messagesAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
