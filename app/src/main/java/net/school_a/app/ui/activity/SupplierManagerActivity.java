package net.school_a.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.umeng.message.PushAgent;

import net.school_a.app.DataProvider;
import net.school_a.app.R;
import net.school_a.app.adapter.SupplierAdapter;
import net.school_a.app.model.bean.SupplierDatamodel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author：Anumbrella
 * Date：16/5/24 下午7:02
 * 供应商管理
 */
public class SupplierManagerActivity extends BaseThemeSettingActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    @BindView(R.id.order_all_toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_title)
    TextView tv_Title;
    private EasyRecyclerView recyclerView;
    private SupplierAdapter adapter;
    private GridLayoutManager girdLayoutManager;
    private Handler handler = new Handler();
    private int page = 0;
    private boolean hasNetWork = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);
        ButterKnife.bind(this);
        PushAgent.getInstance(this).onAppStart();
        initView();
    }

    private void initView() {
        toolbar.setTitle("");
        tv_Title.setText("供应商管理");
        setToolbar(toolbar);
        recyclerView = (EasyRecyclerView) findViewById(R.id.order_all_data);
        adapter = new SupplierAdapter(this);
        girdLayoutManager = new GridLayoutManager(this, 2);
        girdLayoutManager.setSpanSizeLookup(adapter.obtainTipSpanSizeLookUp());
        recyclerView.setErrorView(R.layout.view_net_error);
        recyclerView.setLayoutManager(girdLayoutManager);
        recyclerView.setAdapterWithProgress(adapter);
        recyclerView.setRefreshListener(this);
        adapter.setMore(R.layout.view_more, this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(SupplierManagerActivity.this, "点击第" + position + "项", Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                SupplierDatamodel supplierDatamodel = adapter.getItem(position);
                bundle.putSerializable("model", supplierDatamodel);
                Intent intent = new Intent(SupplierManagerActivity.this, SupplierDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        onRefresh();
    }

    /**
     * 建立toolbar
     *
     * @param toolbar
     */
    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.addAll(DataProvider.getSupplierList(0));
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新
                if (!hasNetWork) {
                    adapter.pauseMore();
                    return;
                }
                adapter.addAll(DataProvider.getSupplierList(page));
                page++;
            }
        }, 2000);
    }

    @OnClick(R.id.supplier_btn)
    void addSupplier() {
        startActivity(new Intent(this, AddSupplierActivity.class));
    }
}
