package com.easywetchat.item;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.easywetchat.R;
import com.easywetchat.databinding.ItemWetchatFriendsCircleLayoutBinding;
import com.easywetchat.model.FeedBean;

public class WetChatFriendsCircleItem extends BaseItem {

    private FeedBean bean;

    private Context context;

    public WetChatFriendsCircleItem(FeedBean bean){
        this.bean = bean;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
        super.onBinding(binding);
        ItemWetchatFriendsCircleLayoutBinding dataBinding = (ItemWetchatFriendsCircleLayoutBinding)binding;
        context = dataBinding.getRoot().getContext();
        dataBinding.friendsCircleItemNameTv.setText(bean.nickName);
        dataBinding.friendsCircleItemContentTv.setText(bean.content);
        dataBinding.friendsCircleItemAvatarView.setImageURI(bean.avatarUrl);
        dataBinding.friendsCircleItemImageLayout.setImageUrls(bean.images);
    }

    @Override
    public int getLayout() {
        return R.layout.item_wetchat_friends_circle_layout;
    }
}
