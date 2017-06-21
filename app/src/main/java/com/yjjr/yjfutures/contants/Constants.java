package com.yjjr.yjfutures.contants;

/**
 * Created by Administrator on 2015/6/2.
 */
public interface Constants {

    String CONTENT_PARAMETER = "CONTENT_PARAMETER";
    String CONTENT_PARAMETER_2 = "CONTENT_PARAMETER_2";
    String CONTENT_PARAMETER_3 = "CONTENT_PARAMETER_3";
    String CONTENT_PARAMETER_4 = "CONTENT_PARAMETER_4";
    String CONTENT_PARAMETER_5 = "CONTENT_PARAMETER_5";
    String CONTENT_PARAMETER_6 = "CONTENT_PARAMETER_6";
    String CONTENT_PARAMETER_7 = "CONTENT_PARAMETER_7";
    String CONTENT_PARAMETER_8 = "CONTENT_PARAMETER_8";
    String CONTENT_PARAMETER_9 = "CONTENT_PARAMETER_9";
    String CONTENT_PARAMETER_10 = "CONTENT_PARAMETER_10";
    String CONTENT_PARAMETER_11 = "CONTENT_PARAMETER_11";
    String CONTENT_PARAMETER_12 = "CONTENT_PARAMETER_12";
    String CONTENT_PARAMETER_13 = "CONTENT_PARAMETER_13";
    String CONTENT_PARAMETER_TOTAL_PAGE = "CONTENT_PARAMETER_TOTAL_PAGE";
    /**
     * 这个字段判断是否来自缓存，如果是缓存的话列表需要刷新数据
     */
    String CONTENT_PARAMETER_CACHE = "CONTENT_PARAMETER_CACHE";

    interface BizType {
        /**
         * 修改登录密码
         */
        int ModifyPwd = 1;
        /**
         * 绑定手机
         */
        int BindMobile = 2;

        /**
         * 绑定邮箱
         */
        int BindEmail = 3;
        /**
         * 找回密码
         */
        int FindPwd = 4;
        /**
         * 模拟开户
         */
        int OpenDemoAccount = 5;
        /**
         * 其它
         */
        int Others = 6;
    }

    interface FROM {
        int IOS = 1;
        int ANDROID = 3;
    }

    interface Common {
        int SUCCESS = 0;
        int FAIL = 1;
        int ERROR = 2;
        int NO_LOGIN = 3;
    }


    interface ThirdPartyItem {
        int QQ = 1;
        int WE_CHAT = 2;
        int SINA_MICRO_BLOG = 0;
    }

    interface LoginStatus {
        int SUCCESS = 0;
        int FAIL = 1;
        int ERROR = 2;
    }

    interface RegisterStatus {
        int SUCCESS = 0;
        int FAIL = 1;
        int ERROR = 2;

    }

    interface TimeRange {
        //TimeRange：
        // 3 自开始盈利 0上周盈利 1上个月盈利 3今天盈利 BrokerID 不筛选则为-1
        int START = 3;
        int WEEK = 0;
        int MONTH = 1;
        int TODAY = 2;
        int DEFAULT = 4;
    }

    interface FollowType {
        int HAND = 1;  //按手数跟随
        int TIMES = 2; //按倍数跟随
    }

    /**
     * 是否跟随交易员
     */
    interface FollowedTrader {
        int UN_FOLLOW = 0;  //未跟随
        int FOLLOWED = 1;  //已跟随
    }

    interface FollowDirection {
        int POSITIVE = 1; //正向跟随
        int REVERSE = 0; //反向跟随
    }

    /**
     * 跟随状态
     */
    interface FollowedType {
        int HISTORY_FOLLOW = 0;  //未跟随
        int IS_FOLLOW = 1;  //已跟随
    }

    interface Broker {
        int PICO = 1;
        int JF = 2;
        int DEMO = 3;
        int FH = 4;
        int KVB = 5;
    }

