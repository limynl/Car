package com.team.car.fragment.found.childfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.team.car.R;
import com.team.car.adapter.found.CommonRecyclerAdapter;
import com.team.car.adapter.found.CommonRecyclerHolder;
import com.team.car.entity.found.InfoModel;
import com.team.car.entity.found.PicModel;
import com.team.car.widgets.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lmy on 2017/1/25.
 * email 1434117404@qq.com
 */

public class carDynamicFragment extends Fragment {
    private static final String TAG  = carDynamicFragment.class.getSimpleName();
    private ToastUtil toastUtil = new ToastUtil();
    private Context context;

    private String url = "http://139.199.23.142:8080/TestShowMessage1/logo.png";
    private XRecyclerView mRecyclerView;//刷新控件
    private CommonRecyclerAdapter<InfoModel> mAdapter;
    private List<InfoModel> mInfoModels;//所有动态的集合
    private List<InfoModel> test;//测试数据
    private int start = 0;
    private int count = 10;//设置一次获取的条目数
    private View footerView;//脚布局
    private ImageView mImageView;

    /**
     * 初始化上下文对象
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    /**
     * 加载主布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_found_dynamic_detail,container,false);
        initView(view);//初始化控件及数据
        return view;
    }

    /**
     * 初始化相应的控件
     * @param view
     */
    private void initView(View view) {
        mRecyclerView = (XRecyclerView)view.findViewById(R.id.homework_recycler);
        mImageView = (ImageView) view.findViewById(R.id.no_content);

//        mNoticeModelList = new ArrayList<>();
        mInfoModels = new ArrayList<>();

        // 获取一些假数据
//        getSomeData();
        //构造假数据
        List<PicModel> list = new ArrayList<>();
        list.add(new PicModel(1, url));
        list.add(new PicModel(2, url));
        list.add(new PicModel(3, url));
        for (int i = 0; i < 5; i++) {
            InfoModel infoModel = new InfoModel();
            infoModel.username = "小李";
            infoModel.content = "这是内容";
            infoModel.praiseCount = 3;
            infoModel.commentCount = 10;
            infoModel.isIPraised = true;
            infoModel.picUrls =list;
            mInfoModels.add(infoModel);
        }

        test = mInfoModels;

//        loadData(true);

        mAdapter = new CommonRecyclerAdapter<InfoModel>(context, mInfoModels, R.layout.layout_car_dynamic_item) {
            @Override
            public void convert(final CommonRecyclerHolder holder, final InfoModel item, final int position, boolean isScrolling) {
               /* if (TextUtils.isEmpty(item.user.avatar)) {//发布人的头像没有，指定默认头像
                    holder.setImageResource(R.id.notice_item_avatar, R.drawable.default_avatar);
                } else {
//                    Log.e(TAG, item.user.avatar);
                    //联网请求图片
                }*/
                holder.setImageByUrl(R.id.notice_item_avatar,url);//
                holder.setText(R.id.notice_item_name, "小李");
                holder.setText(R.id.notice_item_time, "2/15");
                holder.setText(R.id.notice_item_content, item.content);
//                holder.setText(R.id.notice_item_like, "赞 " + item.praiseCount);
//                holder.setText(R.id.notice_item_comment, "评论 " + item.commentCount);
//                if (item.isIPraised){//自己赞了该评论，就将其文本颜色设置为红色
//                    holder.setTextColor(R.id.notice_item_like, getResources().getColor(R.color.red));
//                }else{
//                    holder.setTextColor(R.id.notice_item_like, getResources().getColor(R.color.gray));
//                }

                ArrayList<ImageInfo> imageInfoList = new ArrayList<>();
                List<PicModel> picModels = item.picUrls;
                if (picModels != null && picModels.size() != 0){
                    for (PicModel picModel:picModels) {
                        ImageInfo imageInfo = new ImageInfo();
                        imageInfo.setThumbnailUrl(picModel.imageUrl);
                        imageInfo.setBigImageUrl(picModel.imageUrl);
                        imageInfoList.add(imageInfo);
                    }
                }
                holder.setNineGridAdapter(R.id.community_nineGrid,new NineGridViewClickAdapter(context, imageInfoList));
//                }
//                Log.e(TAG,item.mainid+","+item.isIPraised);

                //点赞
//                holder.setOnRecyclerItemClickListener(R.id.notice_item_like, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //点赞操作，将其传至服务器进行更新，并将当前赞的颜色改变
//                        insertPraised(item,holder);
//
//                    }
//                });

                //进入评论页面，进行评论，查看
//                holder.setOnRecyclerItemClickListener(R.id.notice_item_comment, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(context, "进入评论详情", Toast.LENGTH_SHORT).show();
////                        LookDetailActivity.start(getActivity(), mInfoModels.get(position));//开启评论详情页面
//                    }
//                });
            }
        };


        //设置下拉刷新，上拉加载更多
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);



        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//                loadData(true);
//                mRecyclerView.refreshComplete();//刷新完成
            }

            @Override
            public void onLoadMore() {
//                getSomeData();
//                loadData(false);
//                mRecyclerView.loadMoreComplete();//加载更多完成
            }
        });

        footerView = LayoutInflater.from(context).inflate(R.layout.layout_car_dynamic_footer_not_more, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
        mRecyclerView.addFootView(footerView);
        footerView.setVisibility(View.GONE);

        // 设置下拉图片为自己的图片
        mRecyclerView.setArrowImageView(R.mipmap.ic_launcher);

    }

    /**
     * 把赞的信息提交到服务器
     */
    private void insertPraised(final InfoModel item,final CommonRecyclerHolder holder) {
//        holder.setTextColor(R.id.notice_item_like, getResources().getColor(R.color.red));
        toastUtil.Short(context, "点赞成功").show();

        /*if (AppService.getInstance().getCurrentUser() == null) {
            return;
        }
        AppService.getInstance().updatePraiseAsync(item.mainid, AppService.getInstance().getCurrentUser().username
                , new JsonCallback<LslResponse<PraiseModel>>() {
                    @Override
                    public void onSuccess(LslResponse<PraiseModel> praiseLslResponse, Call call, Response response) {
                        if (praiseLslResponse.code == LslResponse.RESPONSE_OK){
                            Log.e(TAG,"更新赞的信息成功！");
                            int praiseCount = praiseLslResponse.data.praiseCount;
                            boolean isInsert = praiseLslResponse.data.isInsert;
                            if (!isInsert) {
                                holder.setTextColor(R.id.notice_item_like, getResources().getColor(R.color.gray));
                            } else {
                                holder.setTextColor(R.id.notice_item_like, getResources().getColor(R.color.red));
                            }
                            holder.setText(R.id.notice_item_like, "赞 " + praiseCount);
                        }else{
                            Log.e(TAG,"更新赞的信息失败！");
                            UIUtil.showToast("更新赞的信息失败！");
                        }
                    }
                });*/
    }


    private int lastMainId;

    /**
     * 加载数据
     */
    private void loadData(final boolean isRefresh) {
        mRecyclerView.setLoadingMoreEnabled(true);
        mInfoModels.clear();
        mInfoModels = test;
        mAdapter.notifyDataSetChanged();

        /*if (isRefresh) {
            start = 0;
            lastMainId = Integer.MAX_VALUE;
        } else {
            start += count;
        }
        Log.e(TAG+"1", start +"");
        if (AppService.getInstance().getCurrentUser() != null) {
            int classId = AppService.getInstance().getCurrentUser().classid;
            String username = AppService.getInstance().getCurrentUser().username;
            AppService.getInstance().getNoticeAsync(classId, username,InfoType.HOMEWORK, start,count, lastMainId,new JsonCallback<LslResponse<List<InfoModel>>>() {
                @Override
                public void onSuccess(LslResponse<List<InfoModel>> listLslResponse, Call call, Response response) {
                    if (listLslResponse.code == LslResponse.RESPONSE_OK) {
                        mRecyclerView.setLoadingMoreEnabled(true);
                        if (isRefresh) {
                            mInfoModels.clear();
                            Toast.makeText(getActivity(), "刷新成功！", Toast.LENGTH_SHORT).show();
                            mAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getActivity(), "加载成功！", Toast.LENGTH_SHORT).show();
                            mAdapter.notifyDataSetChanged();
                        }
                        lastMainId = listLslResponse.data.get(0).mainid;
                        mInfoModels.addAll(listLslResponse.data);
                        footerView.setVisibility(View.GONE);
                    } else {
                        Log.e(TAG, "onSuccess: size =>"+mInfoModels.size() );
                        lastMainId = Integer.MAX_VALUE;
                        UIUtil.showToast(listLslResponse.msg);
                        Toast.makeText(getActivity(), "正在加载更多", Toast.LENGTH_SHORT).show();
                        footerView.setVisibility(View.VISIBLE);
                        mRecyclerView.setLoadingMoreEnabled(false);
                    }
                    if (isRefresh && mInfoModels.size() == 0){// 没有数据的话，显示图片
                        mImageView.setVisibility(View.VISIBLE);
                    } else {
                        mImageView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    if (e != null){
                        Log.e(TAG, "onError: "+e.getMessage() );
                        e.printStackTrace();
                        UIUtil.showToast(e.getMessage());
                    }
                    if (e instanceof OkGoException){
                        UIUtil.showToast("抱歉，发生了未知错误！");
                    } else if (e instanceof SocketTimeoutException){
                        UIUtil.showToast("你的手机网络太慢！");
                    } else if (e instanceof ConnectException){
                        UIUtil.showToast("对不起，你的手机没有联网！");
                    }
                }
            });
        }*/
    }

    /* @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
     }
 */
    @Override
    public void onStart() {
        super.onStart();
        /*if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//        EventBus.getDefault().register(this);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

        if (mInfoModels != null){
            mInfoModels.clear();
            mInfoModels = null;
        }
    }


    //定义处理接收方法
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(HomeworkEvent event) {
//        if (event.getInfoModel() != null){
//            mInfoModels.add(0,event.getInfoModel());
//            mAdapter.notifyDataSetChanged();
//        }
//    }



}
