package com.fengxing.mobile.commonutils.test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengxing.mobile.commonutils.R;
import com.fengxing.mobile.commonutils.utils.FakeBoldSpan;
import com.fengxing.mobile.commonutils.utils.Spanny;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class TestDeleteListActivity extends AppCompatActivity {
    private DeleteListView myListView;

    private MyAdapter adapter;

    private List<String> contentList = new ArrayList<String>();


    private LinearLayout layout;

    private Button scrollToBtn;

    private Button scrollByBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //testDeleteList();

        //setContentView(R.layout.test_rotation_rocket_view);

        //setContentView(R.layout.test_scroller_layout_activity);

        //testScrollTo();

        fakeBold();

    }

    private void fakeBold() {
        setContentView(R.layout.test_fake_bold_span);

        Spanny spanny = new Spanny()
                .append("        本人于今日收到贵公司送达的保险合同")
                .append("经逐一核对并确认本保险合同的保险单、现金价值表（如有）、保险条款、投保单、人身保险投保提示、产品说明书（如有）和交费凭证（如有）等构件齐全，客户信息资料、保单各项信息真实、准确且无误。", new FakeBoldSpan())
                .append("同时本人已详细阅读保险条款，包括保险责任和责任免除条款等内容，愿意享有保险合同中规定的各项权益同时履行合同中规定的义务。")

                .append("\n本人知悉本保险合同的电子送达回执与纸质送达回执具有同等法律效力，同意通过电子方式确认签收本保险合同。")

                .append("       保险公司提示：\n")
                .append("       1. 就以上保险合同，如果您签收过电子送达回执和纸质送达回执两种回执，我公司按收到您电子送达回执和纸质送达回执中较早者为准。\n", new FakeBoldSpan())
                .append("       2. 为保护您的合法权益，请通过拨打本公司服务电话、登陆网站或咨询本公司柜面服务人员等方式，查询、核对您的保单信息（对保险期限一年期以上的寿险保单，建议您在收到本保单之日起15个自然日内完成首次查询）。\n", new FakeBoldSpan())
                .append("       3. 对于一年期以上的寿险合同，我公司设有15天犹豫期。您于首次签收保险合同后15个自然日内可要求撤销合同。如果您在犹豫期内提出退保要求，我公司将扣除不超过10元的工本费后无息退还您所交的保险费。如果您在犹豫期之后提出退保，我公司将按合同约定支付您退保金，具体详见合同条款与保险单现金价值表。\n\n", new FakeBoldSpan())


                .append("中国人民人寿保险股份有限公司" + "branch_name:" + "\n" +
                        "地址：" + "agent.branch_address" + "\n")
                .append("邮编：" + "agent.branch_zip" + "\n", new FakeBoldSpan())

                .append("销售人员：" + "agent.agent_name" + "(" + "agent.agent_code" + ")\n" +
                        "客户服务电话：400-8895518 \n" +
                        "公司网址：http://www.picclife.com \n");


        TextView textView = (TextView) findViewById(R.id.tv_fake_bold);
        textView.setText(spanny);
    }

    private void testScrollTo() {
        setContentView(R.layout.test_scroll_by_activity);
        layout = (LinearLayout) findViewById(R.id.layout);
        scrollToBtn = (Button) findViewById(R.id.scroll_to_btn);
        scrollByBtn = (Button) findViewById(R.id.scroll_by_btn);
        scrollToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.scrollBy(-60, -100);
            }
        });
        scrollByBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.scrollBy(-30, -50);
            }
        });
    }

    private void testDeleteList() {
        setContentView(R.layout.test_delete_list_activity);
        initList();
        myListView = (DeleteListView) findViewById(R.id.my_list_view);
        myListView.setOnDeleteListener(new DeleteListView.OnDeleteListener() {
            @Override
            public void onDelete(int index) {
                contentList.remove(index);
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new MyAdapter(this, 0, contentList);
        myListView.setAdapter(adapter);
    }

    private void initList() {
        contentList.add("Content Item 1");
        contentList.add("Content Item 2");
        contentList.add("Content Item 3");
        contentList.add("Content Item 4");
        contentList.add("Content Item 5");
        contentList.add("Content Item 6");
        contentList.add("Content Item 7");
        contentList.add("Content Item 8");
        contentList.add("Content Item 9");
        contentList.add("Content Item 10");
        contentList.add("Content Item 11");
        contentList.add("Content Item 12");
        contentList.add("Content Item 13");
        contentList.add("Content Item 14");
        contentList.add("Content Item 15");
        contentList.add("Content Item 16");
        contentList.add("Content Item 17");
        contentList.add("Content Item 18");
        contentList.add("Content Item 19");
        contentList.add("Content Item 20");

    }

    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.test_list_view_item, null);
            } else {
                view = convertView;
            }
            TextView textView = (TextView) view.findViewById(R.id.text_view);
            textView.setText(getItem(position));
            return view;
        }

    }
}