    interface BroadCast {
        //Login Register ChangePassword
        String ScrollViewBroadcast = "com.followMe.followMe.broadcast.scrollview";
        String CLOSE_ACTIVITY = "com.followMe.followMe.closeActivity";
        String CLOSE_ALL_ACTIVITY = "com.followMe.followMe.closeALLActivity";
        String UPGRADE_INVESTOR_SUCCESS = "com.followMe.followMe.UPGRADE.INVESTOR.SUCCESS";
        //Message
        String REMIND_NEWS = "com.followMe.followMe.REMIND.NEWS";
        //Blog
        String ACTION_BLOG_ATTENTION = "com.followMe.followMe.blog.attention";
        String ACTION_BLOG_COLLECTION = "com.followMe.followMe.blog.COLLECTION";
        String ACTION_BLOG_DELETE = "com.followMe.followMe.blog.DELETE";
        String ACTION_BLOG_FAVOURITE = "com.followMe.followMe.blog.FAVOURITE";
        String ACTION_BLOG_SEND = "com.followMe.followMe.blog.SEND";
        String ACTION_BLOG_FORWARD = "com.followMe.followMe.blog.FORWARD";
        String ACTION_BLOG_COMMENT = "com.followMe.followMe.blog.COMMENT";
        String ACTION_DISMISS_TRADER_POPUP_WINDOW = "com.followMe.followMe.blog.DISMISS_TRADER_POPUP_WINDOW";
        String ACTION_REFRESH_COMMENT_COUNT = "com.followMe.followMe.blog.ACTION_REFRESH_COMMENT_COUNT";
        //Trader
        String ACTION_TRADER_FOLLOW_STATUS_CHANGE = "com.followMe.followMe.trader.follow.status.change";
    }

    interface CardType {
        int IDCard = 0;  //身份证
        int Passport = 1; //护照
        int Gas_cardin = 2;  //军官证
    }

    interface ActivityReturnReuqestCode {
        int FOLLOW_MANAGE = 101;
        int HEAD_PORTRAIT_CROP = 102;
        int REQUEST_PICTURE = 103;
        int TAKE_PHOTO = 104;
    }

    interface MessageCommentType {
        int MY_COMMENT = 0X100;
        int COMMENT_ME = 0X101;
        int AT_ME_BLOG = 0X102;
        int AT_ME_COMMENT = 0X103;
    }

    interface ServiceFeeStatus {
        int FAIL = -1;   //申请失败
        int APPLYING = 0; //申请中
        int PASS = 1;     //审核通过
    }

    interface ServiceFeeApplyEnable {
        int ENABLE = 1;   //可申请
        int NO_ENBALE = 0; //不可申请
    }

    interface UserType {
        int TYPE_VISITOR = -1;
        int TYPE_NORMAL = 0;
        int TYPE_TRADER = 1;
        int TYPE_FOLLOWER = 2;
        int TYPE_AUTH_USER = 3;
    }

    interface PasswordLength {
        int MAX_LENGTH = 16;
        int MIN_LENGTH = 6;
    }

    interface AccountRole {
        // 账户角色类型
        // 1：FollowMe官方认证账号 2:认证帐号
        int Official_certification = 1;
        int Certification = 2;
        int V_TRADER = 3;
        int V_USER = 4;
    }

    //持仓类型
    interface OrderType {
        int IS_POSITION = 1; //正在持仓
        int HISTORY_POSITION = 0; //历史持仓
        int GUADAN = 2; //历史持仓
    }

    //买卖
    interface CMD {
        int BUY = 0;
        int SELL = 1;
        int BuyLimit = 2;
        int SellLimit = 3;
        int BuyStop = 4;
        int SellStop = 5;
        int Balance = 6;
        int Credit = 7;

    }

    //开仓 平仓
    interface OPEN_CLOSE_TYPE {
        int OPEN = 0;
        int CLOSE = 1;
    }

    interface PRIVATE_LETTER_READ_TYPE {
        int ALL = -1;
        int NO_READ = 0;
        int READED = 1;
    }

    interface PRIVATE_LETTER_MSG_TYPE {
        int PICTURE = 2;
        int TEXT = 1;
    }


    interface PRIVATE_LEETER_SEND_STATUS {
        int SUCCESS = 0;
        int FAIL = 1;
        int SENDING = 2;
    }

    interface USER_ATTENTION_STATUS {
        int ATTENTION = 0;
        int NOT_ATTENTION = 1;
        int FOCUS_ON_EACH_OTHER = 2;
    }

