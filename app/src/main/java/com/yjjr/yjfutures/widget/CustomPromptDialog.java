package com.yjjr.yjfutures.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yjjr.yjfutures.R;


/**
 * 通用的dialog弹窗
 */
public class CustomPromptDialog extends Dialog {

    public CustomPromptDialog(Context context) {
        super(context);
    }

    public CustomPromptDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private int messageGravity = Gravity.CENTER;
        private boolean isShowClose = false;
        private int messageDrawableId;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessageDrawableId(@DrawableRes int id) {
            messageDrawableId = id;
            return this;
        }

        public Builder setMessageGravity(int gravity) {
            this.messageGravity = gravity;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder isShowClose(boolean isShow) {
            this.isShowClose = isShow;
            return this;
        }

        public CustomPromptDialog create() {
            boolean navigationButtonShow = false;
            boolean positiveButtonShow = false;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomPromptDialog dialog = new CustomPromptDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_custom_prompt, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            TextView tvTitle = (TextView) layout.findViewById(R.id.title);
            int visible = TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE;
            ImageView ivClose = (ImageView) layout.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ivClose.setVisibility(isShowClose ? View.VISIBLE : View.GONE);


            tvTitle.setText(title);
            tvTitle.setVisibility(visible);
            // set the confirm button
            if (positiveButtonText != null) {
                positiveButtonShow = true;
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    (layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                navigationButtonShow = true;
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    (layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (message != null) {
                TextView tvMessage = (TextView) layout.findViewById(R.id.message);
                tvMessage.setGravity(messageGravity);
                tvMessage.setText(message);
                if(messageDrawableId != 0) {
                    tvMessage.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, messageDrawableId), null, null);
                }
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((FrameLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((FrameLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }

//            if (!navigationButtonShow && positiveButtonShow) {
//                layout.findViewById(R.id.positiveButton).setBackgroundResource(R.drawable.selector_custom_dialog_button_bg);
//                layout.findViewById(R.id.sperate_line).setVisibility(View.GONE);
//            }

            dialog.setContentView(layout);
            return dialog;
        }
    }
}
