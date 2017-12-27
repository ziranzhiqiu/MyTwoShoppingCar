package com.jsonqiu.shop.meituan;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsonqiu.tbshoppingcart.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeiTuanActivity extends BaseActivity {


    @BindView(R.id.m_fml_title_back)
    ImageView mFmlTitleBack;
    @BindView(R.id.m_fml_title_tv)
    TextView mTitle;

    @BindView(R.id.m_list_menu)
    RecyclerView mRecyclerMenu;
    @BindView(R.id.m_list_content)
    RecyclerView mRecyclerContent;
    @BindView(R.id.m_list_all_price)
    TextView mListAllPrice;
    @BindView(R.id.m_listview)
    View mListview;
    @BindView(R.id.m_list_submit)
    Button mListSubmit;
    @BindView(R.id.m_list_bt)
    RelativeLayout mListBt;
    @BindView(R.id.m_list_car)
    ImageView mListCar;
    @BindView(R.id.m_list_car_lay)
    RelativeLayout mCarLay;
    @BindView(R.id.m_list_num)
    TextView mListAllNum;

    public static String TAG = "MeiTuanActivity";

    /**
     * 侧边栏菜单数据填充器
     */
    private RecyclerViewMenuAdapter mRecyclerViewMenuCommonadapter = null;
    /**
     * 内容数据填充器
     */
    private RecyclerViewContentAdapter mRecyclerViewContentCommonadapter = null;
    private Context mContext;
    /**
     * 存储数据list
     */
    private List<String> stringMenuList = new ArrayList<String>();
    private List<String> stringContentList = new ArrayList<String>();

    public static int SELECTPOSITION = 0;
    /**
     * 数据操作接口
     */
    GoodsDataBaseInterface mGoodsDataBaseInterface = null;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_meituan);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        setRecyclerView();
        initHttp();
    }

    private void initView() {
        mGoodsDataBaseInterface = OperateGoodsDataBase.getInstance();
        //清空数据库缓存
        mGoodsDataBaseInterface.deleteAll(mContext);
        mTitle.setText("列表菜单");

        mListSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点餐", Toast.LENGTH_SHORT).show();
            }
        });
        mFmlTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 模拟网络请求数据
     */
    private void initHttp() {
        for (int i = 0; i < 15; i++) {
            stringMenuList.add("1111");
        }
        for (int i = 0; i < 10; i++) {
            stringContentList.add("2222");
        }
        setMenuCommonadapter();
        setContentCommonadapter();
    }

    /**
     * 设置RecyclerView的布局方式
     */
    private void setRecyclerView() {
        //垂直listview显示方式
        mRecyclerMenu.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerMenu.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mRecyclerContent.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    /**
     * 菜单列表    数据填充
     */
    private void setMenuCommonadapter() {
        mRecyclerViewMenuCommonadapter = new RecyclerViewMenuAdapter(mContext, stringMenuList);
        mRecyclerMenu.setAdapter(mRecyclerViewMenuCommonadapter);
        mRecyclerViewMenuCommonadapter.setOnItemClickListener(new RecyclerViewMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                SELECTPOSITION = position;
                Log.e(TAG, "SELECTPOSITION:" + SELECTPOSITION);
                mRecyclerViewMenuCommonadapter.notifyDataSetChanged();
                mRecyclerViewContentCommonadapter.notifyDataSetChanged();
                setAll();
                Toast.makeText(mContext,"菜单列表  = " + SELECTPOSITION,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View v, int position) {
            }
        });
    }

    /**
     * 商品种类列表    数据填充
     */
    private void setContentCommonadapter() {
        mRecyclerViewContentCommonadapter = new RecyclerViewContentAdapter(mContext, stringContentList);
        mRecyclerContent.setAdapter(mRecyclerViewContentCommonadapter);
        mRecyclerViewContentCommonadapter.setOnItemClickListener(new RecyclerViewContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewContentAdapter.ViewHolder holder) {
                Toast.makeText(mContext,"---商品种类列表---",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(RecyclerViewContentAdapter.ViewHolder holder) {
            }

            /** 添加 */
            @Override
            public void onItemJiaClick(RecyclerViewContentAdapter.ViewHolder holder) {
                String numText = holder.mNumber.getText().toString().trim();
                /** 点击加号之前还没有数据的时候 */
                if (numText.isEmpty() || numText.equals("0")) {
                    Log.e("TAG", "点击获取信息：SELECTPOSITION--" + SELECTPOSITION + "  DemoData.ListMenu_GOODSID[position]--" + DemoData.ListMenu_GOODSID[holder.getPosition()]);
                    holder.mImgJian.setVisibility(View.VISIBLE);
                    holder.mNumber.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, DemoData.ListMenu_GOODSID[holder.getPosition()], "1", DemoData.ListMenu_PPRICE[holder.getPosition()]) + "");
                    holder.mNumber.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext,"--- 第一次点餐 ---",Toast.LENGTH_SHORT).show();
                }/** 点击加号之前有数据的时候 */
                else {
                    Toast.makeText(mContext,"--- 继续加餐 ---",Toast.LENGTH_SHORT).show();
                    holder.mNumber.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, DemoData.ListMenu_GOODSID[holder.getPosition()], String.valueOf(Integer.parseInt(numText) + 1), DemoData.ListMenu_PPRICE[holder.getPosition()]) + "");
                }
                /** 动画 */
                GoodsAnimUtil.setAnim(MeiTuanActivity.this, holder.mImgJia, mCarLay);
                GoodsAnimUtil.setOnEndAnimListener(new onEndAnim());
                /** 统计购物总数和购物总价 */
            }

            /** 减少 */
            @Override
            public void onItemJianClick(RecyclerViewContentAdapter.ViewHolder holder) {
                Toast.makeText(mContext,"--- 减餐 ---",Toast.LENGTH_SHORT).show();
                String numText = holder.mNumber.getText().toString().trim();
                holder.mNumber.setText(mGoodsDataBaseInterface.saveGoodsNumber(mContext, SELECTPOSITION, DemoData.ListMenu_GOODSID[holder.getPosition()], String.valueOf(Integer.parseInt(numText) - 1), DemoData.ListMenu_PPRICE[holder.getPosition()]) + "");
                numText = holder.mNumber.getText().toString().trim();
                /** 减完之后  数据为0 */
                if (numText.equals("0")) {
                    holder.mNumber.setVisibility(View.GONE);
                    holder.mImgJian.setVisibility(View.GONE);
                }
                setAll();
            }
        });
    }

    /**
     * 动画结束后，更新所有数量和所有价格
     */
    class onEndAnim implements GoodsAnimUtil.OnEndAnimListener {
        @Override
        public void onEndAnim() {
            setAll();
        }
    }

    /**
     * 点击加号和减号的时候设置总数和总价格
     */
    private void setAll() {
        //设置所有购物数量
        if (mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) == 0) {
            mListAllNum.setVisibility(View.GONE);
            mListAllPrice.setText("￥0");
            mListAllNum.setText("0");
        } else {
            mListAllPrice.setText("￥" + mGoodsDataBaseInterface.getSecondGoodsPriceAll(mContext, SELECTPOSITION) + "");
            mListAllNum.setText(mGoodsDataBaseInterface.getSecondGoodsNumberAll(mContext, SELECTPOSITION) + "");
            mListAllNum.setVisibility(View.VISIBLE);
        }
    }
}