    // 交易员账户类型
    /// 资金类型 0：自有资金 1：信用账户 2：配资账户 3：风险共担账户
    /// </summary>
    interface MoneyType {
        int OwnFund = 0;
        int CreditAccount = 1;
        int CapitalAccount = 2;
        int RiskSharingAccount = 3;
    }


    /// <summary>
    /// 账户创建类型 0：默认 1：FM 绑定开户(即：关联账户) 2：FM 新开户
    /// </summary>
    interface CreateType {
        int Default = 0;
        int BindingAccount = 1;
        int FMNewUser = 2;
    }

    interface Sex {
        int MAN = 1;
        int WOMAN = 2;
    }

    interface BloodGroup {
        int A = 0;
        int B = 1;
        int AB = 2;
        int O = 3;
    }

    interface GetAccountType {
        String GetRealInvestor = "LIVE";
    }

    /**
     * 在线交易中对msg.arg1的类型的设置
     */
    interface OnlineTxModelType {
        int SymbolInfoModel = 1;
        int TradeLoginResponse_DataEntity = 2;
    }

    interface OnlineTxSendRequestType {
        int TYPE_OPEN = 1; //1-开仓
        int TYPE_CLOSE = 2; //2-平仓
        int TYPE_DELETE_GUADAN = 5; /////删除挂单
    }

    interface SocketEvent {
        String PRICE = "price";
        String ORDER = "order";
        String TRADE_ORDER = "tradeOrder";
        String MODIFY_ORDER = "modifyOrder";
        String CLOSE_ORDER = "closeOrder";
        String RESULT = "result";

        String PING = "p";

        String MT4_PRICE = "q";
        String MT4_SYMBOL_LIST = "sl";

        //失败/错误代码定义
        interface ErrorCode {
            int eGR_NOERROR = 0;  //没有错误
            int eGR_FXCMDISCONNECT = 10; //与FXCM服务器的连接已经断开
            int eGR_ERRREQUESTFACTORY = 11;//下开仓/平仓/限价/止损/调整现价止损等等 单时调用getRequestFactory失败
            int eGR_ERRCREATEORDERREQUEST = 12; //下开仓/平仓/限价/止损/调整现价止损等等 单时createOrderRequest失败
            int eGR_ERRCREATEMARKETCLOSEORDER = 13; //下平仓单时获取品种市场信息失败
            int eGR_ERROPENTRADERFROMRES = 14; //下开仓单时从ResponseListener::onRequestFailed()函数里返回的失败
            int eGR_ERRCLOSETRADERFROMRES = 15; //下平仓单时从ResponseListener::onRequestFailed()函数里返回的失败
            int eGR_ERRADDLIMITPRICEFROMRES = 16;//新增限价时从ResponseListener::onRequestFailed()函数里返回的失败
            int eGR_ERRADDSTOPPRICEFROMRES = 17; //新增止损价时从ResponseListener::onRequestFailed()函数里返回的失败
            int eGR_ERREDITTRADEFROMRES = 18; //编辑订单(修改止损/限价)时从ResponseListener::onRequestFailed()函数里返回的失败
            int eGR_ERROFFGETNAMEACCOID = 19; //取得交易品编号对应的名字失败
            int eGR_ERRGETACCOUNTINFO = 20; //取得帐号信息失败
            int eGR_ERRMINOFFERBASEUNIT = 21; //取得交易品最小购买单位失败
            int eGR_ERRNOALLTRADE = 22; //该交易品不允许交易
        }

        interface ResultCode {
            int CLOSE_FAIL = 6; //平仓失败
            int CLOSE_SUCCESS = 7; //平仓成功
            int ORDER_FAIL = 3;  //下单失败
            int FORCE_CLOSE = 38; //强制平仓
            int SETTING_FAIL = 9; //设置失败
            int SETTING_SUCCESS = 10; //设置成功
            int ORDER_SUCCESS = 4; //成功下单
            int GUADAN_FAIL = 25;  //挂单失败
            int GUADAN_FAIL_RESON_PRICE_1 = 23; //价格不合理,挂单失败
            int GUADAN_FAIL_RESON_PRICE_2 = 24; //价格不合理,挂单失败
            int GUADAN_SUCCESS = 26; //挂单成功
            int DELETE_GUADAN_STATUS = 27; //返回删除挂单状态   FC = 0 成功 FC = ！0 失败
            int DUICHONGDAN_DEAL_SUCCESS = 29; //对冲单成交
            int GUADAN_DEAL_SUCCESS = 30; //挂单成交
            int GUADAN_MODIFY_SUCCESS = 33; //挂单修改止损止盈成功
        }

        interface MT4ResultCode {
            int SUCCESS = 0; //操作成功
            int GUADAN_DEAL = 217; //挂单成交
        }
    }

    public interface OnlineTransaction {

        public interface SoldType {
            String BUY = "B";
            String SELL = "S";
        }

        interface Code {
            int OpenOrder = 210;
            int CloseOrder = 211;
            int BatchCloseOrder = 219;
            int EditPositionSLTP = 212;
            int CreatLimitedOrder = 213;
            int DeleteLimtedOrder = 214;
            int EditLimtedOrder = 215;
            int MT4TradeMessage = 222;
        }
    }

    /**
     * K线图的TypeName
     */
    interface KLineTypeName {
        String K1 = "1";
        String K5 = "5";
        String K15 = "15";
        String K30 = "30";
        String K60 = "60";
        String K240 = "240";
        String Day = "day";
        String Week = "week";
        String Month = "month";
    }

    /**
     * 微博页面搜索的类型 TODO:这里需要拿到接口后修改
     */
    interface BlogSearchType {
        int All = 0;
        int Blog = 1;
        int People = 2;
    }

    interface AuditStatus {
        //        审核状态：待审核=0，审核失败=1，开户成功=2，审核通过=3
        int WaitAudit = 0;
        int AuditFail = 1;
        int OpenSuccess = 2;
        int AuditPass = 3;
    }

    //新的好友
    public static final int FRIENDLIST_REQUESTCODE = 1001;
    //搜索
    public static final int SEARCH_REQUESTCODE = 1002;
    //添加好友
    public static final int PERSONAL_REQUESTCODE = 1003;
    //加入群组
    public static final int GROUP_JOIN_REQUESTCODE = 1004;
    //退出群组
    public static final int GROUP_QUIT_REQUESTCODE = 1005;
    //修改用户名称
    public static final int FIX_USERNAME_REQUESTCODE = 1006;
    //删除好友
    public static final int DELETE_USERNAME_REQUESTCODE = 1007;
    //修改讨论组名称
    public static final int FIX_DISCUSSION_NAME = 1008;
    //修改群名片
    public static final int FIX_GROUP_INFO = 1010;
    //修改设置页面
    public static final int UPDATE_DISCUTION_NUMBER = 1009;
    //@消息
    public static final int MESSAGE_REPLY = 1010;
    public static final String DEFAULT = "default";
    public static final String APP_TOKEN = "FM_TOKEN";
    public static final String APP_USER_ID = "FM_USERID";
    public static final String APP_USER_NAME = "FM_USER_NAME";
    public static final String APP_USER_PORTRAIT = "FM_USER_PORTRAIT";


    interface UserActive {
        int active = 1;
        int unActive = 0;
    }

    interface QueryUser {
        String ALL = "1"; //所有的
        String ACTIVE = "0"; //可用的
    }

    /**
     * 在线交易品种列表的品种类型
     */
    interface InsType {
        int FOREX = 0;//外汇
        int INDICES = 2;//指数
        int OIL = 3;//原油
        int SILVER = 5;//贵金属
    }

    interface RateType {
        int Deposit = 0;
        int Withdrawal = 1;
    }

    interface TradeState {
        //
        // Summary:
        //     Open normal
        int OpenNormal = 0;
        //
        // Summary:
        //     Open remand
        int OpenRemand = 1;
        //
        // Summary:
        //     Open restored
        int OpenRestored = 2;
        //
        // Summary:
        //     Close normal
        int ClosedNormal = 3;
        //
        // Summary:
        //     Closed part
        int ClosedPart = 4;
        //
        // Summary:
        //     Closed by
        int ClosedBy = 5;
        //
        // Summary:
        //     Deleted
        int Deleted = 6;
    }

    interface SymbolState {
        /**
         * Trade is disabled.
         */
        int TRADE_NO = 0;
        /**
         * Only closure is allowed.
         */
        int TRADE_CLOSE = 1;
        /**
         * Full trading access.
         */
        int TRADE_FULL = 2;
    }
}
